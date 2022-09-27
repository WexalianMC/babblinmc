package com.wexalian.mods.babblinmc.mixins.block;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onDoorUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = ModFeatures.DOUBLE_DOORS.onPlayerActivated(getSelf(), state, world, pos, player);
        if (result.isAccepted()) {
            cir.setReturnValue(result);
        }
    }
    
    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void onDoorPowered(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci) {
        boolean success = ModFeatures.DOUBLE_DOORS.onRedstoneActivated(getSelf(), state, world, pos, block);
        if (success) {
            ci.cancel();
        }
    }
    
    private DoorBlock getSelf() {
        return (DoorBlock) (Object) this;
    }
}
