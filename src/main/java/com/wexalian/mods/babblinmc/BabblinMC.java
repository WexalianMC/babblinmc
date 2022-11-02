package com.wexalian.mods.babblinmc;

import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTankManager;
import com.wexalian.mods.babblinmc.command.BabblinMCCommands;
import com.wexalian.mods.babblinmc.config.ModConfig;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.mods.babblinmc.init.BabblinMCBlockEntities;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import com.wexalian.mods.babblinmc.init.BabblinMCEffects;
import com.wexalian.mods.babblinmc.init.BabblinMCItems;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class BabblinMC implements ModInitializer {
    public static final String MOD_ID = "babblinmc";
    
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
    
    public static final ItemGroup GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "group"))
                                                                .icon(() -> new ItemStack(Items.STICK))
                                                                .build();
    
    public static final Identifier MOD_FEATURE_SYNC = new Identifier(MOD_ID, "mod_feature_sync");
    public static final Identifier MOD_FEATURES_SYNC = new Identifier(MOD_ID, "mod_features_sync");
    
    @Override
    public void onInitialize() {
        BabblinMCBlocks.register();
        BabblinMCBlockEntities.register();
        BabblinMCItems.register();
        BabblinMCEffects.register();
        
        ModFeatures.setup();
        ModConfig.instance().load(CONFIG_PATH);
        ModFeatures.init();
        
        BabblinMCCommands.INSTANCE.registerArguments();
        CommandRegistrationCallback.EVENT.register(BabblinMCCommands.INSTANCE::register);
        
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
    }
    
    private void onServerStarted(MinecraftServer server) {
        EnderTankManager.readNbt(getEnderTankNbtPath(server));
    }
    
    private void onServerStopped(MinecraftServer server) {
        EnderTankManager.writeNbt(getEnderTankNbtPath(server));
        
        ModConfig.instance().save();
    }
    
    @Nonnull
    private static Path getEnderTankNbtPath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("ender_tanks.nbt");
    }
}
