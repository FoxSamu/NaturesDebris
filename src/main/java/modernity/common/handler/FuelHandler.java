/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.handler;

import modernity.common.block.MDBlocks;
import modernity.common.item.MDItemTags;
import modernity.common.item.MDItems;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum FuelHandler {
    INSTANCE;

    @SubscribeEvent
    public void onComputeFurnaceFuelBurnTime( FurnaceFuelBurnTimeEvent event ) {
        burnTime( event, MDItems.ALUMINIUM_LAVA_BUCKET, 20000 );
        burnTime( event, MDBlocks.ANTHRACITE_BLOCK, 16000 );
        burnTime( event, MDItems.ANTHRACITE, 1600 );
        burnTime( event, MDItemTags.LOGS, 300 );
        burnTime( event, MDItemTags.PLANKS, 300 );
        burnTime( event, MDBlocks.BLACKWOOD_STAIRS, 300 );
        burnTime( event, MDBlocks.INVER_STAIRS, 300 );
        burnTime( event, MDBlocks.BLACKWOOD_SLAB, 150 );
        burnTime( event, MDBlocks.INVER_SLAB, 150 );
        burnTime( event, MDBlocks.BLACKWOOD_FENCE, 300 );
        burnTime( event, MDBlocks.INVER_FENCE, 300 );
        burnTime( event, MDBlocks.BLACKWOOD_FENCE_GATE, 300 );
        burnTime( event, MDBlocks.INVER_FENCE_GATE, 300 );
        burnTime( event, MDBlocks.BLACKWOOD_WORKBENCH, 300 );
        burnTime( event, MDBlocks.INVER_WORKBENCH, 300 );
        burnTime( event, MDBlocks.INVER_WORKBENCH, 300 );
        burnTime( event, MDItems.BLACKWOOD_SHOVEL, 200 );
        burnTime( event, MDItems.BLACKWOOD_SWORD, 200 );
        burnTime( event, MDItems.BLACKWOOD_PICKAXE, 200 );
        burnTime( event, MDItems.BLACKWOOD_AXE, 200 );
        burnTime( event, MDBlocks.BLACKWOOD_DOOR, 200 );
        burnTime( event, MDBlocks.INVER_DOOR, 200 );
        burnTime( event, MDItems.BLACKWOOD_STICK, 100 );
        burnTime( event, MDItems.INVER_STICK, 100 );
    }



    private static void burnTime( FurnaceFuelBurnTimeEvent event, Tag<Item> itemTag, int burnTime ) {
        if( event.getItemStack().getItem().isIn( itemTag ) ) {
            event.setBurnTime( burnTime );
        }
    }

    private static void burnTime( FurnaceFuelBurnTimeEvent event, IItemProvider itemProvider, int burnTime ) {
        if( event.getItemStack().getItem() == itemProvider.asItem() ) {
            event.setBurnTime( burnTime );
        }
    }
}
