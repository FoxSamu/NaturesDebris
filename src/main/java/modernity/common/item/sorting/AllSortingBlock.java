/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 05 - 2020
 * Author: rgsw
 */

package modernity.common.item.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class AllSortingBlock implements ItemSortingBlock {
    private final NonNullList<ItemStack> stacks = NonNullList.create();

    @Override
    public void reset() {
        stacks.clear();
    }

    @Override
    public boolean acceptItem( ItemStack item ) {
        stacks.add( item );
        return true;
    }

    @Override
    public void fill( NonNullList<ItemStack> out ) {
        out.addAll( stacks );
    }
}
