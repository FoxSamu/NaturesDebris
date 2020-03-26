/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.loot;

import modernity.common.block.MDNatureBlocks;
import modernity.common.block.farmland.FarmlandBlock;
import net.minecraft.world.storage.loot.ItemLootEntry;

import static modernity.common.block.loot.BlockLoot.*;

public final class MDBlockDrops {
    public static final IBlockLoot SIMPLE = self();
    public static final IBlockDrops DIRT_LIKE = silkTouch( SIMPLE, item( () -> MDNatureBlocks.MURKY_DIRT ) );
    public static final IBlockDrops DIRT = item( () -> MDNatureBlocks.MURKY_DIRT );
    public static final IBlockDrops FARMLAND = silkTouch(
        block -> ItemLootEntry.builder( ( (FarmlandBlock) block ).getLogic().getDirtVariant().getBlock() ),
        item( () -> MDNatureBlocks.MURKY_DIRT )
    );

    private MDBlockDrops() {
    }
}
