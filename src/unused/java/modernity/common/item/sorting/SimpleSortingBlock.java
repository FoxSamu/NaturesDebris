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

import java.util.Comparator;
import java.util.function.Predicate;

public class SimpleSortingBlock implements ItemSortingBlock {
    private final NonNullList<ItemStack> stacks = NonNullList.create();

    private final Predicate<ItemStack> predicate;
    private final Comparator<ItemStack> sorter;

    @Override
    public void reset() {
        stacks.clear();
    }

    public SimpleSortingBlock( Predicate<ItemStack> predicate ) {
        this( predicate, ( o1, o2 ) -> 0 );
    }

    public SimpleSortingBlock( Predicate<ItemStack> predicate, Comparator<ItemStack> sorter ) {
        this.predicate = predicate;
        this.sorter = sorter;
    }

    @Override
    public boolean acceptItem( ItemStack item ) {
        if( predicate.test( item ) ) {
            stacks.add( item );
            return true;
        }
        return false;
    }

    @Override
    public void fill( NonNullList<ItemStack> out ) {
        stacks.sort( sorter );
        out.addAll( stacks );
    }
}
