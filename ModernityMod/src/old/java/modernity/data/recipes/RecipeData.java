/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.data.recipes;

import com.google.common.collect.Lists;
import modernity.common.recipes.data.ItemPredicateBuilder;
import modernity.generic.data.IRecipeData;
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
    private static final List<IRecipeData> RECIPES = Lists.newArrayList();

    static {
    }

    public static void addRecipe(IRecipeData type) {
        RECIPES.add(type);
    }

    static List<IRecipeData> getRecipes() {
        return RECIPES;
    }

    public static InventoryChangeTrigger.Instance hasItem(MinMaxBounds.IntBound amount, IItemProvider itemIn) {
        return hasItem(ItemPredicateBuilder.create().item(itemIn).count(amount).build());
    }

    public static InventoryChangeTrigger.Instance hasItem(IItemProvider itemIn) {
        return hasItem(ItemPredicate.Builder.create().item(itemIn).build());
    }

    public static InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
        return hasItem(ItemPredicate.Builder.create().tag(tagIn).build());
    }

    public static InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.Instance(MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    public EnterBlockTrigger.Instance enteredBlock(Block blockIn) {
        return new EnterBlockTrigger.Instance(blockIn, null);
    }
}
