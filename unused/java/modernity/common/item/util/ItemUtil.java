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
import net.redgalaxy.exc.InstanceOfUtilityClassException;

public final class ItemUtil {
    private ItemUtil() {
        throw new InstanceOfUtilityClassException();
    }

    public static void addHoeTillBehaviour( Block from, BlockState to ) {
        HoeProtectedAccess.addTillBehaviour( from, to );
    }

    public static void addShovelFlattenBehaviour( Block from, BlockState to ) {
        ShovelProtectedAccess.addFlattenBehaviour( from, to );
    }
}
