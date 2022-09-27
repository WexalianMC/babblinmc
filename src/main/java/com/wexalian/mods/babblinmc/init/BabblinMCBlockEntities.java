package com.wexalian.mods.babblinmc.init;

import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTankBlockEntity;
import com.wexalian.mods.babblinmc.block.entity.pump.PumpBlockEntity;
import com.wexalian.nullability.annotations.Nonnull;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.EnergyStorage;

public final class BabblinMCBlockEntities {
    
    public static BlockEntityType<PumpBlockEntity> PUMP;
    public static BlockEntityType<EnderTankBlockEntity> ENDER_TANK;
    
    public static void register() {
        PUMP = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                 new Identifier(BabblinMC.MOD_ID, "pump"),
                                 create(PumpBlockEntity::new, BabblinMCBlocks.PUMP));
        
        FluidStorage.SIDED.registerForBlockEntity((pump, direction) -> pump.getFluidStorage(), PUMP);
        EnergyStorage.SIDED.registerForBlockEntity((pump, direction) -> pump.getEnergyStorage(), PUMP);
        
        ENDER_TANK = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                 new Identifier(BabblinMC.MOD_ID, "ender_tank"),
                                 create(EnderTankBlockEntity::new, BabblinMCBlocks.ENDER_TANK));
        
        FluidStorage.SIDED.registerForBlockEntity((tank, direction) -> tank.getFluidStorage(), ENDER_TANK);
    }
    
    public static <T extends BlockEntity> BlockEntityType<T> create(FabricBlockEntityTypeBuilder.Factory<T> supplier, @Nonnull Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(supplier, blocks).build();
    }
}
