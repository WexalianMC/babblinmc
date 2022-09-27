package com.wexalian.mods.babblinmc.block.entity.endertank.old;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.wexalian.mods.babblinmc.block.entity.endertank.ColorCode;
import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTankBlockEntity;
import com.wexalian.mods.babblinmc.block.entity.endertank.TankOwner;
import com.wexalian.mods.babblinmc.init.BabblinMCBlocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    
    private static final Table<TankOwner, ColorCode, List<TileReference>> LOCATIONS = HashBasedTable.create();
    
    public static void clear() {
        LOCATIONS.clear();
    }
    
    public static void set(TankOwner owner, ColorCode code, EnderTankBlockEntity tile) {
        List<TileReference> list = LOCATIONS.get(owner, code);
        if (list == null) {
            list = new ArrayList<>();
            LOCATIONS.put(owner, code, list);
        }
        list.add(new TileReference(tile));
    }
    
    public static void clear(TankOwner owner, ColorCode code, EnderTankBlockEntity tile) {
        List<TileReference> list = LOCATIONS.get(owner, code);
        if (list != null) {
            list.remove(tile);
            if (list.isEmpty()) LOCATIONS.remove(owner, code);
        }
    }
    
    public static void doBlockUpdate(TankOwner owner, ColorCode code) {
        MinecraftServer server = null;
        if (server != null) server.execute(() -> performBlockUpdate(owner, code));
    }
    
    private static void performBlockUpdate(TankOwner owner, ColorCode code) {
        List<TileReference> locations = LOCATIONS.get(owner, code);
        if (locations != null && !locations.isEmpty()) {
            ImmutableList.copyOf(locations).iterator().forEachRemaining(reference -> {
                EnderTankBlockEntity tile = reference.get();
                if (tile != null && !tile.isRemoved() && tile.getWorld() != null) {
                    World world = tile.getWorld();
                    if (world.isChunkLoaded(tile.getPos())) {
                        world.updateNeighbors(tile.getPos(), BabblinMCBlocks.ENDER_TANK);
                    }
                }
                else {
                    locations.remove(reference);
                }
            });
        }
        if (locations == null || locations.isEmpty()) {
            LOCATIONS.remove(owner, code);
        }
    }
    
    private static class TileReference extends WeakReference<EnderTankBlockEntity> {
        TileReference(EnderTankBlockEntity referent) {
            super(referent);
        }
        
        public boolean equals(Object object) {
            EnderTankBlockEntity myTile = get();
            if (myTile != null) {
                if (object instanceof EnderTankBlockEntity) return myTile.equals(object);
                if (object instanceof TileReference) return myTile.equals(((TileReference) object).get());
            }
            return false;
        }
    }
}
