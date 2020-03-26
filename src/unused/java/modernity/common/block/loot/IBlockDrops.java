/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.loot;

import net.minecraft.block.Block;
import net.minecraft.world.storage.loot.LootTable;

@FunctionalInterface
public interface IBlockDrops {
    LootTable.Builder createLootTable( Block block );
}
