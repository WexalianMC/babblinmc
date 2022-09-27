package com.wexalian.mods.babblinmc.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.server.command.ServerCommandSource;

public class PumpModFeature extends ModFeature {
    
    private final ConfigProperty<Integer> maxRange;
    private final ConfigProperty<Integer> pumpSpeed;
    private final ConfigProperty<Integer> outputSpeed;
    private final ConfigProperty<Integer> tankCapacity;
    private final ConfigProperty<Integer> energyCapacity;
    private final ConfigProperty<Integer> energyUsePerMove;
    private final ConfigProperty<Integer> energyUsePerDrain;
    private final ConfigProperty<Boolean> replaceStone;
    
    public PumpModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        maxRange = config.createIntegerProperty("max_range", 64);
        pumpSpeed = config.createIntegerProperty("pump_speed", 8);
        outputSpeed = config.createIntegerProperty("output_speed", 8);
        tankCapacity = config.createIntegerProperty("tank_capacity", 32);
        energyCapacity = config.createIntegerProperty("energy_capacity", 32000);
        energyUsePerMove = config.createIntegerProperty("energy_use_per_move", 0);
        energyUsePerDrain = config.createIntegerProperty("energy_use_per_drain", 128);
        replaceStone = config.createBooleanProperty("replace_stone", true);
    }
    
    public int getMaxRange() {
        return maxRange.get();
    }
    
    public int getPumpSpeed() {
        return pumpSpeed.get();
    }
    
    public int getOutputSpeed() {
        return outputSpeed.get();
    }
    
    public int getTankCapacity() {
        return tankCapacity.get();
    }
    
    public int getEnergyCapacity() {
        return energyCapacity.get();
    }
    
    public int getEnergyUsePerMove() {
        return energyUsePerMove.get();
    }
    
    public int getEnergyUsePerDrain() {
        return energyUsePerDrain.get();
    }
    
    public boolean getReplaceStone() {
        return replaceStone.get();
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        registerIntSubCommand(root, "max_range", maxRange);
        registerIntSubCommand(root, "pump_speed", pumpSpeed);
        registerIntSubCommand(root, "output_speed", outputSpeed);
        registerIntSubCommand(root, "tank_capacity", tankCapacity);
        registerIntSubCommand(root, "energy_capacity", energyCapacity);
        registerIntSubCommand(root, "energy_use_per_move", energyUsePerMove);
        registerIntSubCommand(root, "energy_use_per_drain", energyUsePerDrain);
        registerBoolSubCommand(root, "replace_stone", replaceStone);
    }
}
