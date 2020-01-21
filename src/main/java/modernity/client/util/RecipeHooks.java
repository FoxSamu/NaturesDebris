/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.client.util;

import com.google.common.collect.Lists;
import modernity.common.recipes.CleaningRecipe;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

/**
 * Handles any client-side hooks.
 */
@OnlyIn( Dist.CLIENT )
public final class RecipeHooks {

    public static RecipeBookCategories getRecipeCategory( IRecipe<?> rec ) {
        return rec instanceof CleaningRecipe ? MDRecipeBookCategories.CLEANER_BLOCKS : null;
    }

    public static RecipeList newRecipeList( RecipeBookCategories category, List<RecipeList> allRecipes, Map<RecipeBookCategories, List<RecipeList>> recsByCategory ) {
        if( category == MDRecipeBookCategories.CLEANER_BLOCKS ) {
            RecipeList list = new RecipeList();
            allRecipes.add( list );
            recsByCategory.computeIfAbsent( category, cgry -> Lists.newArrayList() ).add( list );
            addToSearchList( MDRecipeBookCategories.CLEANER_SEARCH, list, recsByCategory );
            return list;
        }

        return null;
    }

    private static void addToSearchList( RecipeBookCategories category, RecipeList list, Map<RecipeBookCategories, List<RecipeList>> recipesByCategory ) {
        recipesByCategory.computeIfAbsent( category, cgry -> Lists.newArrayList() ).add( list );
    }
}
