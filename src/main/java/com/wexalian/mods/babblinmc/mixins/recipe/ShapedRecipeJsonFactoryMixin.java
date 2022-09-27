package com.wexalian.mods.babblinmc.mixins.recipe;

import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShapedRecipeJsonBuilder.class)
public abstract class ShapedRecipeJsonFactoryMixin {
    
    @Redirect(method = "offerTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getGroup()Lnet/minecraft/item/ItemGroup;"))
    protected ItemGroup getGroup(Item instance) {
        ItemGroup group = instance.getGroup();
        if (group == null) {
            group = ItemGroup.DECORATIONS;
        }
        return group;
    }
}
