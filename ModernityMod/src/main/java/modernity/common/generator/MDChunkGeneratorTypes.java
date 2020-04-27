/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity chunk generators.
 */
@ObjectHolder( "modernity" )
public final class MDChunkGeneratorTypes {
    private static final RegistryHandler<ChunkGeneratorType<?, ?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final ChunkGeneratorType<ProceduralGenSettings<?>, ProceduralChunkGenerator> PROCEDURAL = register( "procedural", new ChunkGeneratorType<>( ProceduralChunkGenerator::new, false, ProceduralGenSettings::new ) );

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
