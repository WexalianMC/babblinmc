package com.wexalian.mods.babblinmc.feature;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
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
    //@formatter:off
    private static final Dynamic3CommandExceptionType VALUE_ALREADY_EQUALS = new Dynamic3CommandExceptionType((id, name, value) -> new LiteralMessage(
        StringUtil.format("{}/{} already equals {}", id, name, value)));
    private static final Dynamic3CommandExceptionType LIST_VALUE_ALREADY_EXISTS = new Dynamic3CommandExceptionType((id, name,value) -> new LiteralMessage(
        StringUtil.format("{}/{} already contains {}", id, name, value)));
    private static final Dynamic4CommandExceptionType MAP_VALUE_ALREADY_EXISTS = new Dynamic4CommandExceptionType((id, name, key, value) -> new LiteralMessage(
        StringUtil.format("{}/{} already maps {} to {}", key, id, name, value)));
    private static final Dynamic2CommandExceptionType LIST_VALUE_DOESNT_EXIST = new Dynamic2CommandExceptionType((id, name) -> new LiteralMessage(
        StringUtil.format("Please specify a value for {}/{} ", id, name)));
    private static final Dynamic2CommandExceptionType MAP_KEY_DOESNT_EXIST = new Dynamic2CommandExceptionType((id, name) -> new LiteralMessage(
        StringUtil.format("Please specify a key for {}/{} ", id, name)));
    private static final Dynamic3CommandExceptionType LIST_VALUE_DOESNT_CONTAIN = new Dynamic3CommandExceptionType((id, name, value) -> new LiteralMessage(
        StringUtil.format("{}/{} does not contain {}", id, name, value)));
    private static final Dynamic3CommandExceptionType MAP_VALUE_DOESNT_CONTAIN = new Dynamic3CommandExceptionType((id, name, key) -> new LiteralMessage(
        StringUtil.format("{}/{} does not contain {}", id, name, key)));
    private static final Dynamic2CommandExceptionType PROPERTY_EMPTY = new Dynamic2CommandExceptionType((id, name) -> new LiteralMessage(
        StringUtil.format("{}/{} is already empty", id, name)));
    //@formatter:on
    
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
            throw VALUE_ALREADY_EQUALS.create(id, name, value);
        })).executes(context -> {
            String text = StringUtil.format("{}/{} is {}", id, name, property.get());
            context.getSource().sendFeedback(Text.of(text), false);
            return 0;
        }));
    }
    
    protected final <T> void registerListSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, ListConfigProperty<T> property, Function<SubCommand, ArgumentType<T>> typeFunction, BiFunction<CommandContext<ServerCommandSource>, String, T> valueFunction) {
        var valueArgumentAdd = argument(name + "ValueAdd", typeFunction.apply(SubCommand.ADD));
        var valueArgumentRemove = argument(name + "ValueRemove", typeFunction.apply(SubCommand.REMOVE));
        
        root.then(literal(name).then(literal("get").executes(context -> {
            String text = StringUtil.format("List {}/{} contains: ", id, name);
            context.getSource().sendFeedback(Text.of(text), false);
            for (T value : property.get()) {
                String valueText = StringUtil.format(" - {}", value);
                context.getSource().sendFeedback(Text.of(valueText), false);
            }
            return 0;
        })).then(literal("add").then(valueArgumentAdd.executes(context -> {
            T value = valueFunction.apply(context, name + "ValueAdd");
            if (value != null) {
                if (!property.contains(value)) {
                    property.add(value);
                    String text = StringUtil.format("Added {} to {}/{}", value, id, name);
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 1;
                }
                throw LIST_VALUE_ALREADY_EXISTS.create(id, name, value);
            }
            throw LIST_VALUE_DOESNT_EXIST.create(id, name);
        }))).then(literal("remove").then(valueArgumentRemove.executes(context -> {
            T value = valueFunction.apply(context, name + "ValueRemove");
            if (value != null) {
                if (property.contains(value)) {
                    if(property.remove(value)){
                        String text = StringUtil.format("Removed {} from {}/{}", value, id, name);
                        context.getSource().sendFeedback(Text.of(text), false);
                        return 1;
                    }
                }
                throw LIST_VALUE_DOESNT_CONTAIN.create(id, name, value);
            }
            throw LIST_VALUE_DOESNT_EXIST.create(id, name);
        }))).then(literal("clear").executes(context -> {
            if (property.size() > 0) {
                String text = StringUtil.format("Cleared {}/{}:", id, name);
                context.getSource().sendFeedback(Text.of(text), false);
                for (T value : property) {
                    String text2 = StringUtil.format(" - {}", value);
                    context.getSource().sendFeedback(Text.of(text2), false);
                }
                property.clear();
                return 1;
            }
            throw PROPERTY_EMPTY.create(id, name);
        })));
    }
    
    protected final <K, V> void registerMapSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, String name, MapConfigProperty<K, V> property, Function<SubCommand, ArgumentType<K>> keyTypeFunction, BiFunction<CommandContext<ServerCommandSource>, String, K> keyFunction, ArgumentType<V> valueType, BiFunction<CommandContext<ServerCommandSource>, String, V> valueFunction) {
        var keyArgumentGet = argument(name + "KeyGet", keyTypeFunction.apply(SubCommand.GET));
        var keyArgumentSet = argument(name + "KeyAdd", keyTypeFunction.apply(SubCommand.ADD));
        var keyArgumentRemove = argument(name + "KeyRemove", keyTypeFunction.apply(SubCommand.REMOVE));
        var valueArgument = argument(name + "Value", valueType);
        
        root.then(literal(name).then(literal("get").then(keyArgumentGet.executes(context -> {
            K key = keyFunction.apply(context, name + "KeyGet");
            if (key != null) {
                V value = property.get(key);
                if (value != null) {
                    String text = StringUtil.format("{} in {}/{} has value {}", key, id, name, value);
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 0;
                }
                throw MAP_VALUE_DOESNT_CONTAIN.create(id, name, key);
            }
            throw MAP_KEY_DOESNT_EXIST.create(id, name);
        })).executes(context -> {
            if (property.size() > 0) {
                String text = StringUtil.format("List {}/{} contains: ", id, name);
                context.getSource().sendFeedback(Text.of(text), false);
                for (var entry : property.entrySet()) {
                    String text2 = StringUtil.format(" - {}: {}", entry.getKey(), entry.getValue());
                    context.getSource().sendFeedback(Text.of(text2), false);
                }
                return 0;
            }
            throw PROPERTY_EMPTY.create(id, name);
        })).then(literal("add").then(keyArgumentSet.then(valueArgument.executes(context -> {
            K key = keyFunction.apply(context, name + "KeyAdd");
            V value = valueFunction.apply(context, name + "Value");
            
            if (key != null) {
                if (value != null) {
                    if (property.get(key) != value) {
                        V old = property.put(key, value);
                        
                        String text;
                        if (old != null) text = StringUtil.format("Changed {} in {}/{} from value {} to {}", key, id, name, old, value);
                        else text = StringUtil.format("Added {} to {}/{} with value {}", key, id, name, value);
                        
                        context.getSource().sendFeedback(Text.of(text), false);
                        return 1;
                    }
                    throw MAP_VALUE_ALREADY_EXISTS.create(id, name, key, value);
                }
                throw MAP_VALUE_DOESNT_CONTAIN.create(id, name, key);
            }
            throw MAP_KEY_DOESNT_EXIST.create(id, name);
        })))).then(literal("remove").then(keyArgumentRemove.executes(context -> {
            K key = keyFunction.apply(context, name + "KeyRemove");
            if (key != null) {
                V value = property.remove(key);
                if (value != null) {
                    String text = StringUtil.format("Removed {} from {}/{} with value {}", key, id, name, value);
                    context.getSource().sendFeedback(Text.of(text), false);
                    return 1;
                }
                throw MAP_VALUE_DOESNT_CONTAIN.create(id, name, key);
            }
            throw MAP_KEY_DOESNT_EXIST.create(id, name);
        }))).then(literal("clear").executes(context -> {
            if (property.size() > 0) {
                String text = StringUtil.format("Cleared {}/{}:", id, name);
                context.getSource().sendFeedback(Text.of(text), false);
                for (var entry : property.entrySet()) {
                    String text2 = StringUtil.format(" - {}: {}", entry.getKey(), entry.getValue());
                    context.getSource().sendFeedback(Text.of(text2), false);
                }
                property.clear();
                return 1;
            }
            throw PROPERTY_EMPTY.create(id, name);
        })));
    }
    
    public enum SubCommand {
        GET,
        ADD,
        REMOVE;
    }
}
