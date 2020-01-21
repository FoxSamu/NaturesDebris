/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.recipes;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import modernity.common.container.CleanerContainer;
import modernity.common.container.inventory.ICleaningInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ServerRecipePlacer;

import java.util.Iterator;

public class CleaningRecipePlacer<C extends ICleaningInventory> extends ServerRecipePlacer<C> {
    private boolean matches;

    public CleaningRecipePlacer( RecipeBookContainer<C> container ) {
        super( container );
    }

    @Override
    protected void tryPlaceRecipe( IRecipe<C> rec, boolean all ) {
        matches = recipeBookContainer.matches( rec );
        int count = recipeItemHelper.getBiggestCraftableStack( rec, null );
        if( matches ) {
            ItemStack stack = recipeBookContainer.getSlot( CleanerContainer.INPUT ).getStack();
            if( stack.isEmpty() || count <= stack.getCount() ) {
                return;
            }
        }

        int max = getMaxAmount( all, count, matches );
        IntList packedItems = new IntArrayList();
        if( recipeItemHelper.canCraft( rec, packedItems, max ) ) {
            if( ! matches ) {
                giveToPlayer( recipeBookContainer.getOutputSlot() );
                giveToPlayer( CleanerContainer.INPUT );
            }

            fillInput( max, packedItems );
        }
    }

    @Override
    protected void clear() {
        giveToPlayer( recipeBookContainer.getOutputSlot() );
        super.clear();
    }

    protected void fillInput( int maxSize, IntList packedItems ) {
        Iterator<Integer> iterator = packedItems.iterator();
        Slot input = recipeBookContainer.getSlot( CleanerContainer.INPUT );

        ItemStack stack = RecipeItemHelper.unpack( iterator.next() );
        if( ! stack.isEmpty() ) {
            int size = Math.min( stack.getMaxStackSize(), maxSize );
            if( matches ) {
                size -= input.getStack().getCount();
            }

            for( int j = 0; j < size; ++ j ) {
                consumeIngredient( input, stack );
            }

        }
    }
}