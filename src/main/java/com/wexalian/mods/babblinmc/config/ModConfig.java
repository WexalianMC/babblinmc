package com.wexalian.mods.babblinmc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wexalian.common.unchecked.Unchecked;
import com.wexalian.config.ConfigHandler;
import com.wexalian.mods.babblinmc.config.gson.ItemTypeAdapter;
import net.minecraft.item.Item;

import java.nio.file.Path;

public class ModConfig {
    private static final ModConfig INSTANCE = new ModConfig();
    
    private final Gson GSON = new GsonBuilder().serializeNulls()
                                               .setPrettyPrinting()
                                               .registerTypeAdapter(Item.class, ItemTypeAdapter.INSTANCE)
                                               .create();
    
    private final ConfigHandler config = ConfigHandler.create("babblinmc.json", GSON);
    
    
    public ConfigHandler getConfig() {
        return config;
    }
    
    public void load(Path path) {
        Unchecked.accept(path, config::load);
    }
    
    public void save() {
        Unchecked.run(config::save);
    }
    
    public static ModConfig instance() {
        return INSTANCE;
    }
}
