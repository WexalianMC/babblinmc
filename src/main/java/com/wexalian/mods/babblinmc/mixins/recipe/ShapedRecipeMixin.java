package com.wexalian.mods.babblinmc.mixins.recipe;

import com.wexalian.common.util.collection.MapUtil;
import com.wexalian.mods.babblinmc.item.EnderTankBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin {
    private static final Block[] WOOL_BLOCKS = new Block[]{Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL};
    
    private static final Map<Block, DyeColor> WOOL_COLORS = MapUtil.newHashMap().values(DyeColor.values(), color -> WOOL_BLOCKS[color.ordinal()]);
    
    @Inject(method = "getOutput", at = @At("RETURN"), cancellable = true)
    private void getOutput(CallbackInfoReturnable<ItemStack> callback) {
        ItemStack output = callback.getReturnValue();
        if (output != null && !output.isEmpty() && output.getItem() instanceof EnderTankBlockItem) {
            DefaultedList<Ingredient> inputs = ((ShapedRecipe) (Object) this).getIngredients();
            ItemStack woolStack = inputs.get(1).getMatchingStacks()[0];
            Block woolBlock = Block.getBlockFromItem(woolStack.getItem());
            DyeColor color = WOOL_COLORS.getOrDefault(woolBlock, DyeColor.WHITE);
            EnderTankBlockItem.addColorCodeToStack(color, output);
            
            callback.setReturnValue(output);
        }
    }
}
