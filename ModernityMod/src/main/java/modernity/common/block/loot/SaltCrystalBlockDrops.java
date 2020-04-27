/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.loot;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.RandomChance;

public class SaltCrystalBlockDrops extends SaltBlockDrops {

    public SaltCrystalBlockDrops() {
        super( 1, 4, 3, 1, 3 );
    }

    @Override
    public LootTable.Builder createLootTable( Block block ) {
        LootPool.Builder saltLoot = createSaltLootPool( block )
                                        .acceptCondition( NO_SILK_TOUCH )
                                        .acceptCondition(
                                            BlockLoot.stateProp( block, MDBlockStateProperties.AGE_0_11, 11 )
                                        );
        LootPool.Builder silkLoot =
            LootPool.builder()
                    .name( "silk_touch" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry( ItemLootEntry.builder( block ) )
                    .acceptCondition( SILK_TOUCH );
        LootPool.Builder youngLoot =
            LootPool.builder()
                    .name( "young" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( MDItems.SALT_DUST )
                                     .acceptCondition( RandomChance.builder( 0.05F ) )
                    )
                    .acceptCondition( NO_SILK_TOUCH )
                    .acceptCondition(
                        BlockLoot.stateProp( block, MDBlockStateProperties.AGE_0_11, 11 )
                                 .inverted()
                    );

        return LootTable.builder().addLootPool( saltLoot ).addLootPool( silkLoot ).addLootPool( youngLoot );
    }
}
