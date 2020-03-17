/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
 * Author: rgsw
 */

package modernity.data.recipes.type;

import modernity.common.block.MDNatureBlocks;
import modernity.common.item.MDItems;
import modernity.data.recipes.IRecipeDataType;
import modernity.data.recipes.RecipeData;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class NatureRecipes implements IRecipeDataType {
    @Override
    public void build( Consumer<IFinishedRecipe> consumer ) {
        ShapedRecipeBuilder.shapedRecipe( MDNatureBlocks.ASPHALT_CONCRETE )
                           .key( 'c', MDItems.ANTHRACITE )
                           .key( 'g', MDItems.GOO_BALL )
                           .key( 'r', MDNatureBlocks.REGOLITH )
                           .patternLine( "cgc" )
                           .patternLine( "grg" )
                           .patternLine( "cgc" )
                           .setGroup( "minerals" )
                           .addCriterion( "has_anthracite", RecipeData.hasItem( MDItems.ANTHRACITE ) )
                           .addCriterion( "has_goo", RecipeData.hasItem( MDItems.GOO_BALL ) )
                           .addCriterion( "has_regolith", RecipeData.hasItem( MDNatureBlocks.REGOLITH ) )
                           .build( consumer );

        CookingRecipeBuilder.smeltingRecipe(
            Ingredient.fromItems( MDNatureBlocks.MURKY_CLAY ),
            MDNatureBlocks.MURKY_TERRACOTTA,
            0.1F, 200
        ).addCriterion( "has_clay", RecipeData.hasItem( MDNatureBlocks.MURKY_CLAY ) ).build( consumer );
    }
}
