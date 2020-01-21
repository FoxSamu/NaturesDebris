/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.api.block.fluid.IVanillaBucketTakeable;
import modernity.common.recipes.CleaningRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Collection;

public class CleanerRecipeButtonWidget extends AbstractRecipeButtonWidget {
    public CleanerRecipeButtonWidget( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
        super( gui, x, y, recipe, craftable );
    }

    @Override
    protected void placeRecipe( IRecipe<?> recipe ) {
        ItemStack[] stacks = recipe.getIngredients().get( 0 ).getMatchingStacks();
        ItemStack[] left = { ItemStack.EMPTY };
        if( recipe instanceof CleaningRecipe ) {
            Collection<Fluid> fluids = ( (CleaningRecipe) recipe ).getRequiredFluid().getMatchingFluids();
            if( ! fluids.isEmpty() ) {
                ArrayList<ItemStack> list = new ArrayList<>();
                for( Fluid f : fluids ) {
                    if( f instanceof IAluminiumBucketTakeable ) {
                        list.add( new ItemStack( ( (IAluminiumBucketTakeable) f ).getFilledAluminiumBucket() ) );
                    }

                    if( f instanceof IVanillaBucketTakeable || f.getFilledBucket() != Items.BUCKET ) {
                        list.add( new ItemStack( f.getFilledBucket() ) );
                    }
                }

                if( ! list.isEmpty() ) {
                    left = list.toArray( left );
                }
            }
        }
        children.add( new Child( 13, 10, stacks ) );
        children.add( new Child( 5, 10, left ) );
    }
}
