/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;
import modernity.common.particle.MDParticles;
import modernity.common.particle.ParticleEntry;

public class MDRegistries {

    private static ForgeRegistry<FluidEntry> fluids;
    private static ForgeRegistry<ParticleEntry> particles;

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
        fluids.setIDRange( 5, Integer.MAX_VALUE );
        // TODO: Use AddCallback here: Bake is not used on synchronization, causing problems with multiple mods over LAN servers...
        fluids.add( (IForgeRegistry.BakeCallback<FluidEntry>) ( owner, stage ) -> MDFluids.inject() );
        MDRegistries.fluids = (ForgeRegistry<FluidEntry>) fluids.create();

        RegistryBuilder<ParticleEntry> particles = new RegistryBuilder<>();
        particles.setName( new ResourceLocation( "modernity:particles" ) );
        particles.setType( ParticleEntry.class );
        particles.setIDRange( 0, Integer.MAX_VALUE );
        particles.add( (IForgeRegistry.AddCallback<ParticleEntry>) ( owner, stage, id, obj, oldObj ) -> MDParticles.inject( id, obj ) );
        MDRegistries.particles = (ForgeRegistry<ParticleEntry>) particles.create();
    }
}
