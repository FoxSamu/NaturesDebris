/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 06 - 2020
 * Author: rgsw
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
