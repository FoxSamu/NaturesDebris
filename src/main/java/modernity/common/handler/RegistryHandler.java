/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.entity.MDEntityTypes;
import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItems;
import modernity.common.particle.MDParticles;
import modernity.common.particle.ParticleEntry;
import modernity.common.registry.MDRegistries;
import modernity.common.tileentity.MDTileEntities;
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
    public void registerEntities( RegistryEvent.Register<EntityType<?>> event ) {
        MDEntityTypes.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerTileEntities( RegistryEvent.Register<TileEntityType<?>> event ) {
        MDTileEntities.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerParticles( RegistryEvent.Register<ParticleEntry> event ) {
        MDParticles.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerRegistries( RegistryEvent.NewRegistry event ) {
        MDRegistries.register();
    }

}
