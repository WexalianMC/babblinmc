package com.wexalian.mods.babblinmc.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class InventoryUtil {
    public static boolean putItems(Inventory inventory, Collection<ItemStack> stacks, Direction side, boolean actuallyPutItems) {
        if (!actuallyPutItems) {
            stacks = deepCopy(stacks);
        }
        
        if (inventory instanceof SidedInventory sidedInventory) {
            int[] availableSlots = sidedInventory.getAvailableSlots(side);
            
            for (int pass = 1; pass <= 2; pass++) {
                for (int availableSlot : availableSlots) {
                    if (putItemsToSlot(inventory, sidedInventory, availableSlot, stacks, side, actuallyPutItems, pass == 2)) {
                        return true;
                    }
                }
            }
        }
        else {
            SidedInventory fakeSidedInventory = new DummySidedInventory();
            
            for (int pass = 1; pass <= 2; pass++) {
                for (int i = 0; i < inventory.size(); i++) {
                    if (putItemsToSlot(inventory, fakeSidedInventory, i, stacks, side, actuallyPutItems, pass == 2)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private static Collection<ItemStack> deepCopy(Collection<ItemStack> collection) {
        Collection<ItemStack> copy = new ArrayList<>(collection.size());
        
        for (ItemStack stack : collection) {
            copy.add(stack.copy());
        }
        
        return copy;
    }
    
    private static boolean putItemsToSlot(Inventory inventory, SidedInventory sidedInventory, int slot, Collection<ItemStack> stacks, Direction side, boolean putItems, boolean considerEmptySlots) {
        ItemStack stackInSlot = inventory.getStack(slot);
        
        Iterator<ItemStack> stacksIterator = stacks.iterator();
        
        while (stacksIterator.hasNext()) {
            ItemStack currentStack = stacksIterator.next();
            
            if (considerEmptySlots && stackInSlot.isEmpty()) {
                if (inventory.isValid(slot, currentStack) && sidedInventory.canInsert(slot, currentStack, side)) {
                    stackInSlot = currentStack;
                    
                    if (putItems) {
                        inventory.setStack(slot, currentStack);
                    }
                    
                    stacksIterator.remove();
                }
            }
            else if (itemsEqual(currentStack, stackInSlot)) {
                int totalCount = currentStack.getCount() + stackInSlot.getCount();
                int maxCount = Math.min(inventory.getMaxCountPerStack(), currentStack.getMaxCount());
                int remainingCount = Math.max(0, totalCount - maxCount);
                
                if (inventory.isValid(slot, currentStack) && sidedInventory.canInsert(slot, currentStack, side)) {
                    if (putItems) {
                        stackInSlot.setCount(remainingCount == 0 ? totalCount : maxCount);
                        inventory.setStack(slot, stackInSlot);
                    }
                    
                    if (remainingCount == 0) {
                        stacksIterator.remove();
                    }
                    else {
                        currentStack.setCount(remainingCount);
                    }
                }
            }
        }
        
        return stacks.isEmpty();
    }
    
    public static boolean itemsEqual(ItemStack first, ItemStack second) {
        return ItemStack.areItemsEqual(first, second) && ItemStack.areNbtEqual(first, second);
    }
}