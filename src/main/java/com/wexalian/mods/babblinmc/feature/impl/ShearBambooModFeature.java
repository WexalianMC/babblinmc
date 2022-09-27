package com.wexalian.mods.babblinmc.feature.impl;

import com.wexalian.config.ConfigHandler;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BambooLeaves;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShearBambooModFeature extends ModFeature {
    public ShearBambooModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
    }
    
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (isEnabled()) {
            PlayerEntity player = context.getPlayer();
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if (state.isOf(Blocks.BAMBOO)) {
                pos = getTopBambooBlock(world, pos);
                world.setBlockState(pos, state.with(BambooBlock.STAGE, 1));
                updateLeaves(world, pos);
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, context.getStack());
                }
                world.playSound(player, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (player != null) {
                    context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                }
                return ActionResult.success(world.isClient);
            }
        }
        
        return ActionResult.PASS;
    }
    
    private BlockPos getTopBambooBlock(World world, BlockPos pos) {
        BlockPos topPos = pos;
        while (world.getBlockState(topPos.up()).isOf(Blocks.BAMBOO)) {
            topPos = topPos.up();
        }
        return topPos;
    }
    
    public void updateLeaves(World world, BlockPos pos) {
        if(updateLeaves(world, pos, BambooLeaves.LARGE)){
            if(updateLeaves(world, pos.down(), BambooLeaves.LARGE)) {
                if(!updateLeaves(world, pos.down(2), BambooLeaves.SMALL)){
                    updateLeaves(world, pos.down(), BambooLeaves.SMALL);
                }
            }
        }
        // updateLeaves(world, pos.down(), BambooLeaves.LARGE);
        // updateLeaves(world, pos.down(2), BambooLeaves.SMALL);
    }
    
    private boolean updateLeaves(World world, BlockPos pos, BambooLeaves leaves) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BambooBlock) {
            world.setBlockState(pos, state.with(BambooBlock.LEAVES, leaves), Block.NOTIFY_LISTENERS);
            return true;
        }
        return false;
    }
}
