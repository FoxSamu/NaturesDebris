/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.loot;

import net.minecraft.block.Block;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

public class MergedBlockDrops implements IBlockDrops {
    private final IBlockLoot[] loots;

    public MergedBlockDrops(IBlockLoot... loots) {
        this.loots = loots;
    }

    @Override
    public LootTable.Builder createLootTable(Block block) {
        LootTable.Builder builder = LootTable.builder();
        int i = 0;
        for(IBlockLoot loot : loots) {
            builder.addLootPool(
                LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .name("pool" + i)
                        .addEntry(loot.createLootEntry(block))
            );
        }
        return builder;
    }
}
