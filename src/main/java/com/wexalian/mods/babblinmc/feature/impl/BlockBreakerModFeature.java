package com.wexalian.mods.babblinmc.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.mods.babblinmc.util.InventoryUtil;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldEvents;

import java.util.List;

public class BlockBreakerModFeature extends ModFeature {
    private final ConfigProperty<Boolean> sound;
    private final ConfigProperty<Boolean> particles;
    
    public BlockBreakerModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        sound = config.createBooleanProperty("sound", true);
        particles = config.createBooleanProperty("particles", true);
    }
    
    @Override
    public void init() {
        for (Item item : Registry.ITEM) {
            if (item instanceof MiningToolItem) {
                DispenserBlock.registerBehavior(item, new Behavior());
            }
        }
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        registerBoolSubCommand(root, "sound", sound);
        registerBoolSubCommand(root, "particles", particles);
    }
    
    public boolean doesPlaySound() {
        return sound.get();
    }
    
    public boolean doesPlayParticles() {
        return particles.get();
    }
    
    public class Behavior extends FallibleItemDispenserBehavior {
        @Override
        protected ItemStack dispenseSilently(@Nonnull BlockPointer pointer, ItemStack stack) {
            if (isEnabled() && hasStonecutterAttached(pointer)) {
                setSuccess(false);
                
                BlockState state = pointer.getBlockState();
                Direction front = state.get(DispenserBlock.FACING);
                BlockPos mineablePos = pointer.getPos().offset(front);
                BlockState mineableState = pointer.getWorld().getBlockState(mineablePos);
                
                if (stack.isSuitableFor(mineableState)) {
                    int remaining = stack.getMaxDamage() - stack.getDamage();
                    if (remaining > 1) {
                        Direction back = front.getOpposite();
                        BlockPos inventoryPos = pointer.getPos().offset(back);
                        Inventory inventory = HopperBlockEntity.getInventoryAt(pointer.getWorld(), inventoryPos);
                        
                        if (inventory == null) {
                            breakAndDrop(pointer, stack, mineablePos);
                        }
                        else {
                            breakAndTransfer(pointer, stack, mineablePos, mineableState, back, inventory);
                        }
                    }
                }
                return stack;
            }
            else return super.dispenseSilently(pointer, stack);
        }
        
        private boolean hasStonecutterAttached(BlockPointer pointer) {
            for (Direction value : Direction.values()) {
                BlockState state = pointer.getWorld().getBlockState(pointer.getPos().offset(value));
                if (state.getBlock() == Blocks.STONECUTTER) {
                    return true;
                }
            }
            return false;
        }
        
        private void breakAndDrop(@Nonnull BlockPointer pointer, ItemStack stack, BlockPos mineablePos) {
            pointer.getWorld().breakBlock(mineablePos, true);
            stack.damage(1, pointer.getWorld().getRandom(), null);
            setSuccess(true);
        }
        
        private void breakAndTransfer(@Nonnull BlockPointer pointer, ItemStack stack, BlockPos mineablePos, BlockState mineableState, Direction back, Inventory inventory) {
            BlockEntity frontBlockEntity = mineableState.hasBlockEntity() ? pointer.getWorld().getBlockEntity(mineablePos) : null;
            List<ItemStack> drops = Block.getDroppedStacks(mineableState, pointer.getWorld(), mineablePos, frontBlockEntity, null, stack);
            
            if (InventoryUtil.putItems(inventory, drops, back, false)) {
                InventoryUtil.putItems(inventory, drops, back, true);
                pointer.getWorld().breakBlock(mineablePos, false);
                stack.damage(1, pointer.getWorld().getRandom(), null);
                setSuccess(true);
            }
        }
        
        @Override
        protected void playSound(BlockPointer pointer) {
            if (isSuccess() && doesPlaySound()) {
                pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.getPos(), 0);
            }
        }
        
        @Override
        protected void spawnParticles(BlockPointer pointer, Direction side) {
            if (isSuccess() && doesPlayParticles()) {
                pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pointer.getPos(), side.getId());
            }
        }
    }
    
}
