/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 7 - 2019
 */

package modernity.common.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class MDLootTables {
    public static final ResourceLocation CURSE_RUIN_FURNACE_FUEL = register( "curse_ruin/furnace_fuel" );
    public static final ResourceLocation CURSE_RUIN_DEFAULT_CHEST = register( "curse_ruin/default_chest" );

    public static void load() {
        // Just for loading class
    }

    private static ResourceLocation register( String id ) {
        return LootTableList.register( new ResourceLocation( "modernity:" + id ) );
    }
}
