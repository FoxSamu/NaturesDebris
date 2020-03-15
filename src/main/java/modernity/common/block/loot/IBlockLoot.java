/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.loot;

import net.minecraft.block.Block;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

@FunctionalInterface
public interface IBlockLoot extends IBlockDrops {
    LootEntry.Builder<?> createLootEntry( Block block );

    @Override
    default LootTable.Builder createLootTable( Block block ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .rolls( ConstantRange.of( 1 ) )
                    .name( "base" )
                    .addEntry( createLootEntry( block ) )
        );
    }
}
