/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.loot;

import natures.debris.common.loot.func.MulCornerCount;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

/**
 * Class that handles all registring that has something to do with loot tables.
 */
public final class MDLootTables {
    private MDLootTables() {
    }

    public static void register() {
        LootFunctionManager.registerFunction(new MulCornerCount.Serializer());
    }
}
