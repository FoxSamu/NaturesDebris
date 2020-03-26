/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
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
