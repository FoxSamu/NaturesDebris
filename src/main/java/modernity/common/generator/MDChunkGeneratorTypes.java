/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator;

import com.google.common.reflect.TypeToken;
import modernity.common.generator.map.surface.SurfaceGenerator;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity chunk generators.
 */
@ObjectHolder( "modernity" )
public final class MDChunkGeneratorTypes {
    private static final RegistryHandler<ChunkGeneratorType<?, ?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final ChunkGeneratorType<GenerationSettings, SurfaceGenerator> SURFACE = register( "surface", new ChunkGeneratorType<>( new SurfaceGenerator.Factory(), false, GenerationSettings::new ) );
    public static final ChunkGeneratorType<MapGenSettings<?>, MapChunkGenerator> MAPGEN = register( "mapgen", new ChunkGeneratorType<>( MapChunkGenerator::new, false, MapGenSettings::new ) );

    private static <T extends ChunkGeneratorType<?, ?>> T register( String id, T type ) {
        return ENTRIES.register( id, type );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<ChunkGeneratorType<?, ?>> token = new TypeToken<ChunkGeneratorType<?, ?>>() {
        };
        handler.addHandler( (Class<ChunkGeneratorType<?, ?>>) token.getRawType(), ENTRIES );
    }

    private MDChunkGeneratorTypes() {
    }
}
