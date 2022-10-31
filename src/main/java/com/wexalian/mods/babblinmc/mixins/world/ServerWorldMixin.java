package com.wexalian.mods.babblinmc.mixins.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean doDaylightCycle(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
        if (rule == GameRules.DO_DAYLIGHT_CYCLE) {
            return instance.getBoolean(rule) && getSelf().isNight();
        }
        return instance.getBoolean(rule);
    }
    
    private ServerWorld getSelf() {
        return (ServerWorld) (Object) this;
    }
}

/*
@Inject(
  method = "foo()V",
  at = @At(
    value = "INVOKE",
    target = "La/b/c/Something;doSomething()V"
  ),
  slice = @Slice(
    from = @At(value = "INVOKE", target = "La/b/c/Something;doSomething2()V"),
    to = @At(value = "INVOKE", target = "La/b/c/Something;doSomething3()V")
  )
)
private void injected(CallbackInfo ci) {
  doSomething5();
}
*/
