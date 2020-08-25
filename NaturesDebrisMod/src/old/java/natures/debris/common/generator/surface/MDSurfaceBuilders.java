/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.surface;

import com.google.common.reflect.TypeToken;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

/**
 * Holder class for Modernity surface bulders. This is only one that wraps {@link ISurfaceGenerator}s.
 */
//@ObjectHolder( "modernity" )
public final class MDSurfaceBuilders {
    private static final RegistryHandler<SurfaceBuilder<?>> ENTRIES = new RegistryHandler<>("natures/debris", true);

    public static final WrapperSurfaceBuilder WRAPPER = register("wrapper", new WrapperSurfaceBuilder());

    private MDSurfaceBuilders() {
    }

    private static <T extends SurfaceBuilder<?>> T register(String id, T builder) {
        return ENTRIES.register(id, builder);
    }

    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<SurfaceBuilder<?>> token = new TypeToken<SurfaceBuilder<?>>() {
        };
        handler.addHandler((Class<SurfaceBuilder<?>>) token.getRawType(), ENTRIES);
    }
}
