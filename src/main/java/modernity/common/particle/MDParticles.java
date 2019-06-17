/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.particle;

import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.common.registry.MDRegistries;

import java.util.ArrayList;

public class MDParticles {
    private static final ArrayList<ParticleEntry> ENTRIES = new ArrayList<>();

    public static final SimpleParticleType SALT = register( "salt", false );

    public static SimpleParticleType register( String id, boolean alwaysRender ) {
        SimpleParticleType type = new SimpleParticleType( new ResourceLocation( "modernity", id ), alwaysRender );
        ENTRIES.add( new ParticleEntry( type ) );
        return type;
    }

    public static <T extends IParticleData> ParticleType<T> register( ParticleType<T> type ) {
        ENTRIES.add( new ParticleEntry( type ) );
        return type;
    }

    public static void register( IForgeRegistry<ParticleEntry> registry ) {
        for( ParticleEntry e : ENTRIES ) {
            registry.register( e );
        }
    }

    public static void inject() {
        ForgeRegistry<ParticleEntry> entries = MDRegistries.particles();
        for( ParticleEntry entry : entries ) {
            int id = entries.getID( entry );
            ResourceLocation name = entries.getRegistryName();

            IRegistry.PARTICLE_TYPE.register( id, name, entry.type );
        }
    }
}
