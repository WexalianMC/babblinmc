package com.wexalian.mods.babblinmc.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DoubleDoorModFeature extends ModFeature {
    private final ConfigProperty<Boolean> playerEnabled;
    private final ConfigProperty<Boolean> redstoneEnabled;
    
    public DoubleDoorModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        playerEnabled = config.createBooleanProperty("player_enabled", true);
        redstoneEnabled = config.createBooleanProperty("redstone_enabled", true);
    }
    
    public ActionResult onPlayerActivated(DoorBlock door, BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (isEnabled() && isPlayerEnabled()) {
            if (!DoorBlock.isWoodenDoor(state)) {
                return ActionResult.PASS;
            }
            
            state = state.cycle(DoorBlock.OPEN);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
            world.syncWorldEvent(player, state.get(DoorBlock.OPEN) ? door.getOpenSoundEventId() : door.getCloseSoundEventId(), pos, 0);
            world.emitGameEvent(player, state.get(DoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            ////////////////////////////
            if (!player.isSneaking()) {
                openCloseOtherDoor(world, pos, state, player, false);
            }
            ////////////////////////////
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
    
    public boolean onRedstoneActivated(DoorBlock door, BlockState state, World world, BlockPos pos, Block block) {
        if (isEnabled() && isRedstoneEnabled()) {
            BlockPos otherPos = pos.offset(state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
            boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(otherPos);
            
            if (!door.getDefaultState().isOf(block) && powered != state.get(DoorBlock.POWERED)) {
                if (powered != state.get(DoorBlock.OPEN)) {
                    world.syncWorldEvent(null, powered ? door.getOpenSoundEventId() : door.getCloseSoundEventId(), pos, 0);
                    world.emitGameEvent(powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
                state = state.with(DoorBlock.POWERED, powered).with(DoorBlock.OPEN, powered);
                world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
                ////////////////////////////
                openCloseOtherDoor(world, pos, state, null, true);
                ////////////////////////////
            }
            return true;
        }
        return false;
    }
    
    private void openCloseOtherDoor(World world, BlockPos pos, BlockState state, PlayerEntity player, boolean redstone) {
        DoorHinge hinge = state.get(DoorBlock.HINGE);
        Direction facing = state.get(DoorBlock.FACING);
        boolean open = state.get(DoorBlock.OPEN);
        
        Direction opposite = facing.getOpposite();
        Direction hingeDir = hinge == DoorHinge.LEFT ? opposite.rotateYCounterclockwise() : opposite.rotateYClockwise();
        
        BlockPos doorPos = pos.offset(hingeDir);
        BlockState doorState = world.getBlockState(doorPos);
        if (doorState.isOf(state.getBlock()) && (redstone || DoorBlock.isWoodenDoor(doorState))) {
            if (doorState.get(DoorBlock.FACING) == facing) {
                if (doorState.get(DoorBlock.HINGE) != hinge) {
                    if (doorState.get(DoorBlock.OPEN) != open) {
                        world.setBlockState(doorPos, doorState.cycle(DoorBlock.OPEN), Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
                        world.emitGameEvent(player, doorState.get(DoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, doorPos);
                    }
                }
            }
        }
    }
    
    private boolean isPlayerEnabled() {
        return playerEnabled.get();
    }
    
    private boolean isRedstoneEnabled() {
        return redstoneEnabled.get();
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        registerBoolSubCommand(root, "player", playerEnabled);
        registerBoolSubCommand(root, "redstone", redstoneEnabled);
    }
}
