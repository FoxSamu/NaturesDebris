/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.biome;

import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import modernity.generic.util.MDDimension;
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
    public static final MeadowBiome MEADOW_RIVER = register( "meadow_river", new MeadowBiome( MeadowBiome.Type.MEADOW_RIVER ), MDDimension.MURK_SURFACE, "river" );

    public static final FlowerMeadowBiome FLOWER_MEADOW = register( "flower_meadow", new FlowerMeadowBiome( FlowerMeadowBiome.Type.FLOWER_MEADOW ), MDDimension.MURK_SURFACE );
    public static final FlowerMeadowBiome HIGH_FLOWER_MEADOW = register( "high_flower_meadow", new FlowerMeadowBiome( FlowerMeadowBiome.Type.HIGH_FLOWER_MEADOW ), MDDimension.MURK_SURFACE );
    public static final FlowerMeadowBiome FLOWER_MEADOW_NO_TREES = register( "flower_meadow_no_trees", new FlowerMeadowBiome( FlowerMeadowBiome.Type.FLOWER_MEADOW_NO_TREES ), MDDimension.MURK_SURFACE );
    public static final FlowerMeadowBiome FLOWER_MEADOW_RIVER = register( "flower_meadow_river", new FlowerMeadowBiome( FlowerMeadowBiome.Type.FLOWER_MEADOW_RIVER ), MDDimension.MURK_SURFACE );

    public static final LushGrasslandBiome LUSH_GRASSLAND = register( "lush_grassland", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome LUSH_GRASSLAND_EDGE = register( "lush_grassland_edge", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND_EDGE ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome LUSH_GRASSLAND_OPEN = register( "lush_grassland_open", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND_OPEN ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome HIGH_LUSH_GRASSLAND = register( "high_lush_grassland", new LushGrasslandBiome( LushGrasslandBiome.Type.HIGH_LUSH_GRASSLAND ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome HIGH_LUSH_GRASSLAND_OPEN = register( "high_lush_grassland_open", new LushGrasslandBiome( LushGrasslandBiome.Type.HIGH_LUSH_GRASSLAND_OPEN ), MDDimension.MURK_SURFACE );
    public static final LushGrasslandBiome LUSH_GRASSLAND_RIVER = register( "lush_grassland_river", new LushGrasslandBiome( LushGrasslandBiome.Type.LUSH_GRASSLAND_RIVER ), MDDimension.MURK_SURFACE );

    public static final ForestBiome FOREST = register( "forest", new ForestBiome( ForestBiome.Type.FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome OPEN_FOREST = register( "open_forest", new ForestBiome( ForestBiome.Type.OPEN_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome BUSH_FOREST = register( "bush_forest", new ForestBiome( ForestBiome.Type.BUSH_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome HIGH_FOREST = register( "high_forest", new ForestBiome( ForestBiome.Type.HIGH_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome HIGH_OPEN_FOREST = register( "high_open_forest", new ForestBiome( ForestBiome.Type.HIGH_OPEN_FOREST ), MDDimension.MURK_SURFACE );
    public static final ForestBiome FOREST_RIVER = register( "forest_river", new ForestBiome( ForestBiome.Type.FOREST_RIVER ), MDDimension.MURK_SURFACE );

    public static final SwampBiome SWAMP = register( "swamp", new SwampBiome( SwampBiome.Type.SWAMP ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_HILLS = register( "swamp_hills", new SwampBiome( SwampBiome.Type.SWAMP_HILLS ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_WATER = register( "swamp_water", new SwampBiome( SwampBiome.Type.SWAMP_WATER ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_MARSHES = register( "swamp_marshes", new SwampBiome( SwampBiome.Type.SWAMP_MARSHES ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_LAND = register( "swamp_land", new SwampBiome( SwampBiome.Type.SWAMP_LAND ), MDDimension.MURK_SURFACE );
    public static final SwampBiome SWAMP_RIVER = register( "swamp_river", new SwampBiome( SwampBiome.Type.SWAMP_RIVER ), MDDimension.MURK_SURFACE );

    public static final WetlandBiome WETLAND = register( "wetland", new WetlandBiome( WetlandBiome.Type.WETLAND ), MDDimension.MURK_SURFACE );
    public static final WetlandBiome WETLAND_FOREST = register( "wetland_forest", new WetlandBiome( WetlandBiome.Type.WETLAND_FOREST ), MDDimension.MURK_SURFACE );
    public static final WetlandBiome WETLAND_MARSH = register( "wetland_marsh", new WetlandBiome( WetlandBiome.Type.WETLAND_MARSH ), MDDimension.MURK_SURFACE );
    public static final WetlandBiome WETLAND_RIVER = register( "wetland_river", new WetlandBiome( WetlandBiome.Type.WETLAND_RIVER ), MDDimension.MURK_SURFACE );

    public static final LakeBiome LAKE = register( "lake", new LakeBiome( LakeBiome.Type.LAKE ), MDDimension.MURK_SURFACE );
    public static final LakeBiome DEEP_LAKE = register( "deep_lake", new LakeBiome( LakeBiome.Type.DEEP_LAKE ), MDDimension.MURK_SURFACE );
    public static final LakeBiome UNDEEP_LAKE = register( "undeep_lake", new LakeBiome( LakeBiome.Type.UNDEEP_LAKE ), MDDimension.MURK_SURFACE );
    public static final LakeBiome GRASS_LAKE = register( "grass_lake", new LakeBiome( LakeBiome.Type.GRASS_LAKE ), MDDimension.MURK_SURFACE );
    public static final LakeBiome DEEP_GRASS_LAKE = register( "deep_grass_lake", new LakeBiome( LakeBiome.Type.DEEP_GRASS_LAKE ), MDDimension.MURK_SURFACE );
    public static final LakeBiome LAKE_SHORE = register( "lake_shore", new LakeBiome( LakeBiome.Type.LAKE_SHORE ), MDDimension.MURK_SURFACE );

    public static final MoorlandBiome MOORLAND = register( "moorland", new MoorlandBiome( MoorlandBiome.Type.MOORLAND ), MDDimension.MURK_SURFACE );
    public static final MoorlandBiome HIGH_MOORLAND = register( "high_moorland", new MoorlandBiome( MoorlandBiome.Type.HIGH_MOORLAND ), MDDimension.MURK_SURFACE );
    public static final MoorlandBiome MOORLAND_NO_TREES = register( "moorland_no_trees", new MoorlandBiome( MoorlandBiome.Type.MOORLAND_NO_TREES ), MDDimension.MURK_SURFACE );
    public static final MoorlandBiome MOORLAND_RIVER = register( "moorland_river", new MoorlandBiome( MoorlandBiome.Type.MOORLAND_RIVER ), MDDimension.MURK_SURFACE );

    public static final TallForestBiome TALL_FOREST = register( "tall_forest", new TallForestBiome( TallForestBiome.Type.TALL_FOREST ), MDDimension.MURK_SURFACE );
    public static final TallForestBiome OPEN_TALL_FOREST = register( "open_tall_forest", new TallForestBiome( TallForestBiome.Type.OPEN_TALL_FOREST ), MDDimension.MURK_SURFACE );
    public static final TallForestBiome TALL_BUSH_FOREST = register( "tall_bush_forest", new TallForestBiome( TallForestBiome.Type.TALL_BUSH_FOREST ), MDDimension.MURK_SURFACE );
    public static final TallForestBiome HIGH_TALL_FOREST = register( "high_tall_forest", new TallForestBiome( TallForestBiome.Type.HIGH_TALL_FOREST ), MDDimension.MURK_SURFACE );
    public static final TallForestBiome HIGH_OPEN_TALL_FOREST = register( "high_open_tall_forest", new TallForestBiome( TallForestBiome.Type.HIGH_OPEN_TALL_FOREST ), MDDimension.MURK_SURFACE );
    public static final TallForestBiome TALL_FOREST_RIVER = register( "tall_forest_river", new TallForestBiome( TallForestBiome.Type.TALL_FOREST_RIVER ), MDDimension.MURK_SURFACE );

    public static final ShrublandBiome SHRUBLAND = register( "shrubland", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_HEATH = register( "shrubland_heath", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_HEATH ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_GRASS = register( "shrubland_grass", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_GRASS ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_TREES = register( "shrubland_trees", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_TREES ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_SAND = register( "shrubland_sand", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_SAND ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_REEDS = register( "shrubland_reeds", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_REEDS ), MDDimension.MURK_SURFACE );
    public static final ShrublandBiome SHRUBLAND_RIVER = register( "shrubland_river", new ShrublandBiome( ShrublandBiome.Type.SHRUBLAND_RIVER ), MDDimension.MURK_SURFACE );




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
