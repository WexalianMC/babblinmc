package com.wexalian.mods.babblinmc.block;

import com.wexalian.mods.babblinmc.block.entity.pump.PumpBlockEntity;
import com.wexalian.mods.babblinmc.block.entity.pump.PumpState;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.nullability.annotations.Nullable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class PumpBlock extends Block implements BlockEntityProvider {
    public PumpBlock() {
        super(Settings.of(Material.STONE).strength(1.9F).sounds(BlockSoundGroup.STONE));
        
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (ModFeatures.PUMP.isEnabled()) {
                if (world.getBlockEntity(pos) instanceof PumpBlockEntity pump) {
                    Text message = PumpState.getMessage(pump);
                    if (message != null) {
                        player.sendSystemMessage(message, player.getUuid());
                    }
                    var fluid = pump.getFluidStorage();
                    var energy = pump.getEnergyStorage();
                    
                    if (fluid != null && energy != null) {
                        if (fluid.getAmount() == 0) {
                            player.sendSystemMessage(new TranslatableText("block.babblinmc.pump.state_empty",
                                                                          energy.getAmount(),
                                                                          energy.getCapacity()), player.getUuid());
                        }
                        else {
                            Identifier id = Registry.FLUID.getId(fluid.variant.getFluid());
                            player.sendSystemMessage(new TranslatableText("block.babblinmc.pump.state",
                                                                          fluid.getAmount() / (FluidConstants.BUCKET / FluidConstants.NUGGET),
                                                                          new TranslatableText(id.toString()),
                                                                          energy.getAmount(),
                                                                          energy.getCapacity()), player.getUuid());
                        }
                    }
                }
            }
            else {
                player.sendSystemMessage(new TranslatableText("block.babblinmc.pump.state.disabled"), player.getUuid());
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    }
    
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PumpBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return !world.isClient ? (levelTicker, pos, stateTicker, blockEntity) -> ((PumpBlockEntity) blockEntity).tick() : null;
    }
}
