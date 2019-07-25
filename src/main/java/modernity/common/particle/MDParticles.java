/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.particle;

import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.api.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MDParticles {
    private static final ArrayList<Pair<ResourceLocation, ParticleType<?>>> ENTRIES = new ArrayList<>();
    private static final List<ParticleEntry> VANILLA_ENTRIES = IRegistry.PARTICLE_TYPE.stream()
                                                                                      .map( ParticleEntry::new )
                                                                                      .collect( Collectors.toList() );

    public static final SimpleParticleType SALT = register( "salt", false );
    public static final SimpleParticleType MODERNIZED_WATER_DRIP = register( "modernized_water_drip", false );
    public static final SimpleParticleType PORTAL_DRIP = register( "portal_drip", false );

    public static SimpleParticleType register( String id, boolean alwaysRender ) {
        SimpleParticleType type = new SimpleParticleType( new ResourceLocation( "modernity", id ), alwaysRender );
        ENTRIES.add( new Pair<>( type.getId(), type ) );
        return type;
    }

    public static <T extends IParticleData> ParticleType<T> register( ParticleType<T> type ) {
        ENTRIES.add( new Pair<>( type.getId(), type ) );
        return type;
    }

    public static void register( IForgeRegistry<ParticleEntry> registry ) {

        for( ParticleEntry e : VANILLA_ENTRIES ) {
            registry.register( e );
        }
        for( Pair<ResourceLocation, ParticleType<?>> e : ENTRIES ) {
            registry.register( new ParticleEntry( e.getRight() ) );
        }
    }

    public static void inject( int id, ParticleEntry obj ) {
        System.out.println( "Injecting particle data for ID " + id + ": " + obj.getRegistryName() );
        IRegistry.PARTICLE_TYPE.register( id, Objects.requireNonNull( obj.getRegistryName() ), obj.type );
    }
}
