package com.wexalian.mods.babblinmc.feature;

import com.wexalian.common.util.StringUtil;
import com.wexalian.config.ConfigHandler;
import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.config.ModConfig;
import com.wexalian.mods.babblinmc.feature.impl.*;
import com.wexalian.mods.babblinmc.feature.impl.block.EnderTankModFeature;
import com.wexalian.mods.babblinmc.feature.impl.block.PumpModFeature;
import com.wexalian.nullability.annotations.Nonnull;

import java.util.*;
import java.util.function.BiFunction;

public final class ModFeatures {
    private static final Map<String, ModFeature> MOD_FEATURES = new HashMap<>();
    
    public static final PumpModFeature PUMP = ModFeatures.register("pump", PumpModFeature::new);
    public static final EnderTankModFeature ENDER_TANK = ModFeatures.register("ender_tank", EnderTankModFeature::new);
    
    public static final MusicDiscModFeature MUSIC_DISCS_DISPENSER = ModFeatures.register("dispenser#music_discs", MusicDiscModFeature::new);
    public static final BlockBreakerModFeature BLOCK_BREAKER_DISPENSER = ModFeatures.register("dispenser#block_breaker", BlockBreakerModFeature::new);
    
    public static final DoubleDoorModFeature DOUBLE_DOORS = ModFeatures.register("double_doors", DoubleDoorModFeature::new);
    
    public static final ExtraCompostableModFeature EXTRA_COMPOSTABLES = ModFeatures.register("extra_compostables", ExtraCompostableModFeature::new);
    public static final ShearBambooModFeature SHEAR_BAMBOO = ModFeatures.register("shear_bamboo", ShearBambooModFeature::new);
    public static final PassableLeavesModFeature PASSABLE_LEAVES = ModFeatures.register("passable_leaves", PassableLeavesModFeature::new);
    
    public static final WeatherSleepModFeature WEATHER_SLEEP = ModFeatures.register("weather_sleep", WeatherSleepModFeature::new);
    
    
    
    public static void setup() {}
    
    public static <T extends ModFeature> T register(@Nonnull String categoryId, @Nonnull BiFunction<String, ConfigHandler, T> factory) {
        ConfigHandler config = ModConfig.instance().getConfig();
        config.pushCategory("feature");
        config.pushCategory(categoryId);
    
        String id = parseId(categoryId);
    
        T feature = factory.apply(id, config);
        MOD_FEATURES.put(id, feature);
        
        config.popCategory();
        config.popCategory();
        return feature;
    }
    
    private static String parseId(@Nonnull String category) {
        String id = category;
        if(id.contains("#")){
            String[] split = id.split("#");
            id = split[split.length - 1];
        }
        return id;
    }
    
    public static void init() {
        List<ModFeature> enabled = new ArrayList<>();
        List<ModFeature> disabled = new ArrayList<>();
        
        for (ModFeature feature : MOD_FEATURES.values()) {
            feature.init();
            
            if (feature.isEnabled()) enabled.add(feature);
            else disabled.add(feature);
        }
        
        BabblinMC.LOGGER.info("Loaded {} mod features: ", MOD_FEATURES.size());
        BabblinMC.LOGGER.info("    Enabled: {} [{}]", enabled.size(), StringUtil.join(enabled, ", ", ModFeature::getId));
        BabblinMC.LOGGER.info("    Disabled: {} [{}]", disabled.size(), StringUtil.join(disabled, ", ", ModFeature::getId));
    }
    
    public static Collection<ModFeature> getModFeatures() {
        return MOD_FEATURES.values();
    }
    
    public static ModFeature getModFeature(String id) {
        return MOD_FEATURES.get(id);
    }
}
