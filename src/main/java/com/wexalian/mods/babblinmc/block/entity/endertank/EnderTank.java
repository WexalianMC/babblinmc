package com.wexalian.mods.babblinmc.block.entity.endertank;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class EnderTank {
    private final TankOwner owner;
    private final ColorCode code;
    
    private final List<IEnderTankHandler> listeners = new ArrayList<>();
    
    private final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }
        
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET * ModFeatures.ENDER_TANK.getSize();
        }
        
        @Override
        protected void onFinalCommit() {
            runListeners();
        }
    };
    
    public EnderTank(TankOwner owner, ColorCode code) {
        
        this.owner = owner;
        this.code = code;
    }
    
    public void writeNbt(NbtCompound nbt) {
        nbt.put("fluid", fluidStorage.variant.toNbt());
        nbt.putLong("amount", fluidStorage.amount);
    }
    
    public void readNbt(NbtCompound nbt) {
        fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound("fluid"));
        fluidStorage.amount = nbt.getLong("amount");
    }
    
    public void addListener(IEnderTankHandler handler) {
        listeners.add(handler);
    }
    
    public void removeListener(IEnderTankHandler handler) {
        listeners.remove(handler);
    }
    
    private void runListeners() {
        listeners.forEach(IEnderTankHandler::update);
    }
    
    public TankOwner getOwner() {
        return owner;
    }
    
    public ColorCode getCode() {
        return code;
    }
    
    public boolean isEmpty() {
        return fluidStorage.isResourceBlank() || fluidStorage.getAmount() == 0;
    }
    
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return fluidStorage;
    }
}
