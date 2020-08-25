/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator;

import com.google.common.reflect.TypeToken;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.world.gen.ChunkGeneratorType;

/**
 * Holder class for Modernity chunk generators.
 */
//@ObjectHolder( "modernity" )
public final class MDChunkGeneratorTypes {
    private static final RegistryHandler<ChunkGeneratorType<?, ?>> ENTRIES = new RegistryHandler<>("natures/debris", true);

    public static final ChunkGeneratorType<ProceduralGenSettings<?>, ProceduralChunkGenerator> PROCEDURAL = register("procedural", new ChunkGeneratorType<>(ProceduralChunkGenerator::new, false, ProceduralGenSettings::new));

    private MDChunkGeneratorTypes() {
    }

    private static <T extends ChunkGeneratorType<?, ?>> T register(String id, T type) {
        return ENTRIES.register(id, type);
    }

    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<ChunkGeneratorType<?, ?>> token = new TypeToken<ChunkGeneratorType<?, ?>>() {
        };
        handler.addHandler((Class<ChunkGeneratorType<?, ?>>) token.getRawType(), ENTRIES);
    }
}
