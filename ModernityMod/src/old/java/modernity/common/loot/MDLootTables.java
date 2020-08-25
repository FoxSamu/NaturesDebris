/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.loot;

import modernity.common.loot.func.MulCornerCount;
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
