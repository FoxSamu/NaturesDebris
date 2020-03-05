/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 05 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.MDDimension;
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
    private static final EnumMap<MDDimension, ArrayList<Entry>> BIOME_LISTS = new EnumMap<>( MDDimension.class );


    public static final MeadowBiome MEADOW = register( "meadow", new MeadowBiome( MeadowBiome.Type.MEADOW ), MDDimension.MURK_SURFACE );
    public static final MeadowBiome HIGH_MEADOW = register( "high_meadow", new MeadowBiome( MeadowBiome.Type.HIGH_MEADOW ), MDDimension.MURK_SURFACE );
    public static final MeadowBiome MEADOW_NO_TREES = register( "meadow_no_trees", new MeadowBiome( MeadowBiome.Type.MEADOW_NO_TREES ), MDDimension.MURK_SURFACE );

    public static final FlowerMeadowBiome FLOWER_MEADOW = register( "flower_meadow", new FlowerMeadowBiome( FlowerMeadowBiome.Type.FLOWER_MEADOW ), MDDimension.MURK_SURFACE );
    public static final FlowerMeadowBiome HIGH_FLOWER_MEADOW = register( "high_flower_meadow", new FlowerMeadowBiome( FlowerMeadowBiome.Type.HIGH_FLOWER_MEADOW ), MDDimension.MURK_SURFACE );
    public static final FlowerMeadowBiome FLOWER_MEADOW_NO_TREES = register( "flower_meadow_no_trees", new FlowerMeadowBiome( FlowerMeadowBiome.Type.FLOWER_MEADOW_NO_TREES ), MDDimension.MURK_SURFACE );

    public static final LushGrasslandBiome LUSH_GRASSLAND = register( "lush_grassland", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome LUSH_GRASSLAND_EDGE = register( "lush_grassland_edge", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND_EDGE ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome LUSH_GRASSLAND_OPEN = register( "lush_grassland_open", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND_OPEN ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome HIGH_LUSH_GRASSLAND = register( "high_lush_grassland", new LushGrasslandBiome( LushGrasslandBiome.Type.HIGH_LUSH_GRASSLAND ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome HIGH_LUSH_GRASSLAND_OPEN = register( "high_lush_grassland_open", new LushGrasslandBiome( LushGrasslandBiome.Type.HIGH_LUSH_GRASSLAND_OPEN ), MDDimension.MURK_SURFACE );

    public static final ForestBiome FOREST = register( "forest", new ForestBiome( ForestBiome.Type.FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome OPEN_FOREST = register( "open_forest", new ForestBiome( ForestBiome.Type.OPEN_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome BUSH_FOREST = register( "bush_forest", new ForestBiome( ForestBiome.Type.BUSH_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome HIGH_FOREST = register( "high_forest", new ForestBiome( ForestBiome.Type.HIGH_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome HIGH_OPEN_FOREST = register( "high_open_forest", new ForestBiome( ForestBiome.Type.HIGH_OPEN_FOREST ), MDDimension.MURK_SURFACE );

    public static final RiverBiome RIVER = register( "river", new RiverBiome( RiverBiome.Type.RIVER ), MDDimension.MURK_SURFACE );

    public static final SwampBiome SWAMP = register( "swamp", new SwampBiome( SwampBiome.Type.SWAMP ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_HILLS = register( "swamp_hills", new SwampBiome( SwampBiome.Type.SWAMP_HILLS ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_WATER = register( "swamp_water", new SwampBiome( SwampBiome.Type.SWAMP_WATER ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_MARSHES = register( "swamp_marshes", new SwampBiome( SwampBiome.Type.SWAMP_MARSHES ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_LAND = register( "swamp_land", new SwampBiome( SwampBiome.Type.SWAMP_LAND ), MDDimension.MURK_SURFACE );

    public static final WaterlandsBiome WATERLANDS = register( "waterlands", new WaterlandsBiome( WaterlandsBiome.Type.WATERLANDS ), MDDimension.MURK_SURFACE );




    public static final ModernityBiome DEFAULT = MEADOW;


    private static <T extends ModernityBiome> T register( String id, T biome, MDDimension dimension, int weight, String... aliases ) {
        BIOME_LISTS.computeIfAbsent( dimension, d -> new ArrayList<>() )
                   .add( new Entry( dimension, biome, weight ) );
        return ENTRIES.register( id, biome, aliases );
    }

    private static <T extends ModernityBiome> T register( String id, T biome, MDDimension dimension, String... aliases ) {
        BIOME_LISTS.computeIfAbsent( dimension, d -> new ArrayList<>() )
                   .add( new Entry( dimension, biome, 0 ) );
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
    public static List<ModernityBiome> getBiomesFor( MDDimension dimen ) {
        return BIOME_LISTS.get( dimen ).stream().map( elem -> elem.biome ).collect( Collectors.toList() );
    }

    /**
     * Creates a generation profile for the speicified modernity dimension. Used in biome layer system.
     */
    public static GenProfile createGenProfile( MDDimension dimen ) {
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
        public final MDDimension dimen;
        public final ModernityBiome biome;
        public final int weight;

        private Entry( MDDimension dimen, ModernityBiome biome, int weight ) {
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
