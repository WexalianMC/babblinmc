package com.wexalian.mods.babblinmc.feature.impl;

import com.wexalian.config.ConfigHandler;
import com.wexalian.mods.babblinmc.feature.ModFeature;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldEvents;

public class MusicDiscModFeature extends ModFeature {
    public MusicDiscModFeature(String id, ConfigHandler config) {
        super(id, config);
    }
    
    @Override
    public void init() {
        for (Item item : Registry.ITEM) {
            if (item instanceof MusicDiscItem) {
                DispenserBlock.registerBehavior(item, new Behavior());
            }
        }
    }
    
    public boolean onDispenseEmpty(BlockPointer pointer) {
        if (isEnabled()) {
            if (hasJukeboxInFront(pointer)) {
                BlockState state = pointer.getBlockState();
                Direction front = state.get(DispenserBlock.FACING);
                BlockPos jukeboxPos = pointer.getPos().offset(front);
                BlockState jukeboxState = pointer.getWorld().getBlockState(jukeboxPos);
                
                BlockEntity blockEntity = pointer.getWorld().getBlockEntity(jukeboxPos);
                if (jukeboxState.get(JukeboxBlock.HAS_RECORD) && blockEntity instanceof JukeboxBlockEntity jukeboxEntity) {
                    ItemStack returnedDisc = jukeboxEntity.getRecord();
                    DispenserBlockEntity dispenserEntity = pointer.getBlockEntity();
                    int slot = dispenserEntity.addToFirstFreeSlot(returnedDisc.copy());
                    if (slot >= 0) {
                        jukeboxEntity.clear();
                        pointer.getWorld().setBlockState(jukeboxPos, jukeboxState.with(JukeboxBlock.HAS_RECORD, false), Block.NOTIFY_LISTENERS);
                        
                        pointer.getWorld().syncWorldEvent(WorldEvents.MUSIC_DISC_PLAYED, jukeboxPos, 0);
                        pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.getPos(), 0);
                        pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pointer.getPos(), front.getId());
                        
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean hasJukeboxInFront(BlockPointer pointer) {
        Direction facing = pointer.getBlockState().get(DispenserBlock.FACING);
        
        BlockPos frontPos = pointer.getPos().offset(facing);
        BlockState frontState = pointer.getWorld().getBlockState(frontPos);
        
        return frontState.getBlock() == Blocks.JUKEBOX;
    }
    
    public class Behavior extends FallibleItemDispenserBehavior {
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack musicDisc) {
            if (isEnabled()) {
                if (hasJukeboxInFront(pointer)) {
                    BlockState state = pointer.getBlockState();
                    Direction front = state.get(DispenserBlock.FACING);
                    BlockPos jukeboxPos = pointer.getPos().offset(front);
                    BlockState jukeboxState = pointer.getWorld().getBlockState(jukeboxPos);
                    
                    ItemStack returnedDisc = ItemStack.EMPTY;
                    
                    if (jukeboxState.get(JukeboxBlock.HAS_RECORD)) {
                        BlockEntity blockEntity = pointer.getWorld().getBlockEntity(jukeboxPos);
                        if (blockEntity instanceof JukeboxBlockEntity jukeboxEntity) {
                            returnedDisc = jukeboxEntity.getRecord().copy();
                            jukeboxEntity.clear();
                        }
                    }
                    ((JukeboxBlock) Blocks.JUKEBOX).setRecord(pointer.getWorld(), jukeboxPos, jukeboxState, musicDisc);
                    pointer.getWorld().syncWorldEvent(WorldEvents.MUSIC_DISC_PLAYED, jukeboxPos, Item.getRawId(musicDisc.getItem()));
                    
                    return returnedDisc;
                }
            }
            return super.dispenseSilently(pointer, musicDisc);
        }
    }
}
