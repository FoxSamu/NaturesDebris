/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
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
        LootFunctionManager.registerFunction( new MulCornerCount.Serializer() );
    }
}
