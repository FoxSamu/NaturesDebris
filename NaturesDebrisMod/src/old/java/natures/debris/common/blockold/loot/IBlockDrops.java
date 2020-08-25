/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.loot;

import net.minecraft.block.Block;
import net.minecraft.world.storage.loot.LootTable;

@FunctionalInterface
public interface IBlockDrops {
    LootTable.Builder createLootTable(Block block);
}
