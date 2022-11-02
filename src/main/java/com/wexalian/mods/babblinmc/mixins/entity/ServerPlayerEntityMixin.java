package com.wexalian.mods.babblinmc.mixins.entity;

import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void onSpawn(CallbackInfo ci) {
        for (ModFeature feature : ModFeatures.getModFeatures()) {
            feature.updateClient(getSelf());
        }
    }
    
    private ServerPlayerEntity getSelf() {
        return (ServerPlayerEntity) (Object) this;
    }
}
