package com.wexalian.mods.babblinmc.data;

import com.wexalian.common.stream.BiStream;
import com.wexalian.common.util.collection.ListUtil;
import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import com.wexalian.mods.babblinmc.init.BabblinMCTags;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Consumer;

public class BabblinMCRecipeDataProvider extends FabricRecipeProvider {
    
    private static final List<Block> CONCRETE = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_CONCRETE);
        l.add(Blocks.ORANGE_CONCRETE);
        l.add(Blocks.MAGENTA_CONCRETE);
        l.add(Blocks.LIGHT_BLUE_CONCRETE);
        l.add(Blocks.YELLOW_CONCRETE);
        l.add(Blocks.LIME_CONCRETE);
        l.add(Blocks.PINK_CONCRETE);
        l.add(Blocks.GRAY_CONCRETE);
        l.add(Blocks.LIGHT_GRAY_CONCRETE);
        l.add(Blocks.CYAN_CONCRETE);
        l.add(Blocks.PURPLE_CONCRETE);
        l.add(Blocks.BLUE_CONCRETE);
        l.add(Blocks.BROWN_CONCRETE);
        l.add(Blocks.GREEN_CONCRETE);
        l.add(Blocks.RED_CONCRETE);
        l.add(Blocks.BLACK_CONCRETE);
    });
    private static final List<Block> CONCRETE_POWDER = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_CONCRETE_POWDER);
        l.add(Blocks.ORANGE_CONCRETE_POWDER);
        l.add(Blocks.MAGENTA_CONCRETE_POWDER);
        l.add(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        l.add(Blocks.YELLOW_CONCRETE_POWDER);
        l.add(Blocks.LIME_CONCRETE_POWDER);
        l.add(Blocks.PINK_CONCRETE_POWDER);
        l.add(Blocks.GRAY_CONCRETE_POWDER);
        l.add(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        l.add(Blocks.CYAN_CONCRETE_POWDER);
        l.add(Blocks.PURPLE_CONCRETE_POWDER);
        l.add(Blocks.BLUE_CONCRETE_POWDER);
        l.add(Blocks.BROWN_CONCRETE_POWDER);
        l.add(Blocks.GREEN_CONCRETE_POWDER);
        l.add(Blocks.RED_CONCRETE_POWDER);
        l.add(Blocks.BLACK_CONCRETE_POWDER);
    });
    private static final List<Block> STAINED_GLASS = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_STAINED_GLASS);
        l.add(Blocks.ORANGE_STAINED_GLASS);
        l.add(Blocks.MAGENTA_STAINED_GLASS);
        l.add(Blocks.LIGHT_BLUE_STAINED_GLASS);
        l.add(Blocks.YELLOW_STAINED_GLASS);
        l.add(Blocks.LIME_STAINED_GLASS);
        l.add(Blocks.PINK_STAINED_GLASS);
        l.add(Blocks.GRAY_STAINED_GLASS);
        l.add(Blocks.LIGHT_GRAY_STAINED_GLASS);
        l.add(Blocks.CYAN_STAINED_GLASS);
        l.add(Blocks.PURPLE_STAINED_GLASS);
        l.add(Blocks.BLUE_STAINED_GLASS);
        l.add(Blocks.BROWN_STAINED_GLASS);
        l.add(Blocks.GREEN_STAINED_GLASS);
        l.add(Blocks.RED_STAINED_GLASS);
        l.add(Blocks.BLACK_STAINED_GLASS);
    });
    private static final List<Block> STAINED_GLASS_PANE = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_STAINED_GLASS_PANE);
        l.add(Blocks.ORANGE_STAINED_GLASS_PANE);
        l.add(Blocks.MAGENTA_STAINED_GLASS_PANE);
        l.add(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        l.add(Blocks.YELLOW_STAINED_GLASS_PANE);
        l.add(Blocks.LIME_STAINED_GLASS_PANE);
        l.add(Blocks.PINK_STAINED_GLASS_PANE);
        l.add(Blocks.GRAY_STAINED_GLASS_PANE);
        l.add(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        l.add(Blocks.CYAN_STAINED_GLASS_PANE);
        l.add(Blocks.PURPLE_STAINED_GLASS_PANE);
        l.add(Blocks.BLUE_STAINED_GLASS_PANE);
        l.add(Blocks.BROWN_STAINED_GLASS_PANE);
        l.add(Blocks.GREEN_STAINED_GLASS_PANE);
        l.add(Blocks.RED_STAINED_GLASS_PANE);
        l.add(Blocks.BLACK_STAINED_GLASS_PANE);
    });
    private static final List<Block> TERRACOTTA = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_TERRACOTTA);
        l.add(Blocks.ORANGE_TERRACOTTA);
        l.add(Blocks.MAGENTA_TERRACOTTA);
        l.add(Blocks.LIGHT_BLUE_TERRACOTTA);
        l.add(Blocks.YELLOW_TERRACOTTA);
        l.add(Blocks.LIME_TERRACOTTA);
        l.add(Blocks.PINK_TERRACOTTA);
        l.add(Blocks.GRAY_TERRACOTTA);
        l.add(Blocks.LIGHT_GRAY_TERRACOTTA);
        l.add(Blocks.CYAN_TERRACOTTA);
        l.add(Blocks.PURPLE_TERRACOTTA);
        l.add(Blocks.BLUE_TERRACOTTA);
        l.add(Blocks.BROWN_TERRACOTTA);
        l.add(Blocks.GREEN_TERRACOTTA);
        l.add(Blocks.RED_TERRACOTTA);
        l.add(Blocks.BLACK_TERRACOTTA);
    });
    private static final List<Block> GLAZED_TERRACOTTA = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_GLAZED_TERRACOTTA);
        l.add(Blocks.ORANGE_GLAZED_TERRACOTTA);
        l.add(Blocks.MAGENTA_GLAZED_TERRACOTTA);
        l.add(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        l.add(Blocks.YELLOW_GLAZED_TERRACOTTA);
        l.add(Blocks.LIME_GLAZED_TERRACOTTA);
        l.add(Blocks.PINK_GLAZED_TERRACOTTA);
        l.add(Blocks.GRAY_GLAZED_TERRACOTTA);
        l.add(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
        l.add(Blocks.CYAN_GLAZED_TERRACOTTA);
        l.add(Blocks.PURPLE_GLAZED_TERRACOTTA);
        l.add(Blocks.BLUE_GLAZED_TERRACOTTA);
        l.add(Blocks.BROWN_GLAZED_TERRACOTTA);
        l.add(Blocks.GREEN_GLAZED_TERRACOTTA);
        l.add(Blocks.RED_GLAZED_TERRACOTTA);
        l.add(Blocks.BLACK_GLAZED_TERRACOTTA);
    });
    private static final List<Block> WOOL = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_WOOL);
        l.add(Blocks.ORANGE_WOOL);
        l.add(Blocks.MAGENTA_WOOL);
        l.add(Blocks.LIGHT_BLUE_WOOL);
        l.add(Blocks.YELLOW_WOOL);
        l.add(Blocks.LIME_WOOL);
        l.add(Blocks.PINK_WOOL);
        l.add(Blocks.GRAY_WOOL);
        l.add(Blocks.LIGHT_GRAY_WOOL);
        l.add(Blocks.CYAN_WOOL);
        l.add(Blocks.PURPLE_WOOL);
        l.add(Blocks.BLUE_WOOL);
        l.add(Blocks.BROWN_WOOL);
        l.add(Blocks.GREEN_WOOL);
        l.add(Blocks.RED_WOOL);
        l.add(Blocks.BLACK_WOOL);
    });
    private static final List<Block> CARPET = ListUtil.newArrayList().fill(l -> {
        l.add(Blocks.WHITE_CARPET);
        l.add(Blocks.ORANGE_CARPET);
        l.add(Blocks.MAGENTA_CARPET);
        l.add(Blocks.LIGHT_BLUE_CARPET);
        l.add(Blocks.YELLOW_CARPET);
        l.add(Blocks.LIME_CARPET);
        l.add(Blocks.PINK_CARPET);
        l.add(Blocks.GRAY_CARPET);
        l.add(Blocks.LIGHT_GRAY_CARPET);
        l.add(Blocks.CYAN_CARPET);
        l.add(Blocks.PURPLE_CARPET);
        l.add(Blocks.BLUE_CARPET);
        l.add(Blocks.BROWN_CARPET);
        l.add(Blocks.GREEN_CARPET);
        l.add(Blocks.RED_CARPET);
        l.add(Blocks.BLACK_CARPET);
    });
    private static final List<Item> DYE = ListUtil.newArrayList().fill(l -> {
        l.add(Items.WHITE_DYE);
        l.add(Items.ORANGE_DYE);
        l.add(Items.MAGENTA_DYE);
        l.add(Items.LIGHT_BLUE_DYE);
        l.add(Items.YELLOW_DYE);
        l.add(Items.LIME_DYE);
        l.add(Items.PINK_DYE);
        l.add(Items.GRAY_DYE);
        l.add(Items.LIGHT_GRAY_DYE);
        l.add(Items.CYAN_DYE);
        l.add(Items.PURPLE_DYE);
        l.add(Items.BLUE_DYE);
        l.add(Items.BROWN_DYE);
        l.add(Items.GREEN_DYE);
        l.add(Items.RED_DYE);
        l.add(Items.BLACK_DYE);
    });
    
    public BabblinMCRecipeDataProvider(@Nonnull FabricDataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected void generateRecipes(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        //replace vanilla
        replaceVanillaChestRecipe(consumer);
        replaceVanillaDispenserRecipe(consumer);
        replaceRedstoneTorchRecipe(consumer);
        
        // 2x2
        add2x2BreadRecipe(consumer);
        add2x2PaperRecipe(consumer);
        add2x2ShulkerBoxRecipe(consumer);
        
        //smelting
        addRottenFleshToLeatherRecipe(consumer);
        addConcretePowderToGlassRecipe(consumer);
        addAllStoneToSmooth(consumer);
        
        //custom crafting
        addDirtRecipe(consumer);
        addGrassBlockRecipe(consumer);
        addGrassToDirtRecipe(consumer);
        addNameTagRecipe(consumer);
        addEnchantedGoldenAppleRecipe(consumer);
        addHorseArmorRecipes(consumer);
        addGravelRecipe(consumer);
        addChestRecipe(consumer);
        addDropperToDispenserRecipe(consumer);
        addAlternativeDispenserRecipe(consumer);
        addDeepslateToCobbledRecipe(consumer);
        addBuddingAmethystRecipe(consumer);
        addClayBallRecipe(consumer);
        addLightRecipe(consumer);
        
        //dyeing
        addConcreteDyeingRecipe(consumer);
        addConcretePowderDyeingRecipe(consumer);
        addGlassDyeingRecipe(consumer);
        addGlassPaneDyeingRecipe(consumer);
        addTerracottaDyeingRecipe(consumer);
        addWoolDyeingRecipe(consumer);
        
        //uncrafting
        addBrickUncraftingRecipe(consumer);
        addGravelUncrafting(consumer);
        addGlowstoneUncraftingRecipe(consumer);
        addNetherBrickUncraftingRecipe(consumer);
        addPrismarineUncraftingRecipe(consumer);
        addQuartzUncraftingRecipe(consumer);
        addPackedIceUncraftingRecipe(consumer);
        addBlueIceUncraftingRecipe(consumer);
        addNetherWartUncraftingRecipe(consumer);
        addStringUncraftingRecipe(consumer);
        addGlassPaneUncraftingRecipes(consumer);
        addCarpetUncraftingRecipes(consumer);
        addGlazedTerracottaUncraftingRecipes(consumer);
        
        //custom blocks
        addPumpRecipe(consumer);
        addEnderTankRecipe(consumer);
    }
    
    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void replaceVanillaChestRecipe(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.CHEST)
                               .pattern("###")
                               .pattern("# #")
                               .pattern("###")
                               .input('#', ItemTags.PLANKS)
                               .criterion("has_lots_of_items",
                                          new InventoryChangedCriterion.Conditions(EntityPredicate.Extended.EMPTY,
                                                                                   NumberRange.IntRange.atLeast(10),
                                                                                   NumberRange.IntRange.ANY,
                                                                                   NumberRange.IntRange.ANY,
                                                                                   new ItemPredicate[0]))
                               .group("chest")
                               .offerTo(consumer);
    }
    
    private void replaceVanillaDispenserRecipe(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.DISPENSER)
                               .pattern("###")
                               .pattern("#X#")
                               .pattern("#R#")
                               .input('R', Items.REDSTONE)
                               .input('#', Blocks.COBBLESTONE)
                               .input('X', Items.BOW)
                               .criterion("has_bow", conditionsFromItem(Items.BOW))
                               .group("dispenser")
                               .offerTo(consumer);
        
    }
    
    private void replaceRedstoneTorchRecipe(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Items.REDSTONE_TORCH, 4)
                               .pattern("R")
                               .pattern("S")
                               .input('R', Items.REDSTONE)
                               .input('S', Items.STICK)
                               .criterion("has_bow", conditionsFromItem(Items.BOW))
                               .offerTo(consumer);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void add2x2BreadRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.BREAD, 1)
                                  .input(Items.WHEAT, 3)
                                  .criterion("has_wheat", conditionsFromItem(Items.WHEAT))
                                  .group("minecraft:wheat")
                                  .offerTo(consumer);
    }
    
    private void add2x2PaperRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.PAPER, 3)
                                  .input(Items.SUGAR_CANE, 3)
                                  .criterion("has_sugar_cane", conditionsFromItem(Items.SUGAR_CANE))
                                  .offerTo(consumer);
    }
    
    private void add2x2ShulkerBoxRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.SHULKER_BOX, 1)
                                  .input(Items.SHULKER_SHELL, 1)
                                  .input(Blocks.CHEST, 1)
                                  .input(Items.SHULKER_SHELL, 1)
                                  .criterion("has_shulker_shell", conditionsFromItem(Items.SHULKER_SHELL))
                                  .offerTo(consumer);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addRottenFleshToLeatherRecipe(Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(Items.ROTTEN_FLESH), Items.LEATHER, 1.0F, 200)
                                .criterion("has_rotten_flesh", conditionsFromItem(Items.ROTTEN_FLESH))
                                .offerTo(consumer, BabblinMC.MOD_ID + ":rotten_flesh_to_leather");
    }
    
    private void addConcretePowderToGlassRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(CONCRETE_POWDER, STAINED_GLASS).forEach((powder, glass) -> {
            String concreteName = Registry.BLOCK.getId(powder).getPath();
            String glassName = Registry.BLOCK.getId(glass).getPath();
            
            CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(powder), glass, 0.3F, 200)
                                    .criterion("has_" + concreteName, conditionsFromItem(powder))
                                    .offerTo(consumer,
                                             BabblinMC.MOD_ID + ":concrete_powder_to_stained_glass/" + concreteName + "_to_" + glassName);
        });
    }
    
    private void addAllStoneToSmooth(Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(Ingredient.fromTag(BabblinMCTags.Items.BASE_STONE_OVERWORLD),
                                                Blocks.SMOOTH_STONE,
                                                0.1F,
                                                200)
                                .criterion("has_stone", conditionsFromTag(BabblinMCTags.Items.BASE_STONE_OVERWORLD))
                                .offerTo(consumer);
        
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addDirtRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.DIRT, 1)
                                  .input(Blocks.COARSE_DIRT, 1)
                                  .criterion("has_coarse_dirt", conditionsFromItem(Blocks.COARSE_DIRT))
                                  .group(BabblinMC.MOD_ID + ":dirt")
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":coarse_dirt_to_dirt");
    }
    
    private void addGrassBlockRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.GRASS_BLOCK, 1)
                                  .input(Blocks.DIRT, 1)
                                  .input(Items.GRASS, 1)
                                  .criterion("has_grass_block", conditionsFromItem(Blocks.GRASS_BLOCK))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":grass_block");
    }
    
    private void addGrassToDirtRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.DIRT, 1)
                                  .input(Blocks.GRASS_BLOCK, 1)
                                  .criterion("has_grass_block", conditionsFromItem(Blocks.GRASS_BLOCK))
                                  .group(BabblinMC.MOD_ID + ":dirt")
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":grass_to_dirt");
        
    }
    
    private void addNameTagRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Items.NAME_TAG, 1)
                               .pattern(" IS")
                               .pattern(" PI")
                               .pattern("P  ")
                               .input('I', Items.IRON_INGOT)
                               .input('P', Items.PAPER)
                               .input('S', Items.STRING)
                               .criterion("has_name_tag", conditionsFromItem(Items.NAME_TAG))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":name_tag");
    }
    
    private void addEnchantedGoldenAppleRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Items.ENCHANTED_GOLDEN_APPLE, 1)
                               .pattern("XXX")
                               .pattern("X#X")
                               .pattern("XXX")
                               .input('X', Blocks.GOLD_BLOCK)
                               .input('#', Items.APPLE)
                               .criterion("has_golden_apple", conditionsFromItem(Items.GOLDEN_APPLE))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":enchanted_golden_apple");
    }
    
    private void addHorseArmorRecipes(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Items.DIAMOND_HORSE_ARMOR, 1)
                               .pattern("  #")
                               .pattern("###")
                               .pattern("#X#")
                               .input('#', Items.DIAMOND)
                               .input('X', Items.SADDLE)
                               .criterion("has_saddle", conditionsFromItem(Items.SADDLE))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":diamond_horse_armor");
        
        ShapedRecipeJsonBuilder.create(Items.GOLDEN_HORSE_ARMOR, 1)
                               .pattern("  #")
                               .pattern("###")
                               .pattern("#X#")
                               .input('#', Items.GOLD_INGOT)
                               .input('X', Items.SADDLE)
                               .criterion("has_saddle", conditionsFromItem(Items.SADDLE))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":golden_horse_armor");
        
        ShapedRecipeJsonBuilder.create(Items.IRON_HORSE_ARMOR, 1)
                               .pattern("  #")
                               .pattern("###")
                               .pattern("#X#")
                               .input('#', Items.IRON_INGOT)
                               .input('X', Items.SADDLE)
                               .criterion("has_saddle", conditionsFromItem(Items.SADDLE))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":iron_horse_armor");
    }
    
    private void addGravelRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.GRAVEL, 1)
                               .pattern("##")
                               .pattern("##")
                               .input('#', Items.FLINT)
                               .criterion("has_flint", conditionsFromItem(Items.FLINT))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":flint_to_gravel");
    }
    
    private void addChestRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.CHEST, 4)
                               .pattern("###")
                               .pattern("# #")
                               .pattern("###")
                               .input('#', ItemTags.LOGS)
                               .criterion("has_chest", conditionsFromItem(Items.CHEST))
                               .group("chest")
                               .offerTo(consumer, BabblinMC.MOD_ID + ":easier_chest");
    }
    
    private void addDropperToDispenserRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.DISPENSER, 1)
                                  .input(Items.BOW, 1)
                                  .input(Items.DROPPER, 1)
                                  .criterion("has_dropper", conditionsFromItem(Items.DROPPER))
                                  .group("dispenser")
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":dropper_to_dispenser");
    }
    
    private void addAlternativeDispenserRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.DISPENSER, 1)
                               .pattern(" SX")
                               .pattern("SDX")
                               .pattern(" SX")
                               .input('S', Items.STICK)
                               .input('X', Items.STRING)
                               .input('D', Blocks.DROPPER)
                               .criterion("has_dropper", conditionsFromItem(Blocks.DROPPER))
                               .group("dispenser")
                               .offerTo(consumer, BabblinMC.MOD_ID + ":alternative_dispenser");
    }
    
    private void addDeepslateToCobbledRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.COBBLED_DEEPSLATE, 1)
                                  .input(Items.DEEPSLATE, 1)
                                  .criterion("has_deepslate", conditionsFromItem(Blocks.DEEPSLATE))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":cobbled_deepslate");
    }
    
    private void addBuddingAmethystRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.BUDDING_AMETHYST, 1)
                               .pattern("###")
                               .pattern("#X#")
                               .pattern("###")
                               .input('#', Items.AMETHYST_SHARD)
                               .input('X', Items.NETHER_STAR)
                               .criterion("has_nether_star", conditionsFromItem(Items.NETHER_STAR))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":budding_amethyst");
    }
    
    private void addLightRecipe(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(Blocks.LIGHT, 16)
                               .pattern("XXX")
                               .pattern("XGX")
                               .pattern("III")
                               .input('X', Blocks.GLASS)
                               .input('G', Items.GLOWSTONE_DUST)
                               .input('I', Items.IRON_NUGGET)
                               .criterion("has_glowstone_dust", conditionsFromItem(Items.GLOWSTONE_DUST))
                               .offerTo(consumer, BabblinMC.MOD_ID + ":light");
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addConcreteDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(CONCRETE, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', BabblinMCTags.Items.CONCRETE)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("redyeing_concrete")
                                   .offerTo(consumer, BabblinMC.MOD_ID + ":dyeing/concrete/" + name);
        });
    }
    
    private void addConcretePowderDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(CONCRETE_POWDER, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', BabblinMCTags.Items.CONCRETE_POWDER)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("concrete_powder")
                                   .offerTo(consumer, BabblinMC.MOD_ID + ":dyeing/concrete_powder/" + name);
        });
    }
    
    private void addGlassDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(STAINED_GLASS, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', BabblinMCTags.Items.GLASS)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("stained_glass")
                                   .offerTo(consumer);
        });
    }
    
    private void addGlassPaneDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(STAINED_GLASS_PANE, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', BabblinMCTags.Items.GLASS_PANES)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("stained_glass_pane")
                                   .offerTo(consumer, name + "_from_glass_pane");
        });
    }
    
    private void addTerracottaDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(TERRACOTTA, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', BabblinMCTags.Items.TERRACOTTA)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("stained_terracotta")
                                   .offerTo(consumer);
        });
    }
    
    private void addWoolDyeingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(WOOL, DYE).forEach((block, item) -> {
            String name = Registry.BLOCK.getId(block).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 8)
                                   .pattern("###")
                                   .pattern("#X#")
                                   .pattern("###")
                                   .input('#', ItemTags.WOOL)
                                   .input('X', item)
                                   .criterion("has_" + name, conditionsFromItem(block))
                                   .group("wool")
                                   .offerTo(consumer);
        });
    }
    
    private void addBrickUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.BRICK, 4)
                                  .input(Blocks.BRICKS)
                                  .criterion("has_bricks", conditionsFromItem(Blocks.BRICKS))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/bricks_from_bricks");
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addGravelUncrafting(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.FLINT, 4)
                                  .input(Blocks.GRAVEL)
                                  .criterion("has_gravel", conditionsFromItem(Blocks.GRAVEL))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/flint_from_gravel");
    }
    
    private void addGlowstoneUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.GLOWSTONE_DUST, 4)
                                  .input(Blocks.GLOWSTONE)
                                  .criterion("has_glowstone", conditionsFromItem(Blocks.GLOWSTONE))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/glowstone_dust_from_glowstone");
    }
    
    private void addNetherBrickUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.NETHER_BRICK, 4)
                                  .input(Blocks.NETHER_BRICKS)
                                  .criterion("has_nether_bricks", conditionsFromItem(Blocks.NETHER_BRICKS))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/nether_bricks_from_nether_bricks");
    }
    
    private void addPrismarineUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.PRISMARINE_SHARD, 9)
                                  .input(Blocks.PRISMARINE_BRICKS)
                                  .criterion("has_prismarine_bricks", conditionsFromItem(Blocks.PRISMARINE_BRICKS))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/prismarine_shards_from_prismarine_bricks");
    }
    
    private void addQuartzUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.QUARTZ, 4)
                                  .input(Blocks.QUARTZ_BLOCK)
                                  .criterion("has_quartz", conditionsFromItem(Blocks.QUARTZ_BLOCK))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/quartz_from_quartz_block");
    }
    
    private void addPackedIceUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.ICE, 9)
                                  .input(Blocks.PACKED_ICE)
                                  .criterion("has_packed_ice", conditionsFromItem(Blocks.PACKED_ICE))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/ice_from_packed_ice");
    }
    
    private void addBlueIceUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Blocks.PACKED_ICE, 9)
                                  .input(Blocks.BLUE_ICE)
                                  .criterion("has_blue_ice", conditionsFromItem(Blocks.BLUE_ICE))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/packed_ice_from_blue_ice");
    }
    
    private void addNetherWartUncraftingRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.NETHER_WART, 9)
                                  .input(Blocks.NETHER_WART_BLOCK)
                                  .criterion("has_nether_wart_block", conditionsFromItem(Blocks.NETHER_WART_BLOCK))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/nether_wart_from_nether_wart_block");
    }
    
    private void addStringUncraftingRecipe(Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.STRING, 4)
                                  .input(ItemTags.WOOL)
                                  .criterion("has_string", conditionsFromItem(Items.STRING))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":uncrafting/string_from_wool");
        
    }
    
    private void addGlassPaneUncraftingRecipes(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(STAINED_GLASS, STAINED_GLASS_PANE).put(Blocks.GLASS, Blocks.GLASS_PANE).forEach((block, pane) -> {
            String blockName = Registry.BLOCK.getId(block).getPath();
            String paneName = Registry.BLOCK.getId(pane).getPath();
            
            ShapedRecipeJsonBuilder.create(block, 3)
                                   .pattern("XXX")
                                   .pattern("X X")
                                   .pattern("XXX")
                                   .input('X', pane)
                                   .criterion("has_" + paneName, conditionsFromItem(pane))
                                   .group("uncrafting_glass")
                                   .offerTo(consumer,
                                            BabblinMC.MOD_ID + ":uncrafting/glass_from_glass_panes/" + blockName + "_from_" + paneName);
        });
    }
    
    private void addCarpetUncraftingRecipes(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(WOOL, CARPET).forEach((wool, carpet) -> {
            String blockName = Registry.BLOCK.getId(wool).getPath();
            String carpetName = Registry.BLOCK.getId(carpet).getPath();
            
            ShapedRecipeJsonBuilder.create(wool, 2)
                                   .pattern("XX")
                                   .pattern("X ")
                                   .input('X', carpet)
                                   .criterion("has_" + carpetName, conditionsFromItem(carpet))
                                   .group("uncrafting_carpet")
                                   .offerTo(consumer,
                                            BabblinMC.MOD_ID + ":uncrafting/wool_from_carpet/" + blockName + "_from_" + carpetName);
        });
    }
    
    private void addGlazedTerracottaUncraftingRecipes(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(TERRACOTTA, GLAZED_TERRACOTTA).forEach((terracotta, glazed) -> {
            String terracottaName = Registry.BLOCK.getId(terracotta).getPath();
            String glazedName = Registry.BLOCK.getId(glazed).getPath();
            
            ShapelessRecipeJsonBuilder.create(terracotta, 1)
                                      .input(glazed)
                                      .criterion("has_" + glazedName, conditionsFromItem(glazed))
                                      .group("uncrafting_terracotta")
                                      .offerTo(consumer,
                                               BabblinMC.MOD_ID + ":uncrafting/terracotta_from_glazed_terracotta/" + terracottaName + "_from_" + glazedName);
        });
    }
    
    private void addClayBallRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(Items.CLAY_BALL, 4)
                                  .input(Blocks.CLAY)
                                  .criterion("has_clay", conditionsFromItem(Blocks.CLAY))
                                  .offerTo(consumer, BabblinMC.MOD_ID + ":clay_ball");
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addPumpRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(BabblinMCBlocks.PUMP, 1)
                               .pattern("OPO")
                               .pattern("LDW")
                               .pattern("OPO")
                               .input('O', Blocks.OBSIDIAN)
                               .input('P', Items.IRON_PICKAXE)
                               .input('D', Items.DIAMOND_BLOCK)
                               .input('L', Items.LAVA_BUCKET)
                               .input('W', Items.WATER_BUCKET)
                               .criterion("has_obsidian", conditionsFromItem(Blocks.OBSIDIAN))
                               .offerTo(consumer);
    }
    private void addEnderTankRecipe(@Nonnull Consumer<RecipeJsonProvider> consumer) {
        BiStream.of(DyeColor.values(), WOOL).forEach((dye, wool) -> {
            ShapedRecipeJsonBuilder.create(BabblinMCBlocks.ENDER_TANK, 1)
                                   .pattern("BWB")
                                   .pattern("ODO")
                                   .pattern("BEB")
                                   .input('B', Items.BLAZE_ROD)
                                   .input('W', wool)
                                   .input('O', Blocks.OBSIDIAN)
                                   .input('D', Items.BUCKET)
                                   .input('E', Items.ENDER_PEARL)
                                   .criterion("has_blaze_rod", conditionsFromItem(Items.BLAZE_ROD))
                                   .offerTo(consumer, BabblinMC.MOD_ID + ":ender_tank/ender_tank_" + dye.getName());
        });
    }
    
    @Override
    @Nonnull
    public String getName() {
        return BabblinMC.MOD_ID + "' Recipes";
    }
    
}
