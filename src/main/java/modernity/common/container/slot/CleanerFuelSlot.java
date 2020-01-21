/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container.slot;

import modernity.common.container.CleanerContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;

public class CleanerFuelSlot extends Slot {
    private final CleanerContainer cleanerContainer;

    public CleanerFuelSlot( CleanerContainer container, IInventory inv, int idx, int x, int y ) {
        super( inv, idx, x, y );
        this.cleanerContainer = container;
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return isFuel( stack );
    }

    public static boolean isFuel( ItemStack stack ) {
        return AbstractFurnaceTileEntity.isFuel( stack );
    }
}