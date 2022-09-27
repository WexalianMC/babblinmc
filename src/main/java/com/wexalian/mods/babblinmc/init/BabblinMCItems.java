package com.wexalian.mods.babblinmc.init;

import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.item.EnderTankBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class BabblinMCItems {
    public static void register() {
        Registry.register(Registry.ITEM,
                          new Identifier(BabblinMC.MOD_ID, "pump"),
                          new BlockItem(BabblinMCBlocks.PUMP, new Item.Settings().group(BabblinMC.GROUP)));
        
        Registry.register(Registry.ITEM,
                          new Identifier(BabblinMC.MOD_ID, "ender_tank"),
                          new EnderTankBlockItem());
        
    }
}
