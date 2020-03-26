/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.client.util;

import modernity.api.util.EnumUtil;
import modernity.common.block.MDNatureBlocks;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class MDRecipeBookCategories {
    public static final RecipeBookCategories CLEANER_SEARCH = createCategory( "CLEANER_SEARCH", new ItemStack( Items.COMPASS ) );
    public static final RecipeBookCategories CLEANER_BLOCKS = createCategory( "CLEANER_BLOCKS", new ItemStack( MDNatureBlocks.MURKY_DIRT ) );

    private MDRecipeBookCategories() {
    }

    private static RecipeBookCategories createCategory( String name, ItemStack... icons ) {
        return EnumUtil.addEnum( RecipeBookCategories.class, name, new Class[] { ItemStack[].class }, (Object) icons );
    }

    public static void init() {
        // Just for initializing this class
    }
}