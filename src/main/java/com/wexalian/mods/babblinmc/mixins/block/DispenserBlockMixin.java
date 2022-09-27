package com.wexalian.mods.babblinmc.mixins.block;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.block.DispenserBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
    
    @Inject(method = "dispense", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V", shift = At.Shift.BEFORE), cancellable = true)
    protected void dispense(ServerWorld world, BlockPos pos, CallbackInfo callback) {
        boolean success = ModFeatures.MUSIC_DISCS_DISPENSER.onDispenseEmpty(new BlockPointerImpl(world, pos));
        if (success) {
            callback.cancel();
        }
    }
}
