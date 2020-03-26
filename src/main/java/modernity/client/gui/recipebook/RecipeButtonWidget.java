/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class RecipeButtonWidget extends AbstractRecipeButtonWidget {
//    public RecipeButtonWidget( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
//        super( gui, x, y, recipe, craftable );
//    }
//
//    @Override
//    protected void placeRecipe( IRecipe<?> recipe ) {
//        placeRecipe( 3, 3, - 1, recipe, recipe.getIngredients().iterator(), 0 );
//    }
//
//    @Override
//    public void setSlotContents( Iterator<Ingredient> ingredients, int slot, int maxAmount, int y, int x ) {
//        ItemStack[] items = ingredients.next().getMatchingStacks();
//        if( items.length != 0 ) {
//            children.add( new Child( 3 + x * 7, 3 + y * 7, items ) );
//        }
//    }
//}
