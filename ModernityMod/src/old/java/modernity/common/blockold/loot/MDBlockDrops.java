/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.loot;

import modernity.common.blockold.MDNatureBlocks;
import modernity.common.blockold.farmland.FarmlandBlock;
import net.minecraft.world.storage.loot.ItemLootEntry;

import static modernity.common.blockold.loot.BlockLoot.*;

public final class MDBlockDrops {
    public static final IBlockLoot SIMPLE = self();
    public static final IBlockDrops DIRT_LIKE = silkTouch(SIMPLE, item(() -> MDNatureBlocks.MURKY_DIRT));
    public static final IBlockDrops DIRT = item(() -> MDNatureBlocks.MURKY_DIRT);
    public static final IBlockDrops FARMLAND = silkTouch(
        block -> ItemLootEntry.builder(((FarmlandBlock) block).getLogic().getDirtVariant().getBlock()),
        item(() -> MDNatureBlocks.MURKY_DIRT)
    );

    private MDBlockDrops() {
    }
}
