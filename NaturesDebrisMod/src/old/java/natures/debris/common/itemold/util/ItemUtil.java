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

public final class ItemUtil {
    private ItemUtil() {
        throw new InstanceOfUtilityClassException();
    }

    public static void addHoeTillBehaviour(Block from, BlockState to) {
        HoeProtectedAccess.addTillBehaviour(from, to);
    }

    public static void addShovelFlattenBehaviour(Block from, BlockState to) {
        ShovelProtectedAccess.addFlattenBehaviour(from, to);
    }
}
