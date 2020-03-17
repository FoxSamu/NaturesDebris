/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
 * Author: rgsw
 */

package modernity.data.recipes.type;

import modernity.data.recipes.IRecipeDataType;
import modernity.data.recipes.RecipeData;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.util.IItemProvider;
import net.redgalaxy.util.Lazy;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MineralBlockRecipeType implements IRecipeDataType {
    private final Lazy<IItemProvider> block;
    private final Lazy<IItemProvider> mineral;



    public MineralBlockRecipeType( Supplier<IItemProvider> mineral, Supplier<IItemProvider> block ) {
        this.block = Lazy.of( block );
        this.mineral = Lazy.of( mineral );
    }

    @Override
    public void build( Consumer<IFinishedRecipe> consumer ) {
        String toBlock = block.get().asItem().getRegistryName() + "_from_item";
        String toMineral = mineral.get().asItem().getRegistryName() + "_from_block";

        ShapedRecipeBuilder.shapedRecipe( block.get() )
                           .key( '#', mineral.get() )
                           .patternLine( "###" )
                           .patternLine( "###" )
                           .patternLine( "###" )
                           .setGroup( "minerals" )
                           .addCriterion( "has_item", RecipeData.hasItem( MinMaxBounds.IntBound.atLeast( 9 ), mineral.get() ) )
                           .build( consumer, toBlock );

        ShapelessRecipeBuilder.shapelessRecipe( block.get(), 9 )
                              .addIngredient( block.get() )
                              .setGroup( "minerals" )
                              .addCriterion( "has_block", RecipeData.hasItem( block.get() ) )
                              .build( consumer, toMineral );
    }
}
