/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.itemold.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemTier;
import modernity.api.util.exc.InstanceOfUtilityClassException;

class HoeProtectedAccess extends HoeItem {
    private HoeProtectedAccess() {
        super(ItemTier.DIAMOND, 0, new Properties());
        throw new InstanceOfUtilityClassException();
    }

    public static void addTillBehaviour(Block block, BlockState flattened) {
        HOE_LOOKUP.put(block, flattened);
    }
}
