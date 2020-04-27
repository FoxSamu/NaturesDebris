/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ItemSortingBlock {
    boolean acceptItem( ItemStack item );
    void fill( NonNullList<ItemStack> out );
    default void reset() {
    }
}
