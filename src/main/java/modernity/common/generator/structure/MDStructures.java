/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.structure;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity structures.
 */
@ObjectHolder( "modernity" )
public final class MDStructures {
    private static final RegistryHandler<Feature<?>> STRUCTURES = new RegistryHandler<>( "modernity" );

    public static final CaveStructure CAVE = register( "cave", new CaveStructure() );
    public static final ForestRunesStructure FOREST_RUNES = register( "forest_runes", new ForestRunesStructure() );

    private static <T extends Structure<?>> T register( String id, T structure, String... aliases ) {
        return STRUCTURES.register( id, structure, aliases );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<Feature<?>> token = new TypeToken<Feature<?>>() {
        };
        handler.addHandler( (Class<Feature<?>>) token.getRawType(), STRUCTURES );
    }

    private MDStructures() {
    }
}
