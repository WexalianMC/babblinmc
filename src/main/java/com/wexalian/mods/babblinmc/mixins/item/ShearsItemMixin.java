package com.wexalian.mods.babblinmc.mixins.item;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = ModFeatures.SHEAR_BAMBOO.useOnBlock(context);
        if (result.isAccepted()) {
            cir.setReturnValue(result);
        }
    }
}
