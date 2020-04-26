/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.recipes.data;

import modernity.generic.data.IRecipeData;
import modernity.common.recipes.builder.ShapedRecipeBuilder;
import modernity.common.recipes.builder.ShapelessRecipeBuilder;
import modernity.common.recipes.builder.SingleItemRecipeBuilder;
import modernity.common.recipes.builder.SmeltingRecipeBuilder;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public final class RecipeDataTypes {

    public static IRecipeData block9( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .patternLine( "###" )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData block9( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return block9( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData block4( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out )
                                              .key( '#', in )
                                              .patternLine( "##" )
                                              .patternLine( "##" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData block4( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return block4( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData one( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapelessRecipeBuilder.shapelessRecipe( out )
                                                 .group( group )
                                                 .ingredient( in )
                                                 .itemCriterion( in )
                                                 .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData one( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return one( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData one( Tag<Item> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return one( IIngredientData.tag( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData join( IIngredientData in1, IIngredientData in2, IResultData out, String group, String id ) {
        return consumer -> ShapelessRecipeBuilder.shapelessRecipe( out )
                                                 .group( group )
                                                 .ingredient( in1 )
                                                 .ingredient( in2 )
                                                 .itemCriterion( in1 )
                                                 .itemCriterion( in2 )
                                                 .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData join( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, Supplier<IItemProvider> out, int count, String group, String id ) {
        return join( IIngredientData.item( in1 ), IIngredientData.item( in2 ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData add( IIngredientData add, IIngredientData into, IResultData out, String group, String id ) {
        return consumer -> ShapelessRecipeBuilder.shapelessRecipe( out )
                                                 .group( group )
                                                 .ingredient( add )
                                                 .ingredient( into )
                                                 .itemCriterion( add )
                                                 .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData add( Supplier<IItemProvider> add, Supplier<IItemProvider> into, Supplier<IItemProvider> out, int count, String group, String id ) {
        return add( IIngredientData.item( add ), IIngredientData.item( into ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData stonecutting( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> SingleItemRecipeBuilder.stonecutting( in, out )
                                                  .group( group )
                                                  .itemCriterion( in )
                                                  .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData stonecutting( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return stonecutting( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData slab( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 6 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData slab( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return slab( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData stairs( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 4 )
                                              .key( '#', in )
                                              .patternLine( "#  " )
                                              .patternLine( "## " )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData stairs( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return stairs( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData step( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 6 )
                                              .key( '#', in )
                                              .patternLine( "# " )
                                              .patternLine( "##" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData step( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return step( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData wall( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 6 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData wall( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return wall( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData corner( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 16 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( "   " )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData corner( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return corner( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData smoking( IIngredientData in, IItemProvider out, float exp, int time, String group, String id ) {
        return consumer -> SmeltingRecipeBuilder.smokingRecipe( in, out, exp, time )
                                                .group( group )
                                                .itemCriterion( in )
                                                .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData smoking( Supplier<IItemProvider> in, Supplier<IItemProvider> out, float exp, int time, String group, String id ) {
        return smoking( IIngredientData.item( in ), () -> out.get().asItem(), exp, time, group, id );
    }

    public static IRecipeData blasting( IIngredientData in, IItemProvider out, float exp, int time, String group, String id ) {
        return consumer -> SmeltingRecipeBuilder.blastingRecipe( in, out, exp, time )
                                                .group( group )
                                                .itemCriterion( in )
                                                .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData blasting( Supplier<IItemProvider> in, Supplier<IItemProvider> out, float exp, int time, String group, String id ) {
        return blasting( IIngredientData.item( in ), () -> out.get().asItem(), exp, time, group, id );
    }


    public static IRecipeData smelting( IIngredientData in, IItemProvider out, float exp, int time, String group, String id ) {
        return consumer -> SmeltingRecipeBuilder.smeltingRecipe( in, out, exp, time )
                                                .group( group )
                                                .itemCriterion( in )
                                                .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData smelting( Supplier<IItemProvider> in, Supplier<IItemProvider> out, float exp, int time, String group, String id ) {
        return smelting( IIngredientData.item( in ), () -> out.get().asItem(), exp, time, group, id );
    }

    public static IRecipeData smelting( Tag<Item> in, Supplier<IItemProvider> out, float exp, int time, String group, String id ) {
        return smelting( IIngredientData.tag( in ), () -> out.get().asItem(), exp, time, group, id );
    }

    public static IRecipeData smelting( List<Supplier<IItemProvider>> in, Supplier<IItemProvider> out, float exp, int time, String group, String id ) {
        return smelting( IIngredientData.items( in ), () -> out.get().asItem(), exp, time, group, id );
    }

    public static IRecipeData fence( IIngredientData stick, IIngredientData wood, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 3 )
                                              .key( '#', wood )
                                              .key( '/', stick )
                                              .patternLine( "#/#" )
                                              .patternLine( "#/#" )
                                              .group( group )
                                              .itemCriterion( wood )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData fence( Supplier<IItemProvider> stick, Supplier<IItemProvider> wood, Supplier<IItemProvider> out, int count, String group, String id ) {
        return fence( IIngredientData.item( stick ), IIngredientData.item( wood ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData door( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 3 )
                                              .key( '#', in )
                                              .patternLine( "##" )
                                              .patternLine( "##" )
                                              .patternLine( "##" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData door( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return door( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData vert( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "#" )
                                              .patternLine( "#" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData vert( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return vert( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData horiz( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "##" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData horiz( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return horiz( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData vert3( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "#" )
                                              .patternLine( "#" )
                                              .patternLine( "#" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData vert3( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return vert3( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData horiz3( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData horiz3( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return horiz3( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData torch( IIngredientData coal, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 4 )
                                              .key( '*', coal )
                                              .key( '/', stick )
                                              .patternLine( "*" )
                                              .patternLine( "/" )
                                              .group( group )
                                              .itemCriterion( coal )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData torch( Supplier<IItemProvider> coal, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return torch( IIngredientData.item( coal ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData torch( Supplier<IItemProvider> coal, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return torch( IIngredientData.item( coal ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData joinVert( IIngredientData up, IIngredientData down, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( 'U', up )
                                              .key( 'D', down )
                                              .patternLine( "U" )
                                              .patternLine( "D" )
                                              .group( group )
                                              .itemCriterion( up )
                                              .itemCriterion( down )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData joinVert( Supplier<IItemProvider> up, Supplier<IItemProvider> down, Supplier<IItemProvider> out, int count, String group, String id ) {
        return joinVert( IIngredientData.item( up ), IIngredientData.item( down ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData joinHoriz( IIngredientData left, IIngredientData right, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( 'L', left )
                                              .key( 'R', right )
                                              .patternLine( "LR" )
                                              .group( group )
                                              .itemCriterion( left )
                                              .itemCriterion( right )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData joinHoriz( Supplier<IItemProvider> left, Supplier<IItemProvider> right, Supplier<IItemProvider> out, int count, String group, String id ) {
        return joinHoriz( IIngredientData.item( left ), IIngredientData.item( right ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData h( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( "###" )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData h( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return h( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData x( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( " # " )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData x( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return x( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData o8( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .patternLine( "# #" )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData o8( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return o8( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData o8( Tag<Item> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return o8( IIngredientData.tag( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData o4( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( " # " )
                                              .patternLine( "# #" )
                                              .patternLine( " # " )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData o4( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return o4( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData join3( IIngredientData in1, IIngredientData in2, IIngredientData in3, IResultData out, String group, String id ) {
        return consumer -> ShapelessRecipeBuilder.shapelessRecipe( out )
                                                 .group( group )
                                                 .ingredient( in1 )
                                                 .ingredient( in2 )
                                                 .itemCriterion( in1 )
                                                 .itemCriterion( in2 )
                                                 .itemCriterion( in3 )
                                                 .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData join3( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, Supplier<IItemProvider> in3, Supplier<IItemProvider> out, int count, String group, String id ) {
        return join3( IIngredientData.item( in1 ), IIngredientData.item( in2 ), IIngredientData.item( in3 ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData asphalt( IIngredientData goo, IIngredientData coal, IIngredientData gravel, IResultData out, String group, String id ) {
        return consumer -> ShapelessRecipeBuilder.shapelessRecipe( out )
                                                 .group( group )
                                                 .ingredient( goo )
                                                 .ingredient( coal )
                                                 .itemCriterion( goo )
                                                 .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData asphalt( Supplier<IItemProvider> goo, Supplier<IItemProvider> coal, Supplier<IItemProvider> gravel, Supplier<IItemProvider> out, int count, String group, String id ) {
        return asphalt( IIngredientData.item( goo ), IIngredientData.item( coal ), IIngredientData.item( gravel ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData boots( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData boots( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return boots( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData helmet( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData helmet( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return helmet( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData leggings( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "###" )
                                              .patternLine( "# #" )
                                              .patternLine( "# #" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData leggings( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return leggings( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData chestplate( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( "###" )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData chestplate( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return chestplate( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData axe( IIngredientData mat, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '*', mat )
                                              .key( '/', stick )
                                              .patternLine( "**" )
                                              .patternLine( "*/" )
                                              .patternLine( " /" )
                                              .group( group )
                                              .itemCriterion( mat )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData axe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return axe( IIngredientData.item( mat ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData axe( Supplier<IItemProvider> mat, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return axe( IIngredientData.item( mat ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData pickaxe( IIngredientData mat, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '*', mat )
                                              .key( '/', stick )
                                              .patternLine( "***" )
                                              .patternLine( " / " )
                                              .patternLine( " / " )
                                              .group( group )
                                              .itemCriterion( mat )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData pickaxe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return pickaxe( IIngredientData.item( mat ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData pickaxe( Supplier<IItemProvider> mat, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return pickaxe( IIngredientData.item( mat ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData hoe( IIngredientData mat, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '*', mat )
                                              .key( '/', stick )
                                              .patternLine( "**" )
                                              .patternLine( " /" )
                                              .patternLine( " /" )
                                              .group( group )
                                              .itemCriterion( mat )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData hoe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return hoe( IIngredientData.item( mat ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData hoe( Supplier<IItemProvider> mat, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return hoe( IIngredientData.item( mat ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData shovel( IIngredientData mat, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '*', mat )
                                              .key( '/', stick )
                                              .patternLine( "*" )
                                              .patternLine( "/" )
                                              .patternLine( "/" )
                                              .group( group )
                                              .itemCriterion( mat )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData shovel( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return shovel( IIngredientData.item( mat ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData shovel( Supplier<IItemProvider> mat, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return shovel( IIngredientData.item( mat ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData sword( IIngredientData mat, IIngredientData stick, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '*', mat )
                                              .key( '/', stick )
                                              .patternLine( "*" )
                                              .patternLine( "*" )
                                              .patternLine( "/" )
                                              .group( group )
                                              .itemCriterion( mat )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData sword( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return sword( IIngredientData.item( mat ), IIngredientData.item( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData sword( Supplier<IItemProvider> mat, Tag<Item> stick, Supplier<IItemProvider> out, int count, String group, String id ) {
        return sword( IIngredientData.item( mat ), IIngredientData.tag( stick ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData bucket( IIngredientData in, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', in )
                                              .patternLine( "# #" )
                                              .patternLine( " # " )
                                              .group( group )
                                              .itemCriterion( in )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData bucket( Supplier<IItemProvider> in, Supplier<IItemProvider> out, int count, String group, String id ) {
        return bucket( IIngredientData.item( in ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }

    public static IRecipeData inO8( IIngredientData o, IIngredientData inner, IResultData out, String group, String id ) {
        return consumer -> ShapedRecipeBuilder.shapedRecipe( out, 1 )
                                              .key( '#', o )
                                              .key( '*', inner )
                                              .patternLine( "###" )
                                              .patternLine( "#*#" )
                                              .patternLine( "###" )
                                              .group( group )
                                              .itemCriterion( o )
                                              .itemCriterion( inner )
                                              .build( consumer, id( id, out, "" ) );
    }

    public static IRecipeData inO8( Supplier<IItemProvider> o, Supplier<IItemProvider> inner, Supplier<IItemProvider> out, int count, String group, String id ) {
        return inO8( IIngredientData.item( o ), IIngredientData.item( inner ), count == - 1 ? IResultData.item( out ) : IResultData.count( out, count ), group, id );
    }


    private static String id( String format, IResultData data, String suffix ) {
        if( format == null ) format = "%s";
        ResourceLocation reg = data.item().asItem().getRegistryName();
        if( reg == null ) reg = new ResourceLocation( "null" );
        return reg.getNamespace() + ":" + String.format( format, reg.getPath() + suffix );
    }

    private static String id( String format, IItemProvider data, String suffix ) {
        if( format == null ) format = "%s";
        ResourceLocation reg = data.asItem().getRegistryName();
        if( reg == null ) reg = new ResourceLocation( "null" );
        return reg.getNamespace() + ":" + String.format( format, reg.getPath() + suffix );
    }

    public static EnterBlockTrigger.Instance enteredBlock( Block blockIn ) {
        return new EnterBlockTrigger.Instance( blockIn, null );
    }

    public static InventoryChangeTrigger.Instance hasItem( MinMaxBounds.IntBound amount, IItemProvider itemIn ) {
        return hasItem( ItemPredicate.Builder.create().item( itemIn ).count( amount ).build() );
    }

    public static InventoryChangeTrigger.Instance hasItem( IItemProvider itemIn ) {
        return hasItem( ItemPredicate.Builder.create().item( itemIn ).build() );
    }

    public static InventoryChangeTrigger.Instance hasItem( Tag<Item> tagIn ) {
        return hasItem( ItemPredicate.Builder.create().tag( tagIn ).build() );
    }

    public static InventoryChangeTrigger.Instance hasItem( ItemPredicate... predicates ) {
        return new InventoryChangeTrigger.Instance( MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates );
    }

    private RecipeDataTypes() {
    }
}
