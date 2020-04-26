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
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemTier;
import net.redgalaxy.exc.InstanceOfUtilityClassException;

class HoeProtectedAccess extends HoeItem {
    private HoeProtectedAccess() {
        super( ItemTier.DIAMOND, 0, new Properties() );
        throw new InstanceOfUtilityClassException();
    }

    public static void addTillBehaviour( Block block, BlockState flattened ) {
        HOE_LOOKUP.put( block, flattened );
    }
}
