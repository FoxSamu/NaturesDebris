/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item.sorting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListSortingBlock implements ItemSortingBlock {
    private final Supplier<IItemProvider>[] items;
    private List<Item> itemList;
    private List<ItemStack> itemStackList;
    private Set<Integer> collectedItemIndices = new HashSet<>();

    @Override
    public void reset() {
        collectedItemIndices.clear();
    }

    @SafeVarargs
    public ListSortingBlock( Supplier<IItemProvider>... items ) {
        this.items = items;
        this.itemStackList = null;
    }

    @Override
    public boolean acceptItem( ItemStack item ) {
        if( itemList == null ) {
            itemList = Stream.of( items )
                             .map( Supplier::get )
                             .map( IItemProvider::asItem )
                             .collect( Collectors.toList() );
        }
        if( itemStackList == null ) {
            itemStackList = NonNullList.withSize( itemList.size(), ItemStack.EMPTY );
        }
        int index = itemList.indexOf( item.getItem() );
        if( index >= 0 ) {
            itemStackList.set( index, item );
            collectedItemIndices.add( index );
            return true;
        }
        return false;
    }

    @Override
    public void fill( NonNullList<ItemStack> out ) {
        for( int i = 0; i < itemList.size(); i++ ) {
            if( collectedItemIndices.contains( i ) ) {
                out.add( itemStackList.get( i ) );
            }
        }
    }
}
