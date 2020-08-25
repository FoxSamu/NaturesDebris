/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.gui.recipebook;

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
