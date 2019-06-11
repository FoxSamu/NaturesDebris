/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItems;
import modernity.common.registry.MDRegistries;
import modernity.common.world.dim.MDDimensions;

public class RegistryHandler {

    @SubscribeEvent
    public void registerBlocks( RegistryEvent.Register<Block> event ) {
        MDBlocks.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerItems( RegistryEvent.Register<Item> event ) {
        MDBlocks.registerItems( event.getRegistry() );
        MDItems.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerBiomes( RegistryEvent.Register<Biome> event ) {
        MDBiomes.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerFluids( RegistryEvent.Register<FluidEntry> event ) {
        MDFluids.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerDimensions( RegistryEvent.Register<ModDimension> event ) {
        MDDimensions.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerRegistries( RegistryEvent.NewRegistry event ) {
        MDRegistries.register();
    }

}
