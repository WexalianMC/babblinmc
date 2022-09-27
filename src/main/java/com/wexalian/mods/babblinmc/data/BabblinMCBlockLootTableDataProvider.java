package com.wexalian.mods.babblinmc.data;

import com.wexalian.mods.babblinmc.block.EnderTankBlock;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.function.CopyStateFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class BabblinMCBlockLootTableDataProvider extends FabricBlockLootTableProvider {
    
    protected BabblinMCBlockLootTableDataProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }
    
    @Override
    protected void generateBlockLootTables() {
        addDrop(BabblinMCBlocks.PUMP);
        addDrop(BabblinMCBlocks.ENDER_TANK, this::enderTankDrops);
    }
    
    public LootTable.Builder enderTankDrops(Block blocks) {
        return LootTable.builder()
                        .pool(LootPool.builder()
                                      .conditionally(SurvivesExplosionLootCondition.builder())
                                      .rolls(ConstantLootNumberProvider.create(1.0f))
                                      .with(ItemEntry.builder(blocks)
                                                     .apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                                                                               .withOperation("code", "BlockEntityTag.code")
                                                                               .withOperation("owner", "BlockEntityTag.owner"))
                                                     .apply(CopyStateFunction.builder(blocks)
                                                                             .addProperty(EnderTankBlock.VARIANT)
                                                                             .addProperty(EnderTankBlock.FLOW))
                                                     .alternatively(ItemEntry.builder(blocks))));
    }
}
