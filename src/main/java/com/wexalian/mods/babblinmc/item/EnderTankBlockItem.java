package com.wexalian.mods.babblinmc.item;

import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.block.EnderTankBlock;
import com.wexalian.mods.babblinmc.block.entity.endertank.ColorCode;
import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTankManager;
import com.wexalian.mods.babblinmc.block.entity.endertank.TankOwner;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import com.wexalian.nullability.annotations.Nonnull;
import com.wexalian.nullability.annotations.Nullable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class EnderTankBlockItem extends BlockItem {
    private static final Style GRAY_STYLE = Style.EMPTY.withColor(Formatting.GRAY);
    private static final Style BLUE_STYLE = Style.EMPTY.withColor(Formatting.BLUE);
    private static final Style ORANGE_STYLE = Style.EMPTY.withColor(ColorCode.toHex(DyeColor.ORANGE));
    
    public EnderTankBlockItem() {
        super(BabblinMCBlocks.ENDER_TANK, new Settings().group(BabblinMC.GROUP).maxCount(1));
    }
    
    // @Override
    // protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
    //     boolean success = super.postPlacement(pos, world, player, stack, state);
    //     if (success && !world.isClient) {
    //         if (world.getBlockEntity(pos) instanceof EnderTankBlockEntity tank) {
    //             tank.setColorCode(color);
    //         }
    //     }
    //     return success;
    // }
    
    // public DyeColor getColor() {
    //     return color;
    // }
    
    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                addColorCodeToStack(color, stack);
                stacks.add(stack);
            }
        }
    }
    
    public static void addColorCodeToStack(DyeColor color, ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateSubNbt("BlockEntityTag");
        nbt.putString("code", ColorCode.of(color, color, color).serializeString());
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        
        ColorCode code = getCode(stack);
        Text colorName0 = getColorName(code, 0);
        Text colorName1 = getColorName(code, 1);
        Text colorName2 = getColorName(code, 2);
        
        MutableText codeText = new LiteralText("Code: [").setStyle(GRAY_STYLE);
        codeText = codeText.append(colorName0).append(", ").append(colorName1).append(", ").append(colorName2).append("]");
        tooltip.add(codeText);
        
        EnderTankBlock.FlowDirection flow = getValue(stack, EnderTankBlock.FLOW, EnderTankBlock.FlowDirection.NONE);
        Text flowDirText = getFlowDirectionText(flow);
        Text flowText = new LiteralText("Owner: ").setStyle(GRAY_STYLE).append(flowDirText);
        tooltip.add(flowText);
        
        TankOwner owner = getOwner(stack);
        Text ownerText = new LiteralText("Owner: ").setStyle(GRAY_STYLE).append(owner.getOwnerName());
        tooltip.add(ownerText);
        
        long droplets = EnderTankManager.getEnderTank(owner, code, false).getFluidStorage().getAmount();
        long mb = droplets / (FluidConstants.BUCKET / FluidConstants.NUGGET);
        Text amountText = new LiteralText("Amount: " + mb + " mB").setStyle(GRAY_STYLE);
        tooltip.add(amountText);
        
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
    
    private static MutableText getColorName(ColorCode code, int index) {
        Style style = Style.EMPTY.withColor(code.toHex(index));
        return new LiteralText(code.getName(index)).setStyle(style);
    }
    
    public static int getRingColor(ItemStack stack, int tintIndex) {
        if (tintIndex >= 1 && tintIndex <= 3) {
            return getCode(stack).toHex(tintIndex - 1);
        }
        return 0xFFFFFF;
    }
    
    private static TankOwner getOwner(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt != null) {
            return TankOwner.readNbt(nbt.getCompound("owner"));
        }
        return TankOwner.ALL;
    }
    
    private static ColorCode getCode(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt != null) {
            return ColorCode.deserializeString(nbt.getString("code"));
        }
        return ColorCode.ALL_WHITE;
    }
    
    private Text getFlowDirectionText(EnderTankBlock.FlowDirection flow) {
        final LiteralText flowText = new LiteralText(flow.getFormatName());
        
        return switch (flow) {
            case NONE -> flowText;
            case INPUT -> flowText.setStyle(BLUE_STYLE);
            case OUTPUT -> flowText.setStyle(ORANGE_STYLE);
        };
    }
}
