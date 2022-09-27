package com.wexalian.mods.babblinmc.block.entity.endertank;

import com.wexalian.nullability.annotations.Nonnull;
import com.wexalian.nullability.annotations.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public final class TankOwner {
    public static final TankOwner ALL = new TankOwner(null, Text.of("All"));
    
    @Nullable
    private final UUID ownerId;
    @Nonnull
    private final Text ownerName;
    
    private TankOwner(@Nullable UUID ownerId, @Nonnull Text ownerName) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }
    
    @Nullable
    public UUID getOwnerId() {
        return ownerId;
    }
    
    @Nonnull
    public Text getOwnerName() {
        return ownerName;
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("id", ownerId == null ? "all" : ownerId.toString());
        nbt.putString("name", ownerName.asString());
        return nbt;
    }
    
    public static TankOwner readNbt(NbtCompound nbt) {
        if (nbt == null) return ALL;
        
        String id = nbt.getString("id");
        if (!id.equalsIgnoreCase("all") && !id.isEmpty()) {
            UUID uuid = UUID.fromString(id);
            Text name = Text.of(nbt.getString("name"));
            return TankOwner.of(uuid, name);
        }
        
        return ALL;
    }
    
    public boolean isAll() {
        return this == ALL;
    }
    
    public boolean isOwner(@Nonnull PlayerEntity player) {
        return player.getUuid().equals(ownerId);
    }
    
    public static TankOwner of(@Nullable PlayerEntity player) {
        if (player == null) return ALL;
        return of(player.getUuid(), player.getDisplayName());
    }
    
    public static TankOwner of(@Nullable UUID ownerId, @Nonnull Text ownerName) {
        if (ownerId == null) return ALL;
        return new TankOwner(ownerId, ownerName);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TankOwner tankOwner = (TankOwner) o;
        return Objects.equals(ownerId, tankOwner.ownerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ownerId);
    }
}
