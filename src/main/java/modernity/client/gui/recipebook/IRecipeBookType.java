/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public interface IRecipeBookType {
    ResourceLocation getTexture();
    int[] getRecipeButtonTextureCoords();
    int[] getToggleButtonTextureCoords();

    boolean canCraftRecipe( RecipeBookContainer<?> container, IRecipe<?> recipe );

    AbstractRecipeButtonWidget createRecipeButton( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable );
}
