package com.wexalian.mods.babblinmc.data;

import com.wexalian.mods.babblinmc.init.BabblinMCTags;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

public class BabblinMCItemTagDataProvider extends FabricTagProvider.ItemTagProvider {
    
    public BabblinMCItemTagDataProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
        super(dataGenerator, blockTagProvider);
    }
    
    @Override
    protected void generateTags() {
        copy(BabblinMCTags.Blocks.CONCRETE, BabblinMCTags.Items.CONCRETE);
        copy(BabblinMCTags.Blocks.CONCRETE_POWDER, BabblinMCTags.Items.CONCRETE_POWDER);
        copy(BabblinMCTags.Blocks.GLASS, BabblinMCTags.Items.GLASS);
        copy(BabblinMCTags.Blocks.GLASS_PANES, BabblinMCTags.Items.GLASS_PANES);
        copy(BabblinMCTags.Blocks.TERRACOTTA, BabblinMCTags.Items.TERRACOTTA);
        
        getOrCreateTagBuilder(BabblinMCTags.Items.BASE_STONE_OVERWORLD).add(Items.STONE, Items.ANDESITE, Items.GRANITE, Items.DIORITE);
    }
    
    @Override
    @Nonnull
    public String getName() {
        return "Babblin' Item Tags";
    }
}
