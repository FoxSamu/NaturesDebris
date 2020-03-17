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
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.IItemProvider;
import net.redgalaxy.util.Lazy;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BarkRecipeType implements IRecipeDataType {
    private final Lazy<IItemProvider> wood;
    private final Lazy<IItemProvider> log;

    public BarkRecipeType( Supplier<IItemProvider> wood, Supplier<IItemProvider> log ) {
        this.wood = Lazy.of( wood );
        this.log = Lazy.of( log );
    }

    @Override
    public void build( Consumer<IFinishedRecipe> consumer ) {
        ShapedRecipeBuilder.shapedRecipe( wood.get(), 3 )
                           .key( '#', log.get() )
                           .patternLine( "##" )
                           .patternLine( "##" )
                           .setGroup( "bark" )
                           .addCriterion( "has_log", RecipeData.hasItem( log.get() ) )
                           .build( consumer );
    }
}
