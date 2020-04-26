/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.data.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import modernity.common.block.loot.IBlockDrops;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockLootData implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private final Map<ResourceLocation, LootTable.Builder> lootTables = Maps.newHashMap();
    private static final Map<Block, IBlockDrops> BLOCK_MAP = Maps.newHashMap();

    protected void addTables() {
        BLOCK_MAP.forEach( ( key, value ) -> add(
            key,
            value.createLootTable( key )
        ) );
    }

    protected void add( Block block, Function<Block, LootTable.Builder> func ) {
        add( block, func.apply( block ) );
    }

    protected void add( Block block, LootTable.Builder builder ) {
        lootTables.put( block.getLootTable(), builder );
    }

    @Override
    public void accept( BiConsumer<ResourceLocation, LootTable.Builder> consumer ) {
        addTables();
        Set<ResourceLocation> locations = Sets.newHashSet();

        for( Block block : getKnownBlocks() ) {
            if( Objects.requireNonNull( block.getRegistryName() ).getNamespace().equals( "modernity" ) ) {
                ResourceLocation lt = block.getLootTable();
                if( lt != LootTables.EMPTY && locations.add( lt ) ) {
                    LootTable.Builder builder = lootTables.remove( lt );
                    if( builder != null ) {
                        consumer.accept( lt, builder );
                    }
                }
            }
        }

        if( ! lootTables.isEmpty() ) {
            throw new IllegalStateException( "Created block loot tables for non-blocks: " + lootTables.keySet() );
        }
    }

    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS;
    }

    public static void addBlock( Block block, IBlockDrops loot ) {
        BLOCK_MAP.put( block, loot );
    }
}