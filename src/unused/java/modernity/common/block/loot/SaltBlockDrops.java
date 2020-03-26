/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.loot;

import modernity.common.block.MDPlantBlocks;
import modernity.common.item.MDItems;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.functions.ApplyBonus;

public class SaltBlockDrops implements IBlockDrops {
    protected static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder( ItemPredicate.Builder.create().enchantment( new EnchantmentPredicate( Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast( 1 ) ) ) );
    protected static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();

    private final int crystalWeight;
    private final int dustWeight;
    private final int nuggetWeight;
    private final int minDrops;
    private final int maxDrops;

    public SaltBlockDrops( int crystalWeight, int dustWeight, int nuggetWeight, int minDrops, int maxDrops ) {
        this.crystalWeight = crystalWeight;
        this.dustWeight = dustWeight;
        this.nuggetWeight = nuggetWeight;
        this.minDrops = minDrops;
        this.maxDrops = maxDrops;
    }

    protected LootPool.Builder createSaltLootPool( Block block ) {
        LootPool.Builder builder = LootPool.builder();
        builder.name( "base" )
               .rolls( maxDrops == minDrops
                       ? ConstantRange.of( minDrops )
                       : RandomValueRange.func_215837_a( minDrops, maxDrops ) )
               .acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, 0.4F, 0 ) );

        if( crystalWeight > 0 ) {
            builder.addEntry( ItemLootEntry.builder( MDPlantBlocks.SALT_CRYSTAL ).weight( crystalWeight ) );
        }

        if( dustWeight > 0 ) {
            builder.addEntry( ItemLootEntry.builder( MDItems.SALT_DUST ).weight( dustWeight ) );
        }

        if( nuggetWeight > 0 ) {
            builder.addEntry( ItemLootEntry.builder( MDItems.SALT_NUGGET ).weight( nuggetWeight ) );
        }
        return builder;
    }

    @Override
    public LootTable.Builder createLootTable( Block block ) {
        return LootTable.builder()
                        .addLootPool( createSaltLootPool( block ).acceptCondition( NO_SILK_TOUCH ) )
                        .addLootPool(
                            LootPool.builder()
                                    .name( "silk_touch" )
                                    .rolls( ConstantRange.of( 1 ) )
                                    .addEntry( ItemLootEntry.builder( block ) )
                                    .acceptCondition( SILK_TOUCH )
                        );
    }
}
