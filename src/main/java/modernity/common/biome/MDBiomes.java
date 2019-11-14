/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.EMDDimension;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object holder for Modernity biomes. This class handles all registries according to biomes.
 */
@ObjectHolder( "modernity" )
public final class MDBiomes {
    private static final RegistryHandler<Biome> ENTRIES = new RegistryHandler<>( "modernity" );
    private static final EnumMap<EMDDimension, ArrayList<Entry>> BIOME_LISTS = new EnumMap<>( EMDDimension.class );


    public static final MeadowBiome MEADOW = register( "meadow", new MeadowBiome(), EMDDimension.SURFACE, 1000 );
    public static final LushMeadowBiome LUSH_MEADOW = register( "lush_meadow", new LushMeadowBiome(), EMDDimension.SURFACE, 1000 );
    public static final ForestBiome FOREST = register( "forest", new ForestBiome(), EMDDimension.SURFACE, 1000 );
    public static final RiverBiome RIVER = register( "river", new RiverBiome(), EMDDimension.SURFACE, 0 );
    public static final SwampBiome SWAMP = register( "swamp", new SwampBiome(), EMDDimension.SURFACE, 1000 );
    public static final WaterlandsBiome WATERLANDS = register( "waterlands", new WaterlandsBiome(), EMDDimension.SURFACE, 1000 );

    private static <T extends ModernityBiome> T register( String id, T biome, EMDDimension dimension, int weight, String... aliases ) {
        BIOME_LISTS.computeIfAbsent( dimension, d -> new ArrayList<>() )
                   .add( new Entry( dimension, biome, weight ) );
        return ENTRIES.register( id, biome, aliases );
    }

    /**
     * Called by {@link RegistryEventHandler} to register all {@link RegistryHandler}s.
     */
    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( Biome.class, ENTRIES );
    }

    /**
     * Returns a list of biomes for the specified modernity dimension.
     */
    public static List<ModernityBiome> getBiomesFor( EMDDimension dimen ) {
        return BIOME_LISTS.get( dimen ).stream().map( elem -> elem.biome ).collect( Collectors.toList() );
    }

    /**
     * Creates a generation profile for the speicified modernity dimension. Used in biome layer system.
     */
    public static GenProfile createGenProfile( EMDDimension dimen ) {
        ArrayList<Entry> entries = BIOME_LISTS.computeIfAbsent( dimen, dim -> new ArrayList<>() );
        int[] ids = new int[ entries.size() ];
        int[] wgs = new int[ entries.size() ];
        int twg = 0;
        int i = 0;
        for( Entry e : entries ) {
            int id = Registry.BIOME.getId( e.biome );
            int wg = e.weight;
            twg += wg;
            ids[ i ] = id;
            wgs[ i ] = wg;
            i++;
        }

        return new GenProfile( ids, wgs, twg );
    }

    private MDBiomes() {
    }

    private static class Entry {
        public final EMDDimension dimen;
        public final ModernityBiome biome;
        public final int weight;

        private Entry( EMDDimension dimen, ModernityBiome biome, int weight ) {
            this.dimen = dimen;
            this.biome = biome;
            this.weight = weight;
        }
    }

    public static class GenProfile {
        public final int[] biomeIDs;
        public final int[] weights;
        public final int totalWeight;

        private GenProfile( int[] biomeIDs, int[] weights, int totalWeight ) {
            this.biomeIDs = biomeIDs;
            this.weights = weights;
            this.totalWeight = totalWeight;
        }
    }
}
