/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container.slot;

import modernity.common.container.inventory.ICleaningInventory;
import modernity.common.fluid.MDFluidTags;
import modernity.common.item.base.BaseBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CleanerBucketSlot extends Slot {
    private final ICleaningInventory cleaningInv;

    public CleanerBucketSlot( ICleaningInventory inventory, int index, int x, int y ) {
        super( inventory, index, x, y );
        this.cleaningInv = inventory;
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return isBucket( stack );
    }

    @Override
    public int getItemStackLimit( ItemStack stack ) {
        return 1;
    }

    public static boolean isBucket( ItemStack stack ) {
        return stack.getItem() instanceof BucketItem || stack.getItem() instanceof BaseBucketItem;
    }

    public static boolean isFilledBucket( ItemStack stack ) {
        if( isBucket( stack ) ) {
            Fluid fluid;
            if( stack.getItem() instanceof BucketItem ) {
                fluid = ( (BucketItem) stack.getItem() ).getFluid();
            } else {
                fluid = ( (BaseBucketItem) stack.getItem() ).getContaining();
            }
            return fluid != Fluids.EMPTY && fluid.isIn( MDFluidTags.CLEANING );
        }
        return false;
    }

    public static boolean isEmptyBucket( ItemStack stack ) {
        return isBucket( stack ) && ! isFilledBucket( stack );
    }


    @Override
    public void onSlotChanged() {
        ItemStack stack = getStack();
        if( ! stack.isEmpty() ) {
            Item item = stack.getItem();

            Fluid fluid = Fluids.EMPTY;
            if( item instanceof BucketItem ) {
                fluid = ( (BucketItem) item ).getFluid();
            } else if( item instanceof BaseBucketItem ) {
                fluid = ( (BaseBucketItem) item ).getContaining();
            }

            if( fluid != Fluids.EMPTY ) {
                if( cleaningInv.fillWith( fluid ) ) {
                    putStack( stack.getContainerItem() );
                }
            }
        }
        inventory.markDirty();
    }
}
