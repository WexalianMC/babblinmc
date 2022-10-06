package com.wexalian.mods.babblinmc.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PassableLeavesModFeature extends ModFeature {
    private final ConfigProperty<Boolean> players;
    private final ConfigProperty<Boolean> hostile;
    private final ConfigProperty<Boolean> passive;
    private final ConfigProperty<Boolean> items;
    private final ConfigProperty<Boolean> persistent;
    
    public PassableLeavesModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        players = config.createBooleanProperty("player", true);
        hostile = config.createBooleanProperty("hostile", false);
        passive = config.createBooleanProperty("passive", false);
        items = config.createBooleanProperty("items", false);
        persistent = config.createBooleanProperty("persistent", false);
    }
    
    @Nonnull
    public VoxelShape getLeavesCollisionShape(BlockState state, ShapeContext context) {
        if (isEnabled()) {
            if (context instanceof EntityShapeContext entityContext) {
                if (persistent.get() && state.get(LeavesBlock.PERSISTENT)) {
                    if (entityContext.getEntity() instanceof PlayerEntity && players.get()) {
                        return VoxelShapes.empty();
                    }
                    if (entityContext.getEntity() instanceof HostileEntity && hostile.get()) {
                        return VoxelShapes.empty();
                    }
                    if (entityContext.getEntity() instanceof PassiveEntity && passive.get()) {
                        return VoxelShapes.empty();
                    }
                    if (entityContext.getEntity() instanceof ItemEntity && items.get()) {
                        return VoxelShapes.empty();
                    }
                }
            }
        }
        return VoxelShapes.fullCube();
    }
    
    public boolean shouldVineConnectTo(BlockView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return isEnabled() && state.getBlock() instanceof LeavesBlock;
    }
    
    public boolean canPlaceSnowLayerAt(BlockState state) {
        return isEnabled() && state.getBlock() instanceof LeavesBlock;
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        registerBoolSubCommand(root, "players", players);
        registerBoolSubCommand(root, "hostile", hostile);
        registerBoolSubCommand(root, "passive", passive);
        registerBoolSubCommand(root, "items", items);
        registerBoolSubCommand(root, "persistent", persistent);
    }
}
