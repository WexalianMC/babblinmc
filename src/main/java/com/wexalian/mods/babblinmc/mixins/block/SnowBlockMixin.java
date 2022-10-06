package com.wexalian.mods.babblinmc.mixins.block;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
    
    @Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
    public void canPlaceSnowLayerAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ModFeatures.PASSABLE_LEAVES.canPlaceSnowLayerAt(state)) {
            cir.setReturnValue(true);
        }
    }
    
}