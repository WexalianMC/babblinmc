package com.wexalian.mods.babblinmc.data;

import com.wexalian.mods.babblinmc.init.BabblinMCTags;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;

public class BabblinMCBlockTagDataProvider extends FabricTagProvider.BlockTagProvider {
    public BabblinMCBlockTagDataProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }
    
    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(BabblinMCTags.Blocks.CONCRETE).add(Blocks.WHITE_CONCRETE,
                                                                 Blocks.ORANGE_CONCRETE,
                                                                 Blocks.MAGENTA_CONCRETE,
                                                                 Blocks.LIGHT_BLUE_CONCRETE,
                                                                 Blocks.YELLOW_CONCRETE,
                                                                 Blocks.LIME_CONCRETE,
                                                                 Blocks.PINK_CONCRETE,
                                                                 Blocks.GRAY_CONCRETE,
                                                                 Blocks.LIGHT_GRAY_CONCRETE,
                                                                 Blocks.CYAN_CONCRETE,
                                                                 Blocks.PURPLE_CONCRETE,
                                                                 Blocks.BLUE_CONCRETE,
                                                                 Blocks.BROWN_CONCRETE,
                                                                 Blocks.GREEN_CONCRETE,
                                                                 Blocks.RED_CONCRETE,
                                                                 Blocks.BLACK_CONCRETE);
        getOrCreateTagBuilder(BabblinMCTags.Blocks.CONCRETE_POWDER).add(Blocks.WHITE_CONCRETE_POWDER,
                                                                        Blocks.ORANGE_CONCRETE_POWDER,
                                                                        Blocks.MAGENTA_CONCRETE_POWDER,
                                                                        Blocks.LIGHT_BLUE_CONCRETE_POWDER,
                                                                        Blocks.YELLOW_CONCRETE_POWDER,
                                                                        Blocks.LIME_CONCRETE_POWDER,
                                                                        Blocks.PINK_CONCRETE_POWDER,
                                                                        Blocks.GRAY_CONCRETE_POWDER,
                                                                        Blocks.LIGHT_GRAY_CONCRETE_POWDER,
                                                                        Blocks.CYAN_CONCRETE_POWDER,
                                                                        Blocks.PURPLE_CONCRETE_POWDER,
                                                                        Blocks.BLUE_CONCRETE_POWDER,
                                                                        Blocks.BROWN_CONCRETE_POWDER,
                                                                        Blocks.GREEN_CONCRETE_POWDER,
                                                                        Blocks.RED_CONCRETE_POWDER,
                                                                        Blocks.BLACK_CONCRETE_POWDER);
        getOrCreateTagBuilder(BabblinMCTags.Blocks.GLASS).add(Blocks.GLASS,
                                                              Blocks.WHITE_STAINED_GLASS,
                                                              Blocks.ORANGE_STAINED_GLASS,
                                                              Blocks.MAGENTA_STAINED_GLASS,
                                                              Blocks.LIGHT_BLUE_STAINED_GLASS,
                                                              Blocks.YELLOW_STAINED_GLASS,
                                                              Blocks.LIME_STAINED_GLASS,
                                                              Blocks.PINK_STAINED_GLASS,
                                                              Blocks.GRAY_STAINED_GLASS,
                                                              Blocks.LIGHT_GRAY_STAINED_GLASS,
                                                              Blocks.CYAN_STAINED_GLASS,
                                                              Blocks.PURPLE_STAINED_GLASS,
                                                              Blocks.BLUE_STAINED_GLASS,
                                                              Blocks.BROWN_STAINED_GLASS,
                                                              Blocks.GREEN_STAINED_GLASS,
                                                              Blocks.RED_STAINED_GLASS,
                                                              Blocks.BLACK_STAINED_GLASS);
        getOrCreateTagBuilder(BabblinMCTags.Blocks.GLASS_PANES).add(Blocks.GLASS_PANE,
                                                                    Blocks.WHITE_STAINED_GLASS_PANE,
                                                                    Blocks.ORANGE_STAINED_GLASS_PANE,
                                                                    Blocks.MAGENTA_STAINED_GLASS_PANE,
                                                                    Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
                                                                    Blocks.YELLOW_STAINED_GLASS_PANE,
                                                                    Blocks.LIME_STAINED_GLASS_PANE,
                                                                    Blocks.PINK_STAINED_GLASS_PANE,
                                                                    Blocks.GRAY_STAINED_GLASS_PANE,
                                                                    Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
                                                                    Blocks.CYAN_STAINED_GLASS_PANE,
                                                                    Blocks.PURPLE_STAINED_GLASS_PANE,
                                                                    Blocks.BLUE_STAINED_GLASS_PANE,
                                                                    Blocks.BROWN_STAINED_GLASS_PANE,
                                                                    Blocks.GREEN_STAINED_GLASS_PANE,
                                                                    Blocks.RED_STAINED_GLASS_PANE,
                                                                    Blocks.BLACK_STAINED_GLASS_PANE);
        getOrCreateTagBuilder(BabblinMCTags.Blocks.TERRACOTTA).add(Blocks.TERRACOTTA,
                                                                   Blocks.WHITE_TERRACOTTA,
                                                                   Blocks.ORANGE_TERRACOTTA,
                                                                   Blocks.MAGENTA_TERRACOTTA,
                                                                   Blocks.LIGHT_BLUE_TERRACOTTA,
                                                                   Blocks.YELLOW_TERRACOTTA,
                                                                   Blocks.LIME_TERRACOTTA,
                                                                   Blocks.PINK_TERRACOTTA,
                                                                   Blocks.GRAY_TERRACOTTA,
                                                                   Blocks.LIGHT_GRAY_TERRACOTTA,
                                                                   Blocks.CYAN_TERRACOTTA,
                                                                   Blocks.PURPLE_TERRACOTTA,
                                                                   Blocks.BLUE_TERRACOTTA,
                                                                   Blocks.BROWN_TERRACOTTA,
                                                                   Blocks.GREEN_TERRACOTTA,
                                                                   Blocks.RED_TERRACOTTA,
                                                                   Blocks.BLACK_TERRACOTTA);
        
        getOrCreateTagBuilder(BlockTags.MOSS_REPLACEABLE).add(Blocks.COBBLESTONE);
        
        // getOrCreateBuilder(BlockTags.FENCES).addTag(BlockTags.STAIRS);
        // getOrCreateBuilder(BlockTags.WOODEN_FENCES).addTag(BlockTags.STAIRS);
    }
    
    @Override
    @Nonnull
    public String getName() {
        return "Babblin' Block Tags";
    }
}
