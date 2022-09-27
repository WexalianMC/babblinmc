package com.wexalian.mods.babblinmc.util;

import com.wexalian.nullability.annotations.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

public final class RaytraceUtil {
    public static int rayTraceShapes(@Nonnull Entity entity, @Nonnull BlockPos pos, @Nonnull VoxelShape[] shapes) {
        Pair<Vec3d, Vec3d> lookVec = getRayTraceVectors(entity);
        return rayTraceShapes(lookVec.getLeft(), lookVec.getRight(), pos, shapes);
    }
    
    @Nonnull
    public static Pair<Vec3d, Vec3d> getRayTraceVectors(@Nonnull Entity entity) {
        float pitch = entity.getPitch();
        float yaw = entity.getYaw();
        Vec3d start = entity.getEyePos();
        float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = -MathHelper.cos(-pitch * 0.017453292F);
        float f4 = MathHelper.sin(-pitch * 0.017453292F);
        float f5 = f2 * f3;
        float f6 = f1 * f3;
        double reachDistance = 5.0D;
        // if (entity instanceof ServerPlayerEntity) {
        //     reachDistance = ((ServerPlayerEntity) entity).getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        // }
        Vec3d end = start.add(f5 * reachDistance, f4 * reachDistance, f6 * reachDistance);
        return Pair.of(start, end);
    }
    
    public static int rayTraceShapes(@Nonnull Vec3d start, @Nonnull Vec3d end, @Nonnull BlockPos pos, @Nonnull VoxelShape[] shapes) {
        int closest = -1;
        double distance = Double.POSITIVE_INFINITY;
        
        for (int index = 0; index < shapes.length; index++) {
            VoxelShape shape = shapes[index];
            HitResult res = shape.raycast(start, end, pos);
            if (res != null) {
                double partDistance = start.squaredDistanceTo(res.getPos());
                if (distance > partDistance) {
                    distance = partDistance;
                    closest = index;
                }
            }
        }
        
        return closest;
    }
}
