/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.world.gen.placement;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDPlacements {
    private static final RegistryHandler<Placement<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final InCave CAVE = register( "cave", new InCave() );
    public static final InCave.WithChance CHANCE_CAVE = register( "chance_cave", new InCave.WithChance() );
    public static final InCave.WithFrequency COUNT_CAVE = register( "count_cave", new InCave.WithFrequency() );
    public static final AtSurfaceBelowHeight LIMITED_HEIGHTMAP = register( "limited_heightmap", new AtSurfaceBelowHeight() );

    private static <T extends Placement<?>> T register( String id, T placement ) {
        return ENTRIES.register( id, placement );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<Placement<?>> token = new TypeToken<Placement<?>>() {
        };
        handler.addHandler( (Class<Placement<?>>) token.getRawType(), ENTRIES );
    }

    private MDPlacements() {
    }
}
