package com.wexalian.mods.babblinmc.feature;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.wexalian.common.util.StringUtil;
import com.wexalian.config.ConfigHandler;
import com.wexalian.config.ConfigProperty;
import com.wexalian.config.ListConfigProperty;
import com.wexalian.config.MapConfigProperty;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public abstract class ModFeature {
    private final String id;
    
    protected final ConfigProperty<Boolean> enabled;
    
    public ModFeature(@Nonnull String id, @Nonnull ConfigHandler config) {
        this.id = id;
        enabled = config.createBooleanProperty("enabled", false);
    }
    
    public void init() {}
    
    @Nonnull
    public final String getId() {
        return id;
    }
    
    public final boolean isEnabled() {
        return enabled.get();
    }
    
    public void registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> root) {
        registerBoolSubCommand(root, "enabled", enabled);
    }
    
    protected final void registerBoolSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<Boolean> property) {
        registerSubCommand(root, name, property, BoolArgumentType.bool(), BoolArgumentType::getBool);
    }
    
    protected final void registerDoubleSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<Double> property) {
        registerSubCommand(root, name, property, DoubleArgumentType.doubleArg(), DoubleArgumentType::getDouble);
    }
    
    protected final void registerFloatSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<Float> property) {
        registerSubCommand(root, name, property, FloatArgumentType.floatArg(), FloatArgumentType::getFloat);
    }
    
    protected final void registerIntSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<Integer> property) {
        registerSubCommand(root, name, property, IntegerArgumentType.integer(), IntegerArgumentType::getInteger);
    }
    
    protected final void registerLongSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<Long> property) {
        registerSubCommand(root, name, property, LongArgumentType.longArg(), LongArgumentType::getLong);
    }
    
    protected final void registerStringSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<String> property) {
        registerSubCommand(root, name, property, StringArgumentType.string(), StringArgumentType::getString);
    }
    
    protected final <T> void registerSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ConfigProperty<T> property, ArgumentType<T> type, BiFunction<CommandContext<ServerCommandSource>, String, T> valueFunction) {
        var valueArgument = argument(name + "Value", type);
        
        root.then(literal(name).then(valueArgument.executes(context -> {
            var value = valueFunction.apply(context, name + "Value");
            if (property.get() != value) {
                property.set(value);
                String text = StringUtil.format("Set {}/{} to {}", id, name, value);
                context.getSource().sendFeedback(Text.of(text), false);
                return 1;
            }
            return 0;
        })).executes(context -> {
            String text = StringUtil.format("{}/{} is {}", id, name, property.get());
            context.getSource().sendFeedback(Text.of(text), false);
            return 0;
        }));
    }
    
    protected final <T> void registerListSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ListConfigProperty<T> property, ArgumentType<T> type, BiFunction<CommandContext<ServerCommandSource>, String, T> valueFunction) {
        var valueArgument1 = argument(name + "Value1", type);
        var valueArgument2 = argument(name + "Value2", type);
        
        root.then(literal(name).then(literal("get").executes(context -> {
            String text = StringUtil.format("List {}/{} contains: ", id, name);
            context.getSource().sendFeedback(Text.of(text), false);
            for (T value : property.get()) {
                String valueText = StringUtil.format(" - {}", value);
                context.getSource().sendFeedback(Text.of(valueText), false);
            }
            return 0;
        })).then(literal("add").then(valueArgument1.executes(context -> {
            T value = valueFunction.apply(context, name + "Value1");
            if (value != null) {
                property.add(value);
                String text = StringUtil.format("Added {} to {}/{}", value, id, name);
                context.getSource().sendFeedback(Text.of(text), false);
                return 1;
            }
            return 0;
        }))).then(literal("remove").then(valueArgument2.executes(context -> {
            T value = valueFunction.apply(context, name + "Value2");
            if (value != null) {
                property.remove(value);
                String text = StringUtil.format("Removed {} from {}/{}", value, id, name);
                context.getSource().sendFeedback(Text.of(text), false);
                return 1;
            }
            return 0;
        }))).then(literal("clear").executes(context -> {
            property.clear();
            String text = StringUtil.format("Cleared {}/{}", id, name);
            context.getSource().sendFeedback(Text.of(text), false);
            return 1;
        })));
    }
    
    protected final <K, V> void registerMapSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, MapConfigProperty<K, V> property, Function<MapSubCommand, ArgumentType<K>> keyTypeFunction, BiFunction<CommandContext<ServerCommandSource>, String, K> keyFunction, ArgumentType<V> valueType, BiFunction<CommandContext<ServerCommandSource>, String, V> valueFunction) {
        var keyArgument1 = argument(name + "KeyGet", keyTypeFunction.apply(MapSubCommand.GET));
        var keyArgument2 = argument(name + "KeySet", keyTypeFunction.apply(MapSubCommand.SET));
        var keyArgument3 = argument(name + "KeyRemove", keyTypeFunction.apply(MapSubCommand.REMOVE));
        var valueArgument = argument(name + "Value", valueType);
        
        root.then(literal(name).then(literal("get").then(keyArgument1.executes(context -> {
            K key = keyFunction.apply(context, name + "KeyGet");
            if (key != null) {
                V value = property.get(key);
                if (value != null) {
                    String text = StringUtil.format("{} in {}/{} has value {}", key, id, name, value);
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 1;
                }
            }
            return 0;
        })).executes(context -> {
            String text = StringUtil.format("List {}/{} contains: ", id, name);
            context.getSource().sendFeedback(Text.of(text), false);
            for (var entry : property.entrySet()) {
                String text2 = StringUtil.format(" - {}: {}", entry.getKey(), entry.getValue());
                context.getSource().sendFeedback(Text.of(text2), false);
            }
            return 0;
        })).then(literal("set").then(keyArgument2.then(valueArgument.executes(context -> {
            K key = keyFunction.apply(context, name + "KeySet");
            V value = valueFunction.apply(context, name + "Value");
            
            if (key != null && value != null) {
                if (property.get(key) != value) {
                    V old = property.put(key, value);
                    
                    String text;
                    if (old != null) text = StringUtil.format("Changed {} in {}/{} from value {} to {}", key, id, name, old, value);
                    else text = StringUtil.format("Added {} to {}/{} with value {}", key, id, name, value);
                    
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 1;
                }
                else {
                    String text = StringUtil.format("{} already exists in {}/{} with value {}", key, id, name, value);
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 0;
                }
            }
            return -1;
        })))).then(literal("remove").then(keyArgument3.executes(context -> {
            K key = keyFunction.apply(context, name + "KeyRemove");
            if (key != null) {
                V value = property.remove(key);
                String text = StringUtil.format("Removed {} from {}/{} with value {}", key, id, name, value);
                context.getSource().sendFeedback(Text.of(text), false);
                return 1;
            }
            return 0;
        }))).then(literal("clear").executes(context -> {
            String text = StringUtil.format("Cleared {}/{}:", id, name);
            context.getSource().sendFeedback(Text.of(text), false);
            for (var entry : property.entrySet()) {
                String text2 = StringUtil.format(" - {}: {}", entry.getKey(), entry.getValue());
                context.getSource().sendFeedback(Text.of(text2), false);
            }
            property.clear();
            return 1;
        })));
    }
    
    public enum MapSubCommand {
        GET,
        SET,
        REMOVE;
    }
}
