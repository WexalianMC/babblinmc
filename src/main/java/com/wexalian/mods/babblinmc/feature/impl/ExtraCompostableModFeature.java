package com.wexalian.mods.babblinmc.feature.impl;

import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.MapConfigProperty;
import com.wexalian.mods.babblinmc.command.argument.ItemArgumentType;
import com.wexalian.mods.babblinmc.command.argument.PermittedFloatArgumentType;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class ExtraCompostableModFeature extends ModFeature {
    private final MapConfigProperty<Item, Float> compostables;
    
    public ExtraCompostableModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        super(id, config);
        
        compostables = config.createMapProperty("compostables", new TypeToken<>() {});
    }
    
    @Override
    public void init() {
        if (isEnabled()) {
            compostables.forEach(this::addCompostableitem);
        }
        
        enabled.addListener(((oldV, newV) -> {
            if (newV) compostables.forEach(this::addCompostableitem);
            else compostables.forEach(this::removeCompostableItem);
        }));
        compostables.addListener((removed, added) -> {
            removed.forEach(this::removeCompostableItem);
            added.forEach(this::addCompostableitem);
        });
    }
    
    private void addCompostableitem(Item item, float amount) {
        CompostingChanceRegistry.INSTANCE.add(item, amount);
    }
    
    private void removeCompostableItem(Item item, float amount) {
        CompostingChanceRegistry.INSTANCE.remove(item);
    }
    
    @Override
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        super.registerSubCommands(root);
        
        root.then(literal("reset").executes(this::resetCompostables));
        
        registerMapSubCommand(root,
                              "compostables",
                              compostables,
                              this::getItemArgument,
                              ItemArgumentType::getItem,
                              PermittedFloatArgumentType.permitted(0.3f, 0.5f, 0.65f, 0.85f, 1.0f),
                              PermittedFloatArgumentType::getFloat);
    }
    
    private ArgumentType<Item> getItemArgument(SubCommand subCommand) {
        return switch (subCommand) {
            case GET, REMOVE -> ItemArgumentType.permitted(compostables.keySet());
            case ADD_SET -> ItemArgumentType.all();
        };
    }
    
    private int resetCompostables(CommandContext<ServerCommandSource> context) {
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.clear();
        ComposterBlock.registerDefaultCompostableItems();
        compostables.forEach(this::addCompostableitem);
        return 0;
    }
}
