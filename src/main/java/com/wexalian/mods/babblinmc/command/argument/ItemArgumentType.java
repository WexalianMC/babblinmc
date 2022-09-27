package com.wexalian.mods.babblinmc.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.wexalian.nullability.annotations.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ItemArgumentType implements ArgumentType<Item> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick");
    
    @Nullable
    private final Collection<Item> permitted;
    @Nullable
    private final Collection<String> examples;
    
    private ItemArgumentType(@Nullable Collection<Item> permitted) {
        this.permitted = permitted;
        this.examples = permitted != null ? permitted.stream().map(Object::toString).toList() : null;
    }
    
    @Override
    public Item parse(StringReader reader) throws CommandSyntaxException {
        ItemStringReader itemReader = new ItemStringReader(reader, false).consume();
        return itemReader.getItem();
    }
    
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (permitted != null) {
            return CommandSource.suggestIdentifiers(permitted.stream().map(Registry.ITEM::getId), builder);
        }
        
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        
        ItemStringReader itemReader = new ItemStringReader(reader, false);
        try {
            itemReader.consume();
        }
        catch (CommandSyntaxException ignored) {}
        
        return itemReader.getSuggestions(builder, Registry.ITEM);
    }
    
    @Override
    public Collection<String> getExamples() {
        return examples != null ? examples : EXAMPLES;
    }
    
    public static <S> Item getItem(CommandContext<S> context, String name) {
        return context.getArgument(name, Item.class);
    }
    
    public static ItemArgumentType permitted(@Nullable Collection<Item> permitted) {
        return new ItemArgumentType(permitted);
    }
    
    public static ItemArgumentType all() {
        return permitted(null);
    }
}
