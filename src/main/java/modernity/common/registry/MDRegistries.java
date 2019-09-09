/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.registry;

import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;
import modernity.common.particle.MDParticles;
import modernity.common.particle.ParticleEntry;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class MDRegistries {

    private static ForgeRegistry<FluidEntry> fluids;
    private static ForgeRegistry<ParticleEntry> particles;

    public static final ResourceLocation FLUID_STATE_LIST = new ResourceLocation( "modernity:fluid_state_list" );

    public static ForgeRegistry<FluidEntry> fluids() {
        return fluids;
    }

    public static ForgeRegistry<ParticleEntry> particles() {
        return particles;
    }

    public static void register() {
        RegistryBuilder<FluidEntry> fluids = new RegistryBuilder<>();
        fluids.setName( new ResourceLocation( "modernity:fluids" ) );
        fluids.setType( FluidEntry.class );
        fluids.setIDRange( 0, Integer.MAX_VALUE );
        fluids.add( (IForgeRegistry.AddCallback<FluidEntry>) ( owner, stage, id, obj, oldObj ) -> MDFluids.inject( id, obj ) );
        fluids.add( (IForgeRegistry.CreateCallback<FluidEntry>) ( owner, stage ) -> owner.setSlaveMap( FLUID_STATE_LIST, new ClearableObjectIntIdentityMap<>() ) );
        fluids.add( (IForgeRegistry.BakeCallback<FluidEntry>) ( owner, stage ) -> MDFluids.calculateStateRegistry( owner.getSlaveMap( FLUID_STATE_LIST, ClearableObjectIntIdentityMap.class ), owner ) );
        MDRegistries.fluids = (ForgeRegistry<FluidEntry>) fluids.create();

        RegistryBuilder<ParticleEntry> particles = new RegistryBuilder<>();
        particles.setName( new ResourceLocation( "modernity:particles" ) );
        particles.setType( ParticleEntry.class );
        particles.setIDRange( 0, Integer.MAX_VALUE );
        particles.add( (IForgeRegistry.AddCallback<ParticleEntry>) ( owner, stage, id, obj, oldObj ) -> MDParticles.inject( id, obj ) );
        MDRegistries.particles = (ForgeRegistry<ParticleEntry>) particles.create();
    }

    public static class ClearableObjectIntIdentityMap <I> extends ObjectIntIdentityMap<I> {
        void clear() {
            this.identityMap.clear();
            this.objectList.clear();
            this.nextId = 0;
        }

        void remove( I key ) {
            Integer prev = this.identityMap.remove( key );
            if( prev != null ) {
                this.objectList.set( prev, null );
            }
        }
    }
}
