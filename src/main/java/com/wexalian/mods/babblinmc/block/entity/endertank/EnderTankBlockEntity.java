package com.wexalian.mods.babblinmc.block.entity.endertank;

import com.wexalian.mods.babblinmc.block.EnderTankBlock;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.mods.babblinmc.init.BabblinMCBlockEntities;
import com.wexalian.nullability.annotations.Nonnull;
import com.wexalian.nullability.annotations.Nullable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class EnderTankBlockEntity extends BlockEntity implements IEnderTankHandler {
    @Nonnull
    private ColorCode code = ColorCode.ALL_WHITE;
    
    @Nonnull
    private TankOwner owner = TankOwner.ALL;
    
    public EnderTankBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(BabblinMCBlockEntities.ENDER_TANK, pos, state);
    }
    
    public void setColorCode(@Nonnull DyeColor color) {
        if (code.getColor(0) != color || code.getColor(1) != color || code.getColor(2) != color) {
            setOwnerAndCode(owner, ColorCode.of(color, color, color));
            markDirty();
        }
    }
    
    public boolean setColor(int ring, @Nonnull DyeColor color) {
        if (code.getColor(ring) != color) {
            setOwnerAndCode(owner, code.withColor(ring, color));
            markDirty();
            return true;
        }
        return false;
    }
    
    public void setOwner(@Nonnull TankOwner owner) {
        setOwnerAndCode(owner, code);
        markDirty();
    }
    
    @Override
    public void markDirty() {
        if (world != null) {
            BlockState state = getCachedState();
            BlockEntity.markDirty(world, pos, state);
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }
    
    @Nonnull
    public ColorCode getCode() {
        return code;
    }
    
    @Nonnull
    public TankOwner getOwner() {
        return owner;
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("owner", owner.toNbt());
        nbt.putString("code", code.serializeString());
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        ColorCode code = ColorCode.deserializeString(nbt.getString("code"));
        TankOwner owner = TankOwner.readNbt(nbt.getCompound("owner"));
        
        setOwnerAndCode(owner, code);
        
        markDirty();
    }
    
    private void setOwnerAndCode(@Nonnull TankOwner owner, @Nonnull ColorCode code) {
        getEnderTank().removeListener(this);
        this.owner = owner;
        this.code = code;
        getEnderTank().addListener(this);
    }
    
    @Nonnull
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
    }
    
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Nonnull
    public EnderTank getEnderTank() {
        return EnderTankManager.getEnderTank(owner, code, world == null || world.isClient);
    }
    
    public FluidVariant getFluidVariant() {
        return getEnderTank().getFluidStorage().getResource();
    }
    
    @Override
    public void update() {
        markDirty();
    }
    
    @Nullable
    public Storage<FluidVariant> getFluidStorage() {
        if (ModFeatures.ENDER_TANK.isEnabled()) {
            var storage = getEnderTank().getFluidStorage();
            EnderTankBlock.FlowDirection flow = getCachedState().get(EnderTankBlock.FLOW);
            return FilteringStorage.of(storage, flow.isInput(), flow.isOutput());
        }
        return null;
    }
}
