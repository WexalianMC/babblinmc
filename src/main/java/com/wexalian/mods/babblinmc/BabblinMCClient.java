package com.wexalian.mods.babblinmc;

import com.wexalian.mods.babblinmc.block.EnderTankBlock;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import com.wexalian.mods.babblinmc.item.EnderTankBlockItem;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public class BabblinMCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(EnderTankBlock::getRingColor, BabblinMCBlocks.ENDER_TANK);
        ColorProviderRegistry.ITEM.register(EnderTankBlockItem::getRingColor, BabblinMCBlocks.ENDER_TANK);
    
        ModelPredicateProviderRegistry.register(BabblinMCBlocks.ENDER_TANK.asItem(),
                                                new Identifier("variant"),
                                                (stack, world, entity, seed) -> getValue(stack,
                                                                                         EnderTankBlock.VARIANT,
                                                                                         EnderTankBlock.EnumVariant.PUBLIC).ordinal());
        
        ModelPredicateProviderRegistry.register(BabblinMCBlocks.ENDER_TANK.asItem(),
                                                new Identifier("flow"),
                                                (stack, world, entity, seed) -> (float) getValue(stack,
                                                                                                 EnderTankBlock.FLOW,
                                                                                                 EnderTankBlock.FlowDirection.NONE).ordinal() / 2F);
        
        ClientPlayNetworking.registerGlobalReceiver(BabblinMC.MOD_FEATURE_SYNC, (client, handler, buf, responseSender) -> {
            buf.retain();
            client.execute(() -> {
                String id = buf.readString();
                ModFeatures.getModFeature(id).read(buf);
                buf.release();
            });
        });
        
        ClientPlayNetworking.registerGlobalReceiver(BabblinMC.MOD_FEATURES_SYNC, (client, handler, buf, responseSender) -> {
            buf.retain();
            client.execute(() -> {
                int count = buf.readInt();
                for (int i = 0; i < count; i++) {
                    String id = buf.readString();
                    ModFeatures.getModFeature(id).read(buf);
                }
                buf.release();
            });
        });
    }
    
    @Nonnull
    public static <T extends Comparable<T>> T getValue(ItemStack stack, Property<T> property, T defaultValue) {
        NbtCompound nbt = stack.getSubNbt(BlockItem.BLOCK_STATE_TAG_KEY);
        if (nbt != null) {
            String value = nbt.getString(property.getName());
            return property.parse(value).orElse(defaultValue);
        }
        return defaultValue;
    }
}
