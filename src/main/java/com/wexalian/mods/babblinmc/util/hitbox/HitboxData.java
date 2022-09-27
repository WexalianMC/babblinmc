package com.wexalian.mods.babblinmc.util.hitbox;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HitboxData {
    public static Map<Direction, VoxelShape> build(double[][] cordsets, boolean orientedDown) {
        Map<Direction, VoxelShape> voxelShapeMap = new HashMap<>();
        Map<Direction, List<VoxelShape>> shapeMap = new HashMap<>();
        for (double[] cords : cordsets) {
            if (cords.length == 6) {
                Map<Direction, VoxelShape> values = getValues(orientedDown, cords);
                for (Map.Entry<Direction, VoxelShape> value : values.entrySet()) {
                    List<VoxelShape> shapeList = shapeMap.getOrDefault(value.getKey(), new ArrayList<>());
                    shapeList.add(value.getValue());
                    shapeMap.put(value.getKey(), shapeList);
                }
            }
        }
        for (Map.Entry<Direction, List<VoxelShape>> shapeEntry : shapeMap.entrySet()) {
            List<VoxelShape> shapeList = shapeEntry.getValue();
            VoxelShape[] voxels = new VoxelShape[shapeList.size() - 1];
            for (int index = 0; index < voxels.length; index++)
                voxels[index] = shapeList.get(index + 1);
            VoxelShape voxelShape = VoxelShapes.union(shapeList.get(0), voxels);
            voxelShapeMap.put(shapeEntry.getKey(), voxelShape);
        }
        return voxelShapeMap;
    }
    
    private static Map<Direction, VoxelShape> getValues(boolean orientedDown, double... cords) {
        Map<Direction, VoxelShape> map = new HashMap<>();
        if (orientedDown) {
            map.put(Direction.DOWN, createShape(cords[0], cords[1], cords[2], cords[3], cords[4], cords[5]));
            map.put(Direction.UP, createShape(cords[0], 16.0D - cords[4], 16.0D - cords[5], cords[3], 16.0D - cords[1], 16.0D - cords[2]));
            cords = new double[]{cords[0], 16.0D - cords[5], cords[1], cords[3], 16.0D - cords[2], cords[4]};
        }
        else {
            map.put(Direction.DOWN, createShape(cords[0], cords[2], 16.0D - cords[4], cords[3], cords[5], 16.0D - cords[1]));
            map.put(Direction.UP, createShape(cords[0], 16.0D - cords[5], cords[1], cords[3], 16.0D - cords[2], cords[4]));
        }
        map.put(Direction.NORTH, createShape(cords[0], cords[1], cords[2], cords[3], cords[4], cords[5]));
        map.put(Direction.SOUTH, createShape(16.0D - cords[3], cords[1], 16.0D - cords[5], 16.0D - cords[0], cords[4], 16.0D - cords[2]));
        map.put(Direction.WEST, createShape(cords[5], cords[1], 16.0D - cords[0], cords[2], cords[4], 16.0D - cords[3]));
        map.put(Direction.EAST, createShape(16.0D - cords[2], cords[1], cords[0], 16.0D - cords[5], cords[4], cords[3]));
        return map;
    }
    
    public static VoxelShape createShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.createCuboidShape(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }
}