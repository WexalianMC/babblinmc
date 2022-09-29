package com.wexalian.mods.babblinmc.feature.impl;

import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.config.ListConfigProperty;
import com.wexalian.mods.babblinmc.command.argument.ItemArgumentType;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.List;

public class EnderTankModFeature extends ModFeature {
    private final ConfigProperty<Integer> size;
    private final ListConfigProperty<Item> privateUpgrades;
    
    public EnderTankModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        size = config.createIntegerProperty("size", 32);
        privateUpgrades = config.createListProperty("private_upgrades", () -> List.of(Items.DIAMOND), new TypeToken<>() {});
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        registerIntSubCommand(root, "size", size);
        registerListSubCommand(root, "private_upgrades", privateUpgrades, this::getItemArgumentType, ItemArgumentType::getItem);
    }
    
    private ArgumentType<Item> getItemArgumentType(SubCommand subCommand) {
        return switch (subCommand) {
            case GET, REMOVE -> ItemArgumentType.permitted(privateUpgrades);
            case ADD -> ItemArgumentType.all();
        };
    }
    
    public int getSize() {
        return size.get();
    }
    
    public Collection<Item> getPrivateUpgrades() {
        return privateUpgrades;
    }
}
