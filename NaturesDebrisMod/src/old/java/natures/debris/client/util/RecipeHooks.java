/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.util;


// TODO Re-evaluate
///**
// * Handles any recipe-related hooks.
// */
//@OnlyIn( Dist.CLIENT )
//public final class RecipeHooks {
//
//    public static RecipeBookCategories getRecipeCategory( IRecipe<?> recipe ) {
//        if( recipe.getId().getNamespace().equals( "modernity" ) ) {
//            IRecipeType<?> type = recipe.getType();
//            if( type == IRecipeType.SMELTING ) {
//                if( recipe.getRecipeOutput().getItem().isFood() ) {
//                    return RecipeBookCategories.FURNACE_FOOD;
//                } else {
//                    return recipe.getRecipeOutput().getItem() instanceof BlockItem ? RecipeBookCategories.FURNACE_BLOCKS : RecipeBookCategories.FURNACE_MISC;
//                }
//            } else if( type == IRecipeType.BLASTING ) {
//                return recipe.getRecipeOutput().getItem() instanceof BlockItem ? RecipeBookCategories.BLAST_FURNACE_BLOCKS : RecipeBookCategories.BLAST_FURNACE_MISC;
//            } else if( type == IRecipeType.SMOKING ) {
//                return RecipeBookCategories.SMOKER_FOOD;
//            } else if( type == IRecipeType.STONECUTTING ) {
//                return RecipeBookCategories.STONECUTTER;
//            } else if( type == IRecipeType.CAMPFIRE_COOKING ) {
//                return RecipeBookCategories.CAMPFIRE;
//            } else {
//                ItemStack out = recipe.getRecipeOutput();
//                ItemGroup group = out.getItem().getGroup();
//                if( group == MDItemGroup.BLOCKS ) {
//                    return RecipeBookCategories.BUILDING_BLOCKS;
//                } else if( group != MDItemGroup.TOOLS && group != MDItemGroup.COMBAT ) {
//                    return RecipeBookCategories.MISC;
//                } else {
//                    return RecipeBookCategories.EQUIPMENT;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static RecipeList newRecipeList( RecipeBookCategories category, List<RecipeList> allRecipes, Map<RecipeBookCategories, List<RecipeList>> recsByCategory ) {
//        if( category == MDRecipeBookCategories.CLEANER_BLOCKS ) {
//            RecipeList list = new RecipeList();
//            allRecipes.add( list );
//            recsByCategory.computeIfAbsent( category, cgry -> Lists.newArrayList() ).add( list );
//            addToSearchList( MDRecipeBookCategories.CLEANER_SEARCH, list, recsByCategory );
//            return list;
//        }
//
//        return null;
//    }
//
//    private static void addToSearchList( RecipeBookCategories category, RecipeList list, Map<RecipeBookCategories, List<RecipeList>> recipesByCategory ) {
//        recipesByCategory.computeIfAbsent( category, cgry -> Lists.newArrayList() ).add( list );
//    }
//}
