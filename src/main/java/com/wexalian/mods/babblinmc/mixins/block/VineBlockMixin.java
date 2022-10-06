package com.wexalian.mods.babblinmc.mixins.block;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VineBlock.class)
public class VineBlockMixin {
    @Inject(at = @At("HEAD"), method = "shouldConnectTo", cancellable = true)
    private static void shouldVineConnectTo(BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (ModFeatures.PASSABLE_LEAVES.shouldVineConnectTo(world, pos)) {
            cir.setReturnValue(true);
        }
    }
}
