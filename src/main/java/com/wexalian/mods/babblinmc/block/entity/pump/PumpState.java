package com.wexalian.mods.babblinmc.block.entity.pump;

import com.wexalian.mods.babblinmc.feature.ModFeatures;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum PumpState {
    DISABLED(false, false),
    NO_ENERGY(false, false),
    TANK_FULL(false, true),
    REDSTONE(false, false),
    WORKING(true, true),
    ERROR(false, false),
    DONE(false, true);
    
    private final boolean working;
    private final boolean output;
    
    PumpState(boolean working, boolean output) {
        this.working = working;
        this.output = output;
    }
    
    public boolean isWorking() {
        return working;
    }
    
    public boolean isOutput() {
        return output;
    }
    
    @SuppressWarnings("ConstantConditions")
    public static Text getMessage(PumpBlockEntity pump) {
        return switch (pump.getState()) {
            case DISABLED -> new TranslatableText("block.babblinmc.pump.state.disabled");
            case NO_ENERGY -> new TranslatableText("block.babblinmc.pump.state.energy");
            case REDSTONE -> new TranslatableText("block.babblinmc.pump.state.redstone");
            case TANK_FULL -> new TranslatableText("block.babblinmc.pump.state.full");
            case ERROR -> new TranslatableText("block.babblinmc.pump.state.error");
            case DONE -> new TranslatableText("block.babblinmc.pump.state.done");
            case WORKING -> new TranslatableText("block.babblinmc.pump.state.working",
                                                 pump.getCurrentPos().getX(),
                                                 pump.getCurrentPos().getY(),
                                                 pump.getCurrentPos().getZ(),
                                                 pump.getRange(),
                                                 ModFeatures.PUMP.getMaxRange());
        };
    }
}