/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.loot;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.SetCount;

import java.util.function.Supplier;

public class CropBlockDrops<T extends Comparable<T>> implements IBlockDrops {
    private final Supplier<Item> item;
    private final Supplier<Item> seeds;
    private final IProperty<T> prop;
    private final T value;

    public CropBlockDrops(Supplier<Item> item, Supplier<Item> seeds, IProperty<T> prop, T value) {
        this.item = item;
        this.seeds = seeds;
        this.prop = prop;
        this.value = value;
    }

    @Override
    public LootTable.Builder createLootTable(Block block) {
        return LootTable.builder()
                        .addLootPool(
                            LootPool.builder()
                                    .name("seeds")
                                    .rolls(ConstantRange.of(1))
                                    .addEntry(
                                        ItemLootEntry
                                            .builder(seeds.get())
                                            .acceptFunction(
                                                SetCount.builder(RandomValueRange.of(1, 3))
                                                        .acceptCondition(BlockLoot.stateProp(block, prop, value))
                                            )
                                    )
                        )
                        .addLootPool(
                            LootPool.builder()
                                    .name("drops")
                                    .rolls(ConstantRange.of(1))
                                    .addEntry(
                                        ItemLootEntry
                                            .builder(item.get())
                                            .acceptFunction(
                                                SetCount.builder(RandomValueRange.of(1, 3))
                                            )
                                    )
                                    .acceptCondition(BlockLoot.stateProp(block, prop, value))
                        );
    }
}
