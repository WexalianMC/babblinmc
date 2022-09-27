package com.wexalian.mods.babblinmc.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PermittedFloatArgumentType implements ArgumentType<Float> {
    private static final Dynamic2CommandExceptionType FLOAT_NOT_PERMITTED = new Dynamic2CommandExceptionType((found, permitted) -> new LiteralMessage(
        "Float must be one of " + permitted + ", found " + found));
    
    private final List<Float> permitted;
    private final List<String> examples;
    
    private PermittedFloatArgumentType(List<Float> permitted) {
        this.permitted = permitted;
        this.examples = permitted.stream().map(Object::toString).toList();
    }
    
    public static PermittedFloatArgumentType permitted(Float... values) {
        return permitted(List.of(values));
    }
    
    public static PermittedFloatArgumentType permitted(List<Float> values) {
        return new PermittedFloatArgumentType(values);
    }
    
    public static float getFloat(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Float.class);
    }
    
    @Override
    public Float parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final float result = reader.readFloat();
        if (!permitted.contains(result)) {
            reader.setCursor(start);
            throw FLOAT_NOT_PERMITTED.createWithContext(reader, result, permitted);
        }
        return result;
    }
    
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        examples.forEach(value -> {
            if (value.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(value);
            }
        });
        return builder.buildFuture();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermittedFloatArgumentType that = (PermittedFloatArgumentType) o;
        return permitted.equals(that.permitted);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(permitted);
    }
    
    @Override
    public String toString() {
        return "float(" + permitted + ")";
    }
    
    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}