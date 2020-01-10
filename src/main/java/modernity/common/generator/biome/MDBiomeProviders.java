/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
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
public final class MDBiomeProviders {
    private static final RegistryHandler<BiomeProviderType<?, ?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final BiomeProviderType<LayerBiomeProviderSettings, LayerBiomeProvider> LAYERED
        = register( "layered", new BiomeProviderType<>( LayerBiomeProvider::new, LayerBiomeProviderSettings::new ) );

    private static <T extends BiomeProviderType<?, ?>> T register( String id, T type ) {
        return ENTRIES.register( id, type );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BiomeProviderType<?, ?>> token = new TypeToken<BiomeProviderType<?, ?>>() {
        };
        handler.addHandler( (Class<BiomeProviderType<?, ?>>) token.getRawType(), ENTRIES );
    }

    private MDBiomeProviders() {
    }
}
