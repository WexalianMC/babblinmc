package com.wexalian.mods.babblinmc.util.hitbox;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

import java.util.Map;

public class HitboxHelper {
    public static Map<Direction, VoxelShape> getBody() {
        double[][] cordsets = {
            { 2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D }, { 1.9D, -0.1D, 1.9D, 3.9D, 2.9D, 3.9D }, { 12.1D, -0.1D, 1.9D, 14.1D, 2.9D, 3.9D }, { 1.9D, -0.1D, 12.1D, 3.9D, 2.9D, 14.1D }, { 12.1D, -0.1D, 12.1D, 14.1D, 2.9D, 14.1D }, { 3.9D, -0.1D, 1.9D, 4.9D, 1.9D, 3.9D }, { 11.1D, -0.1D, 1.9D, 12.1D, 1.9D, 3.9D }, { 3.9D, -0.1D, 12.1D, 4.9D, 1.9D, 14.1D }, { 11.1D, -0.1D, 12.1D, 12.1D, 1.9D, 14.1D }, { 1.9D, -0.1D, 3.9D, 3.9D, 1.9D, 4.9D },
            { 1.9D, -0.1D, 11.1D, 3.9D, 1.9D, 12.1D }, { 12.1D, -0.1D, 3.9D, 14.1D, 1.9D, 4.9D }, { 12.1D, -0.1D, 11.1D, 14.1D, 1.9D, 12.1D }, { 1.9D, 13.1D, 1.9D, 3.9D, 16.1D, 3.9D }, { 12.1D, 13.1D, 1.9D, 14.1D, 16.1D, 3.9D }, { 1.9D, 13.1D, 12.1D, 3.9D, 16.1D, 14.1D }, { 12.1D, 13.1D, 12.1D, 14.1D, 16.1D, 14.1D }, { 3.9D, 14.1D, 1.9D, 4.9D, 16.1D, 3.9D }, { 11.1D, 14.1D, 1.9D, 12.1D, 16.1D, 3.9D }, { 3.9D, 14.1D, 12.1D, 4.9D, 16.1D, 14.1D },
            { 11.1D, 14.1D, 12.1D, 12.1D, 16.1D, 14.1D }, { 1.9D, 14.1D, 3.9D, 3.9D, 16.1D, 4.9D }, { 1.9D, 14.1D, 11.1D, 3.9D, 16.1D, 12.1D }, { 12.1D, 14.1D, 3.9D, 14.1D, 16.1D, 4.9D }, { 12.1D, 14.1D, 11.1D, 14.1D, 16.1D, 12.1D } };
        return HitboxData.build(cordsets, true);
    }
    
    public static Map<Direction, VoxelShape> getRingTop() {
        return HitboxData.build(new double[][] { { 1.5D, 9.5D, 1.5D, 14.5D, 10.5D, 14.5D },  }, true);
    }
    
    public static Map<Direction, VoxelShape> getRingMiddle() {
        return HitboxData.build(new double[][] { { 1.5D, 7.5D, 1.5D, 14.5D, 8.5D, 14.5D },  }, true);
    }
    
    public static Map<Direction, VoxelShape> getRingBottom() {
        return HitboxData.build(new double[][] { { 1.5D, 5.5D, 1.5D, 14.5D, 6.5D, 14.5D },  }, true);
    }
}
