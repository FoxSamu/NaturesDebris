/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.item.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ShovelItem;
import net.redgalaxy.exc.InstanceOfUtilityClassException;

class ShovelProtectedAccess extends ShovelItem {
    private ShovelProtectedAccess() {
        super( ItemTier.DIAMOND, 0, 0, new Item.Properties() );
        throw new InstanceOfUtilityClassException();
    }

    public static void addFlattenBehaviour( Block block, BlockState flattened ) {
        field_195955_e.put( block, flattened );
    }
}
