/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
 * Author: rgsw
 */

package modernity.data.recipes;

import com.google.common.collect.Lists;
import modernity.common.block.MDTreeBlocks;
import modernity.data.recipes.type.BarkRecipeType;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

import java.util.List;

public class RecipeData {
    private static final List<IRecipeDataType> RECIPES = Lists.newArrayList();

    static {
        addRecipe( new BarkRecipeType( () -> MDTreeBlocks.BLACKWOOD, () -> MDTreeBlocks.BLACKWOOD_LOG ) );
        addRecipe( new BarkRecipeType( () -> MDTreeBlocks.STRIPPED_BLACKWOOD, () -> MDTreeBlocks.STRIPPED_BLACKWOOD_LOG ) );
        addRecipe( new BarkRecipeType( () -> MDTreeBlocks.INVER_WOOD, () -> MDTreeBlocks.INVER_LOG ) );
        addRecipe( new BarkRecipeType( () -> MDTreeBlocks.STRIPPED_INVER, () -> MDTreeBlocks.STRIPPED_INVER_LOG ) );


    }

    public static void addRecipe( IRecipeDataType type ) {
        RECIPES.add( type );
    }

    static List<IRecipeDataType> getRecipes() {
        return RECIPES;
    }

    public EnterBlockTrigger.Instance enteredBlock( Block blockIn ) {
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
}
