package com.wexalian.mods.babblinmc.init;

import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.block.EnderTankBlock;
import com.wexalian.mods.babblinmc.block.PumpBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class BabblinMCBlocks {
    public static final Block PUMP = new PumpBlock();
    public static final Block ENDER_TANK = new EnderTankBlock();
    
    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(BabblinMC.MOD_ID, "pump"), PUMP);
        Registry.register(Registry.BLOCK, new Identifier(BabblinMC.MOD_ID, "ender_tank"), ENDER_TANK);
    }
}
