/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.structure;

import com.google.common.reflect.TypeToken;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;

/**
 * Holder class for Modernity structures.
 */
//@ObjectHolder( "modernity" )
public final class MDStructures {
    private static final RegistryHandler<Feature<?>> STRUCTURES = new RegistryHandler<>("natures/debris");

    public static final ForestRunesStructure FOREST_RUNES = register("forest_runes", new ForestRunesStructure());

    private MDStructures() {
    }

    private static <T extends Structure<?>> T register(String id, T structure, String... aliases) {
        return STRUCTURES.register(id, structure, aliases);
    }

    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<Feature<?>> token = new TypeToken<Feature<?>>() {
        };
        handler.addHandler((Class<Feature<?>>) token.getRawType(), STRUCTURES);
    }
}
