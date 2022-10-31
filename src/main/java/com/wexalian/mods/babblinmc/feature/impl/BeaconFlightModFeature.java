package com.wexalian.mods.babblinmc.feature.impl;

import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.mods.babblinmc.init.BabblinMCEffects;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BeaconFlightModFeature extends ModFeature {
    
    private final ConfigProperty<Integer> levelRequired;
    
    public BeaconFlightModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        levelRequired = config.createIntegerProperty("level_required", 30);
    }
    
    @Override
    public void init() {
        if (isEnabled()) {
            addFlightEffect();
        }
        
        enabled.addListener(((oldV, newV) -> {
            if (newV) addFlightEffect();
            else removeFlightEffect();
        }));
    }
    
    private void addFlightEffect() {
        BeaconBlockEntity.EFFECTS_BY_LEVEL[3] = new StatusEffect[]{StatusEffects.REGENERATION, BabblinMCEffects.FLIGHT_EFFECT};
        BeaconBlockEntity.EFFECTS = Arrays.stream(BeaconBlockEntity.EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());
    }
    
    private void removeFlightEffect() {
        BeaconBlockEntity.EFFECTS_BY_LEVEL[3] = new StatusEffect[]{StatusEffects.REGENERATION};
        BeaconBlockEntity.EFFECTS = Arrays.stream(BeaconBlockEntity.EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());
    }
    
    public boolean canFly(PlayerEntity player) {
        return isEnabled() && player.experienceLevel >= levelRequired.get();
    }
}
