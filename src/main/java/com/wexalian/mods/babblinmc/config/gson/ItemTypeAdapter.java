package com.wexalian.mods.babblinmc.config.gson;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class ItemTypeAdapter implements JsonDeserializer<Item>, JsonSerializer<Item> {
    public static final ItemTypeAdapter INSTANCE = new ItemTypeAdapter();
    
    @Override
    public Item deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String id = json.getAsString();
        if (!id.isBlank()) {
            return Registry.ITEM.get(new Identifier(id));
        }
        return null;
    }
    
    @Override
    public JsonElement serialize(Item item, Type type, JsonSerializationContext context) {
        if (item == null || item == Items.AIR) {
            return new JsonPrimitive("");
        }
        Identifier id = Registry.ITEM.getId(item);
        return new JsonPrimitive(id.toString());
    }
}
