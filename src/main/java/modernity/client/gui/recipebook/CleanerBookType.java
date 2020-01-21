/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CleanerBookType implements IRecipeBookType {
    public static final ResourceLocation TEXTURE = new ResourceLocation( "modernity:textures/gui/recipe_book_buttons.png" );

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public int[] getRecipeButtonTextureCoords() {
        return new int[] { 113, 1, 26, 26 };
    }

    @Override
    public int[] getToggleButtonTextureCoords() {
        return new int[] { 113, 53, 28, 18 };
    }

    @Override
    public boolean canCraftRecipe( RecipeBookContainer<?> container, IRecipe<?> recipe ) {
        return true;
    }

    @Override
    public AbstractRecipeButtonWidget createRecipeButton( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
        return new CleanerRecipeButtonWidget( gui, x, y, recipe, craftable );
    }
}
