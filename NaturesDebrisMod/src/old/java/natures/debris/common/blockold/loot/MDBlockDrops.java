/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.loot;

import natures.debris.common.blockold.MDNatureBlocks;
import natures.debris.common.blockold.farmland.FarmlandBlock;
import net.minecraft.world.storage.loot.ItemLootEntry;

public final class MDBlockDrops {
    public static final IBlockLoot SIMPLE = BlockLoot.self();
    public static final IBlockDrops DIRT_LIKE = BlockLoot.silkTouch(SIMPLE, BlockLoot.item(() -> MDNatureBlocks.MURKY_DIRT));
    public static final IBlockDrops DIRT = BlockLoot.item(() -> MDNatureBlocks.MURKY_DIRT);
    public static final IBlockDrops FARMLAND = BlockLoot.silkTouch(
        block -> ItemLootEntry.builder(((FarmlandBlock) block).getLogic().getDirtVariant().getBlock()),
        BlockLoot.item(() -> MDNatureBlocks.MURKY_DIRT)
    );

    private MDBlockDrops() {
    }
}
