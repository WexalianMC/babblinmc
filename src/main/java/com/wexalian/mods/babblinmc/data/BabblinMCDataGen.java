package com.wexalian.mods.babblinmc.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BabblinMCDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var blockStateProvider = new BabblinMCBlockStateDataProvider(generator);
        var blockTagProvider = new BabblinMCBlockTagDataProvider(generator);
        var blockLootTableProvider = new BabblinMCBlockLootTableDataProvider(generator);
        var itemTagProvider = new BabblinMCItemTagDataProvider(generator, blockTagProvider);
        var recipeProvider = new BabblinMCRecipeDataProvider(generator);
        
        generator.addProvider(blockStateProvider);
        generator.addProvider(blockTagProvider);
        generator.addProvider(blockLootTableProvider);
        generator.addProvider(itemTagProvider);
        generator.addProvider(recipeProvider);
    }
}
