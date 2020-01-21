/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container.slot;

import modernity.common.tileentity.CleanerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CleanerResultSlot extends Slot {
    private final PlayerEntity player;
    private int removeCount;

    public CleanerResultSlot( PlayerEntity player, IInventory inv, int index, int xPosition, int yPosition ) {
        super( inv, index, xPosition, yPosition );
        this.player = player;
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return false;
    }

    @Override
    public ItemStack decrStackSize( int amount ) {
        if( getHasStack() ) {
            removeCount += Math.min( amount, getStack().getCount() );
        }

        return super.decrStackSize( amount );
    }

    @Override
    public ItemStack onTake( PlayerEntity thePlayer, ItemStack stack ) {
        onCrafting( stack );
        super.onTake( thePlayer, stack );
        return stack;
    }

    @Override
    protected void onCrafting( ItemStack stack, int amount ) {
        removeCount += amount;
        onCrafting( stack );
    }

    @Override
    protected void onCrafting( ItemStack stack ) {
        stack.onCrafting( player.world, player, removeCount );
        if( ! player.world.isRemote && inventory instanceof CleanerTileEntity ) {
            ( (CleanerTileEntity) inventory ).unlockUsedRecipesAndGainXP( player );
        }

        removeCount = 0;
    }
}