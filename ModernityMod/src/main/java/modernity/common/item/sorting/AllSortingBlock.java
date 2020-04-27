/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item.sorting;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class AllSortingBlock implements ItemSortingBlock {
    private final List<ItemStack> stacks = Lists.newArrayList();

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
