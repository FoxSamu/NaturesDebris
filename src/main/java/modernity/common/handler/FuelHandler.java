/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 3 - 2019
 */

package modernity.common.handler;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

import static modernity.common.block.MDBlocks.*;
import static modernity.common.item.MDItemTags.*;
import static modernity.common.item.MDItems.*;

public class FuelHandler {
    private static final ThreadLocal<Map<Item, Integer>> FUEL_TICKS = ThreadLocal.withInitial( () -> {
        Map<Item, Integer> map = Maps.newHashMap();
        setBurnTime( map, 20000, ALUMINIUM_HEATROCK_BUCKET );
        setBurnTime( map, 16000, ALUMINIUM_OIL_BUCKET );
        setBurnTime( map, 16000, ANTHRACITE_BLOCK );
        setBurnTime( map, 1600, ANTHRACITE );
        setBurnTime( map, 300, LOGS );
        setBurnTime( map, 300, PLANKS );
        setBurnTime( map, 150, WOODEN_SLABS );
        setBurnTime( map, 300, WOODEN_STAIRS );
        setBurnTime( map, 150, WOODEN_STEPS );
        setBurnTime( map, 100, WOODEN_CORNERS );
        setBurnTime( map, 300, BLACKWOOD_FENCE );
        setBurnTime( map, 300, BLACKWOOD_FENCE_GATE );
        setBurnTime( map, 300, INVER_FENCE );
        setBurnTime( map, 300, INVER_FENCE_GATE );
        setBurnTime( map, 100, STICKS );
        setBurnTime( map, 100, BLACKWOOD_SAPLING );
        setBurnTime( map, 100, INVER_SAPLING );
        setBurnTime( map, 100, ANTHRACITE_TORCH );
        setBurnTime( map, 100, EXTINGUISHED_ANTHRACITE_TORCH );

        // Special case for torches:
        // Torches are objects made from two fuels, albeit burning: they should be a fuel on itself
        setBurnTime( map, 100, Blocks.TORCH );
        setBurnTime( map, 100, Blocks.WALL_TORCH );
        setBurnTime( map, 100, Blocks.REDSTONE_TORCH );
        setBurnTime( map, 100, Blocks.REDSTONE_WALL_TORCH );
        return map;
    } );

    private static void setBurnTime( Map<Item, Integer> map, int ticks, IItemProvider... providers ) {
        for( IItemProvider provider : providers )
            map.put( provider.asItem(), ticks );
    }

    private static void setBurnTime( Map<Item, Integer> map, int ticks, Tag<Item> tag ) {
        for( Item i : tag.getAllElements() )
            map.put( i, ticks );
    }

    @SubscribeEvent
    public void onFuelTime( FurnaceFuelBurnTimeEvent event ) {
        Map<Item, Integer> fuelMap = FUEL_TICKS.get();
        if( fuelMap.containsKey( event.getItemStack().getItem() ) ) {
            event.setBurnTime( fuelMap.get( event.getItemStack().getItem() ) );
        }
    }
}
