/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.itemold.util;

import modernity.api.util.exc.InstanceOfUtilityClassException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ShovelItem;

class ShovelProtectedAccess extends ShovelItem {
    private ShovelProtectedAccess() {
        super(ItemTier.DIAMOND, 0, 0, new Properties());
        throw new InstanceOfUtilityClassException();
    }

    public static void addFlattenBehaviour(Block block, BlockState flattened) {
        SHOVEL_LOOKUP.put(block, flattened);
    }
}
