package com.wexalian.mods.babblinmc.feature.impl;

import com.wexalian.config.ConfigHandler;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class WeatherSleepModFeature extends ModFeature {
    public WeatherSleepModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(this::canSleep);
    }
    
    private ActionResult canSleep(PlayerEntity player, BlockPos pos, boolean night) {
        if (isEnabled()) {
            if (!night && player.getWorld().isRaining()) {
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
