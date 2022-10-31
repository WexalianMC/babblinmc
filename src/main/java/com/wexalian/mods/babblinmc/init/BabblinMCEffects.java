package com.wexalian.mods.babblinmc.init;

import com.wexalian.mods.babblinmc.BabblinMC;
import com.wexalian.mods.babblinmc.effect.FlightEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BabblinMCEffects {
    public static final FlightEffect FLIGHT_EFFECT = new FlightEffect();
    
    public static void register() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(BabblinMC.MOD_ID, "flight"), FLIGHT_EFFECT);
    }
}
