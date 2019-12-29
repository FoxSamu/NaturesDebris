/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.recipes;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class CleaningRecipe extends AbstractCookingRecipe {
    private final float fluidAmount;
    private final Fluid requiredFluid;

    public CleaningRecipe( IRecipeType<?> type, ResourceLocation id, String group, Ingredient ingr, ItemStack result, float xp, int cookingTime, float fluidAmount, Fluid requiredFluid ) {
        super( type, id, group, ingr, result, xp, cookingTime );
        this.fluidAmount = fluidAmount;
        this.requiredFluid = requiredFluid;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MDRecipeSerializers.CLEANING;
    }

    public float getFluidAmount() {
        return fluidAmount;
    }

    public Fluid getRequiredFluid() {
        return requiredFluid;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
}
