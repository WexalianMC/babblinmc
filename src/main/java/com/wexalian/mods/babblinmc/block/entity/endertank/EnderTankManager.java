package com.wexalian.mods.babblinmc.block.entity.endertank;

import com.wexalian.common.unchecked.Unchecked;
import com.wexalian.mods.babblinmc.BabblinMC;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EnderTankManager {
    
    private static final Map<TankOwner, Map<ColorCode, EnderTank>> CONTAINERS_DATABASE = new ConcurrentHashMap<>();
    private static final Map<TankOwner, Map<ColorCode, EnderTank>> CONTAINERS_CLIENT_CACHE = new ConcurrentHashMap<>();
    
    public static Map<TankOwner, Map<ColorCode, EnderTank>> getContainerTable(boolean isClient) {
        return isClient ? CONTAINERS_CLIENT_CACHE : CONTAINERS_DATABASE;
    }
    
    public static EnderTank getEnderTank(TankOwner owner, ColorCode code, boolean isClient) {
        return getContainerTable(isClient).computeIfAbsent(owner, o -> new ConcurrentHashMap<>())
                                          .computeIfAbsent(code, c -> new EnderTank(owner, code));
    }
    
    private static NbtCompound getSaveData() {
        Map<TankOwner, Map<ColorCode, EnderTank>> tanks = getContainerTable(false);
        NbtCompound enderTankNbt = new NbtCompound();
        
        for (TankOwner owner : tanks.keySet()) {
            NbtCompound ownerNbt = new NbtCompound();
            boolean added = false;
            
            for (var entry : tanks.get(owner).entrySet()) {
                ColorCode code = entry.getKey();
                EnderTank tank = entry.getValue();
                
                if (!tank.isEmpty()) {
                    NbtCompound tankNbt = new NbtCompound();
                    tank.writeNbt(tankNbt);
                    
                    ownerNbt.put(code.serializeString(), tankNbt);
                    added = true;
                }
            }
            
            if (added) {
                ownerNbt.putString("name", owner.getOwnerName().asString());
                String key = owner.getOwnerId() == null ? "all" : owner.getOwnerId().toString();
                enderTankNbt.put(key, ownerNbt);
            }
            
        }
        
        return enderTankNbt;
    }
    
    private static void loadSaveData(NbtCompound nbt) {
        for (String ownerId : nbt.getKeys()) {
            NbtCompound ownerNbt = nbt.getCompound(ownerId);
            TankOwner owner = getOwner(ownerId, ownerNbt.getString("name"));
            
            for (String key : ownerNbt.getKeys()) {
                if (key.equalsIgnoreCase("name")) continue;
                
                ColorCode code = ColorCode.deserializeString(key);
                NbtCompound tankNbt = ownerNbt.getCompound(key);
                
                getEnderTank(owner, code, false).readNbt(tankNbt);
            }
        }
    }
    
    private static TankOwner getOwner(String ownerId, String ownerName) {
        if (ownerId.trim().equalsIgnoreCase("all")) {
            return TankOwner.ALL;
        }
        UUID uuid = UUID.fromString(ownerId);
        Text name = Text.of(ownerName);
        
        return TankOwner.of(uuid, name);
    }
    
    public static void writeNbt(Path path) {
        NbtCompound compound = getSaveData();
        if (!compound.isEmpty()) {
            BabblinMC.LOGGER.info("Ender Tank contents found, saving");
            Unchecked.accept(compound, path.toFile(), NbtIo::writeCompressed);
        }
        
    }
    
    public static void readNbt(Path path) {
        if (Files.exists(path)) {
            BabblinMC.LOGGER.info("Ender Tank info found, loading");
            NbtCompound nbt = Unchecked.apply(path.toFile(), NbtIo::readCompressed);
            if (nbt != null) {
                loadSaveData(nbt);
            }
        }
    }
}
