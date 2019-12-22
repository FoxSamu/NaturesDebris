/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.surface;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity surface bulders. This is only one that wraps {@link ISurfaceGenerator}s.
 */
@ObjectHolder( "modernity" )
public final class MDSurfaceBuilders {
    private static final RegistryHandler<SurfaceBuilder<?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final WrapperSurfaceBuilder WRAPPER = register( "wrapper", new WrapperSurfaceBuilder() );

    private static <T extends SurfaceBuilder<?>> T register( String id, T builder ) {
        return ENTRIES.register( id, builder );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<SurfaceBuilder<?>> token = new TypeToken<SurfaceBuilder<?>>() {
        };
        handler.addHandler( (Class<SurfaceBuilder<?>>) token.getRawType(), ENTRIES );
    }

    private MDSurfaceBuilders() {
    }
}
