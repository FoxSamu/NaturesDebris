/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.base;

import net.minecraft.item.Item;

/**
 * Implementing blocks have a custom item
 */
@FunctionalInterface
public interface ICustomBlockItem {
    Item createBlockItem( Item.Properties properties );
}
