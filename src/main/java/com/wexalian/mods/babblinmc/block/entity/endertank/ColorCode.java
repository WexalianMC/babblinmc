package com.wexalian.mods.babblinmc.block.entity.endertank;

import com.google.common.base.Strings;
import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.util.DyeColor;

import java.util.Objects;

public final class ColorCode {
    public static final ColorCode ALL_WHITE = of(DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE);
    
    private static final String[] COLOR_NAMES = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
    
    private final DyeColor color0;
    private final DyeColor color1;
    private final DyeColor color2;
    
    private ColorCode(@Nonnull DyeColor color0, @Nonnull DyeColor color1, @Nonnull DyeColor color2) {
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
    }
    
    public DyeColor getColor(int index) {
        if (index == 0) return color0;
        if (index == 1) return color1;
        if (index == 2) return color2;
        return DyeColor.WHITE;
    }
    
    public String getName(int index) {
        return COLOR_NAMES[getColor(index).ordinal()];
    }
    
    public ColorCode withColor(int index, DyeColor color) {
        if (index == 0) return of(color, color1, color2);
        if (index == 1) return of(color0, color, color2);
        if (index == 2) return of(color0, color1, color);
        return this;
    }
    
    public int toHex(int index) {
        return toHex(getColor(index));
    }
    
    public static int toHex(DyeColor color) {
        float[] colors = color.getColorComponents();
        int red = (int) (colors[0] * 255);
        int green = (int) (colors[1] * 255);
        int blue = (int) (colors[2] * 255);
        return red << 16 | green << 8 | blue;
    }
    
    public String serializeString() {
        String[] colors = new String[]{color0.getName(), color1.getName(), color2.getName()};
        return String.join(",", colors);
    }
    
    public static ColorCode deserializeString(String formatted) {
        if (Strings.isNullOrEmpty(formatted)) {
            return ALL_WHITE;
        }
        
        String[] split = formatted.split(",");
        
        DyeColor color0 = DyeColor.byName(split[0], DyeColor.WHITE);
        DyeColor color1 = DyeColor.byName(split[1], DyeColor.WHITE);
        DyeColor color2 = DyeColor.byName(split[2], DyeColor.WHITE);
        
        return of(color0, color1, color2);
    }
    
    public static ColorCode of(DyeColor color1, DyeColor color2, DyeColor color3) {
        return new ColorCode(color1, color2, color3);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorCode colorCode = (ColorCode) o;
        return color0 == colorCode.color0 && color1 == colorCode.color1 && color2 == colorCode.color2;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(color0, color1, color2);
    }
    
    @Override
    public String toString() {
        return "ColorCode{" + color0 + ", " + color1 + ", " + color2 + '}';
    }
}
