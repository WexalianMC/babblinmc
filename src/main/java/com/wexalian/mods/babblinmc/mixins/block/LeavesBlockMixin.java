package com.wexalian.mods.babblinmc.mixins.block;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin{
    @Nonnull
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ModFeatures.PASSABLE_LEAVES.getLeavesCollisionShape(state, context);
    }
}
