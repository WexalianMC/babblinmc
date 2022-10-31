package com.wexalian.mods.babblinmc.effect;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FlightEffect extends StatusEffect {
    
    public FlightEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xE1F2FC);
    }
    
    private final ArrayList<PlayerEntity> playersToRemoveFlight = new ArrayList<>();
    
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player && ModFeatures.BEACON_FLIGHT.canFly(player)) {
            player.getAbilities().allowFlying = true;
            player.sendAbilitiesUpdate();
        }
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            playersToRemoveFlight.remove(player);
        }
    }
    
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            if (!player.isCreative() && !player.isSpectator()) {
                playersToRemoveFlight.add(player);
                
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (playersToRemoveFlight.contains(player)) {
                            playersToRemoveFlight.remove(player);
                            
                            player.getAbilities().allowFlying = false;
                            player.getAbilities().flying = false;
                            
                            // if (WingedBeaconsConfig.INSTANCE.slowFallingTime >= 1)
                            //     player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                            //                                                     WingedBeaconsConfig.INSTANCE.slowFallingTime * 20,
                            //                                                     1,
                            //                                                     false,
                            //                                                     false));
                        }
                        else {
                            player.getAbilities().allowFlying = ModFeatures.BEACON_FLIGHT.canFly(player);
                        }
                        player.sendAbilitiesUpdate();
                    }
                };
                
                Timer timer = new Timer("FlightEffectTimer");
                timer.schedule(task, 2000L);
            }
        }
    }
}