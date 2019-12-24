/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.biome;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity biome provider types.
 */
@ObjectHolder( "modernity" )
public final class MDBiomeProviderTypes {
    private static final RegistryHandler<BiomeProviderType<?, ?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    private static <T extends BiomeProviderType<?, ?>> T register( String id, T type ) {
        return ENTRIES.register( id, type );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BiomeProviderType<?, ?>> token = new TypeToken<BiomeProviderType<?, ?>>() {
        };
        handler.addHandler( (Class<BiomeProviderType<?, ?>>) token.getRawType(), ENTRIES );
    }

    private MDBiomeProviderTypes() {
    }
}
