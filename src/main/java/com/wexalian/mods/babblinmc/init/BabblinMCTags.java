package com.wexalian.mods.babblinmc.init;

import com.wexalian.mods.babblinmc.BabblinMC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BabblinMCTags {
    public static class Blocks {
        public static final TagKey<Block> CONCRETE = tag("concrete");
        public static final TagKey<Block> CONCRETE_POWDER = tag("concrete_powder");
        public static final TagKey<Block> GLASS = tag("glass");
        public static final TagKey<Block> GLASS_PANES = tag("glass_panes");
        public static final TagKey<Block> TERRACOTTA = tag("terracotta");
        
        private static TagKey<Block> tag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(BabblinMC.MOD_ID, name));
        }
    }
    
    public static class Items {
        public static final TagKey<Item> CONCRETE = tag("concrete");
        public static final TagKey<Item> CONCRETE_POWDER = tag("concrete_powder");
        public static final TagKey<Item> GLASS = tag("glass");
        public static final TagKey<Item> GLASS_PANES = tag("glass_panes");
        public static final TagKey<Item> TERRACOTTA = tag("terracotta");
        public static final TagKey<Item> BASE_STONE_OVERWORLD = tag("base_stone_overworld");
        
        private static TagKey<Item> tag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier(BabblinMC.MOD_ID, name));
        }
    }
}
