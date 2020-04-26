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

public interface ItemSortingBlock {
    boolean acceptItem( ItemStack item );
    void fill( NonNullList<ItemStack> out );
    default void reset() {
    }
}
