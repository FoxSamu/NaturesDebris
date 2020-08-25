/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.loot;

import modernity.util.Lazy;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.conditions.TableBonus;

import java.util.function.Supplier;

public class LeavesBlockDrops implements IBlockDrops {
    protected static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
    protected static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();

    private final Lazy<IItemProvider> sapling;
    private final Lazy<IItemProvider> stick;
    private final float[] saplingFortuneTable;

    public LeavesBlockDrops(Supplier<IItemProvider> sapling, Supplier<IItemProvider> stick, float... saplingFortuneTable) {
        this.sapling = Lazy.of(sapling);
        this.stick = Lazy.of(stick);
        this.saplingFortuneTable = saplingFortuneTable;
    }

    protected LootPool.Builder createSaplingLootPool(Block block) {
        LootPool.Builder builder = LootPool.builder();
        builder.name("base")
               .rolls(ConstantRange.of(1))
               .addEntry(
                   ItemLootEntry.builder(sapling.get())
                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, saplingFortuneTable))
               );
        return builder;
    }

    protected LootPool.Builder createStickLootPool(Block block) {
        LootPool.Builder builder = LootPool.builder();
        builder.name("stick")
               .rolls(ConstantRange.of(1))
               .addEntry(
                   ItemLootEntry.builder(stick.get())
                                .acceptCondition(TableBonus.builder(
                                    Enchantments.FORTUNE,
                                    0.02F,
                                    0.022222223F,
                                    0.025F,
                                    0.033333335F,
                                    0.1F
                                ))
               );
        return builder;
    }

    @Override
    public LootTable.Builder createLootTable(Block block) {
        return LootTable.builder()
                        .addLootPool(createSaplingLootPool(block).acceptCondition(NO_SILK_TOUCH))
                        .addLootPool(createStickLootPool(block).acceptCondition(NO_SILK_TOUCH))
                        .addLootPool(
                            LootPool.builder()
                                    .name("silk_touch")
                                    .rolls(ConstantRange.of(1))
                                    .addEntry(ItemLootEntry.builder(block))
                                    .acceptCondition(SILK_TOUCH)
                        );
    }
}
