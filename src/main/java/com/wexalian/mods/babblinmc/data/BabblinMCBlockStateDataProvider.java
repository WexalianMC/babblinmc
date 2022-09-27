package com.wexalian.mods.babblinmc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class BabblinMCBlockStateDataProvider extends FabricModelProvider {
    public BabblinMCBlockStateDataProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // VariantsBlockStateSupplier.create(BabblinMCBlocks.ENDER_TANK).coordinate(BlockStateVariantMap.create(EnderTankBlock.FACING, EnderTankBlock.POWERED, EnderTankBlock.VARIANT, EnderTankBlock.FLOW).register((facing, powered, variant, flow) -> {
        //
        // })
    }
    // private void registerRepeater() {
    //     // this.registerItemModel(Items.REPEATER);
    //     this.blockStateCollector.accept(VariantsBlockStateSupplier.create(Blocks.REPEATER).coordinate(BlockStateVariantMap.create(Properties.DELAY, Properties.LOCKED, Properties.POWERED).register((integer, boolean_, boolean2) -> {
    //         StringBuilder stringBuilder = new StringBuilder();
    //         stringBuilder.append('_').append(integer).append("tick");
    //         if (boolean2.booleanValue()) {
    //             stringBuilder.append("_on");
    //         }
    //         if (boolean_.booleanValue()) {
    //             stringBuilder.append("_locked");
    //         }
    //         return BlockStateVariant.create().put(VariantSettings.MODEL, TextureMap.getSubId(Blocks.REPEATER, stringBuilder.toString()));
    //     })).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates()));
    // }
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }
}
