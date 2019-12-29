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

public class WorkbenchBookType implements IRecipeBookType {
    @Override
    public ResourceLocation getTexture() {
        return RecipeBookGui.TEXTURE;
    }

    @Override
    public int[] getRecipeButtonTextureCoords() {
        return new int[] { 152, 78, 26, 26 };
    }

    @Override
    public int[] getToggleButtonTextureCoords() {
        return new int[] { 152, 41, 28, 18 };
    }

    @Override
    public boolean canCraftRecipe( RecipeBookContainer<?> container, IRecipe<?> recipe ) {
        return true;
    }

    @Override
    public AbstractRecipeButtonWidget createRecipeButton( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
        return null;
    }
}
