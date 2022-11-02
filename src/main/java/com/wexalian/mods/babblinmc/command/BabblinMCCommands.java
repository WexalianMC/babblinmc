package com.wexalian.mods.babblinmc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.wexalian.common.util.StringUtil;
import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.command.argument.ItemArgumentType;
import com.wexalian.mods.babblinmc.command.argument.PermittedFloatArgumentType;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class BabblinMCCommands {
    public static final BabblinMCCommands INSTANCE = new BabblinMCCommands();
    
    private BabblinMCCommands() {}
    
    public void registerArguments() {
        ArgumentTypes.register(BabblinMC.MOD_ID + ":item", ItemArgumentType.class, new ConstantArgumentSerializer<>(ItemArgumentType::all));
        ArgumentTypes.register(BabblinMC.MOD_ID + ":permitted_float", PermittedFloatArgumentType.class, new ConstantArgumentSerializer<>(PermittedFloatArgumentType::permitted));
    }
    
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var root = literal(BabblinMC.MOD_ID).requires(s -> s.hasPermissionLevel(4)).then(createFeaturesCommand()).executes(this::executeRoot);
        
        dispatcher.register(root);
    }
    
    private LiteralArgumentBuilder<ServerCommandSource> createFeaturesCommand() {
        var featuresRoot = literal("features").executes(this::executeListModFeatures);
        for (ModFeature feature : ModFeatures.getModFeatures()) {
            var featureRoot = literal(feature.getId());
            feature.registerSubCommands(featureRoot);
            featuresRoot.then(featureRoot);
        }
        return featuresRoot;
    }
    
    private int executeListModFeatures(CommandContext<ServerCommandSource> context) {
        String text1 = StringUtil.format("Mod features: {}", ModFeatures.getModFeatures().size());
        context.getSource().sendFeedback(Text.of(text1), false);
        for (ModFeature feature : ModFeatures.getModFeatures()) {
            String text2 = StringUtil.format(" - {} = {}", feature.getId(), feature.isEnabled());
            context.getSource().sendFeedback(Text.of(text2), false);
        }
        return 0;
    }
    
    private int executeRoot(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Text.of("The BabblinMC command"), false);
        return 0;
    }
}
