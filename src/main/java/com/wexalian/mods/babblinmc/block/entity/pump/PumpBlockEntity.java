package com.wexalian.mods.babblinmc.block.entity.pump;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.mods.babblinmc.init.BabblinMCBlockEntities;
import com.wexalian.nullability.annotations.Nullable;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.common.fluid.FluidUtils;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.LinkedList;
import java.util.Queue;

public class PumpBlockEntity extends BlockEntity {
    private final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }
        
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET * ModFeatures.PUMP.getTankCapacity();
        }
        
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(ModFeatures.PUMP.getEnergyCapacity(),
                                                                              ModFeatures.PUMP.getEnergyCapacity(),
                                                                              0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    
    @Nullable
    private BlockPos currentPos;
    private int ticks;
    private int range = -1;
    private final Queue<BlockPos> surfaces = new LinkedList<>();
    
    @SuppressWarnings("unchecked")
    public BlockApiCache<Storage<FluidVariant>, Direction>[] sideCache = new BlockApiCache[6];
    
    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(BabblinMCBlockEntities.PUMP, pos, state);
    }
    
    public void tick() {
        if (ModFeatures.PUMP.isEnabled()) {
            if (world != null && !world.isClient()) {
                if (interval(ModFeatures.PUMP.getPumpSpeed()) && getState().isWorking()) {
                    pump();
                }
                if (interval(ModFeatures.PUMP.getOutputSpeed()) && getState().isOutput()) {
                    outputFluid();
                }
                ticks++;
            }
        }
    }
    
    private void outputFluid() {
        for (Direction dir : Direction.values()) {
            if (!fluidStorage.getResource().isBlank() && fluidStorage.getAmount() > 0) {
                Storage<FluidVariant> destination = getAdjacentFluidStorage(dir);
                if (destination != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        long maxInsert = Math.min(fluidStorage.getAmount(), FluidConstants.BUCKET);
                        long insert = destination.insert(fluidStorage.getResource(), maxInsert, transaction);
                        if (insert > 0) {
                            long extract = fluidStorage.extract(fluidStorage.getResource(), insert, transaction);
                            if (insert == extract) {
                                transaction.commit();
                            }
                        }
                    }
                }
            }
        }
    }
    
    private Storage<FluidVariant> getAdjacentFluidStorage(Direction dir) {
        if (world instanceof ServerWorld serverWorld) {
            if (sideCache[dir.ordinal()] == null) {
                sideCache[dir.ordinal()] = BlockApiCache.create(FluidStorage.SIDED, serverWorld, pos.offset(dir));
            }
            return sideCache[dir.ordinal()].find(dir.getOpposite());
        }
        return null;
    }
    
    private boolean interval(int interval) {
        return interval == 0 || ticks % interval == 0;
    }
    
    private void pump() {
        if (world != null) {
            if (currentPos == null || currentPos.getY() == world.getDimension().getMinimumY()) {
                if (surfaces.isEmpty()) {
                    range++;
                    if (range > ModFeatures.PUMP.getMaxRange()) return;
                    rebuildSurfaces();
                }
                currentPos = surfaces.poll();
            }
            else currentPos = currentPos.down();
            
            int usePerMove = ModFeatures.PUMP.getEnergyUsePerMove();
            if (energyStorage.getAmount() >= usePerMove) {
                energyStorage.amount -= usePerMove;
            }
            
            FluidVariant drained = drainAt(currentPos, true);
            
            if (!drained.isBlank() && (fluidStorage.getResource().isBlank() || fluidStorage.getResource()
                                                                                           .isOf(drained.getFluid())) && fluidStorage.getCapacity() - fluidStorage.getAmount() >= FluidConstants.BLOCK) {
                drained = drainAt(currentPos, false);
                
                if (!drained.isBlank()) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        fluidStorage.insert(drained, FluidConstants.BLOCK, transaction);
                        transaction.commit();
                    }
                    
                    if (ModFeatures.PUMP.getReplaceStone() && drained.getFluid() instanceof LavaFluid) {
                        world.setBlockState(currentPos, Blocks.STONE.getDefaultState(), 11);
                    }
                    
                    energyStorage.amount -= ModFeatures.PUMP.getEnergyUsePerDrain();
                }
            }
            markDirty();
        }
    }
    
    private FluidVariant drainAt(BlockPos pos, boolean simulate) {
        if (world != null) {
            BlockState frontBlockState = world.getBlockState(pos);
            Block frontBlock = frontBlockState.getBlock();
            
            if (frontBlock instanceof FluidBlock) {
                // @Volatile: Logic from LiquidBlock#pickupFluid
                if (frontBlockState.get(FluidBlock.LEVEL) == 0) {
                    Fluid fluid = FluidUtils.fluidFromBlock(frontBlock);
                    
                    if (!simulate) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                    }
                    
                    return FluidVariant.of(fluid);
                }
            }
            // else if (frontBlock instanceof IFluidBlock) {
            //     IFluidBlock fluidBlock = (IFluidBlock) frontBlock;
            //
            //     if (fluidBlock.canDrain(level, pos)) {
            //         return fluidBlock.drain(level, pos, action);
            //     }
            // }
        }
        return FluidVariant.blank();
    }
    
    @Override
    public void markRemoved() {
        super.markRemoved();
        if (surfaces.isEmpty()) {
            rebuildSurfaces();
        }
    }
    
    private void rebuildSurfaces() {
        surfaces.clear();
        
        if (range == -1) {
            surfaces.add(getPos().down());
            
            return;
        }
        
        int hl = 3 + 2 * range;
        int vl = 1 + 2 * range;
        
        // Top
        for (int i = 0; i < hl; ++i) {
            surfaces.add(getPos().add(-range - 1 + i, -1, -range - 1));
        }
        
        // Right
        for (int i = 0; i < vl; ++i) {
            surfaces.add(getPos().add(-range - 1 + vl + 1, -1, -range - 1 + i + 1));
        }
        
        // Bottom
        for (int i = 0; i < hl; ++i) {
            surfaces.add(getPos().add(-range - 1 + hl - i - 1, -1, -range - 1 + hl - 1));
        }
        
        // Left
        for (int i = 0; i < vl; ++i) {
            surfaces.add(getPos().add(-range - 1, -1, -range - 1 + vl - i));
        }
    }
    
    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        
        tag.put("fluid", fluidStorage.variant.toNbt());
        tag.putLong("amount", fluidStorage.amount);
        
        tag.putLong("energy", energyStorage.amount);
        tag.putInt("range", range);
        
        if (currentPos != null) {
            tag.putLong("current", currentPos.asLong());
        }
        
        NbtList surfaceNbt = new NbtList();
        surfaces.forEach(pos -> surfaceNbt.add(NbtLong.of(pos.asLong())));
        tag.put("surfaces", surfaceNbt);
    }
    
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        
        fluidStorage.variant = FluidVariant.fromNbt(tag.getCompound("fluid"));
        fluidStorage.amount = tag.getLong("amount");
        
        energyStorage.amount = tag.getLong("energy");
        range = tag.getInt("range");
        
        if (tag.contains("current")) {
            currentPos = BlockPos.fromLong(tag.getLong("current"));
        }
        
        NbtList surfaceNbt = tag.getList("surfaces", NbtElement.LONG_TYPE);
        surfaceNbt.forEach(element -> surfaces.add(BlockPos.fromLong(((NbtLong) element).longValue())));
    }
    
    public PumpState getState() {
        if (ModFeatures.PUMP.isEnabled()) {
            if (world != null) {
                if (range > ModFeatures.PUMP.getMaxRange()) {
                    return PumpState.DONE;
                }
                else if (world.isReceivingRedstonePower(getPos())) {
                    return PumpState.REDSTONE;
                }
                else if (energyStorage.getAmount() <= 0) {
                    return PumpState.NO_ENERGY;
                }
                else if (fluidStorage.getAmount() > fluidStorage.getCapacity() - FluidConstants.BUCKET) {
                    return PumpState.TANK_FULL;
                }
                else {
                    return PumpState.WORKING;
                }
            }
            return PumpState.ERROR;
        }
        return PumpState.DISABLED;
    }
    
    @Nullable
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        if (ModFeatures.PUMP.isEnabled()) {
            return fluidStorage;
        }
        return null;
    }
    
    @Nullable
    public SimpleEnergyStorage getEnergyStorage() {
        if (ModFeatures.PUMP.isEnabled()) {
            return energyStorage;
        }
        return null;
    }
    
    @Nullable
    public BlockPos getCurrentPos() {
        return currentPos;
    }
    
    public int getRange() {
        return range;
    }
}
