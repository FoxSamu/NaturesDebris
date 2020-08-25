/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.container.inventory;

import natures.debris.common.tileentity.WorkbenchTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;

public class WorkbenchInventory extends CraftingInventory {
    private final WorkbenchTileEntity tile;
    private final Container craftMatrixHandler;

    public WorkbenchInventory(Container eventHandler, int width, int height, WorkbenchTileEntity tile) {
        super(eventHandler, width, height);
        this.tile = tile;
        this.craftMatrixHandler = eventHandler;
    }

    @Override
    public int getSizeInventory() {
        return tile.getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return tile.getStackInSlot(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = tile.removeStackFromSlot(index);
        if (!stack.isEmpty()) {
            craftMatrixHandler.onCraftMatrixChanged(this);
        }

        return stack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = tile.decrStackSize(index, count);
        if (!stack.isEmpty()) {
            craftMatrixHandler.onCraftMatrixChanged(this);
        }

        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        tile.setInventorySlotContents(index, stack);
        craftMatrixHandler.onCraftMatrixChanged(this);
    }

    @Override
    public void clear() {
        tile.clear();
        craftMatrixHandler.onCraftMatrixChanged(this);
    }

    @Override
    public void markDirty() {
        tile.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return tile.isUsableByPlayer(player);
    }

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for (int i = 0; i < tile.getSizeInventory(); i++) {
            helper.accountPlainStack(tile.getStackInSlot(i));
        }
    }
}
