package modernity.common.biome;

import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.api.util.EMDDimension;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class MDBiomes {
    private static final EnumMap<EMDDimension, ArrayList<Entry>> ENTRIES = new EnumMap<>( EMDDimension.class );


    public static final MeadowBiome MEADOW = registerBiomeGen( EMDDimension.SURFACE, new MeadowBiome(), 1000 );
    public static final LushMeadowBiome LUSH_MEADOW = registerBiomeGen( EMDDimension.SURFACE, new LushMeadowBiome(), 1000 );
    public static final ForestBiome FOREST = registerBiomeGen( EMDDimension.SURFACE, new ForestBiome(), 1000 );
    public static final WaterlandsBiome WATERLANDS = registerBiomeGen( EMDDimension.SURFACE, new WaterlandsBiome(), 1000 );
    public static final SwampBiome SWAMP = registerBiomeGen( EMDDimension.SURFACE, new SwampBiome(), 1000 );

    public static final RiverBiome RIVER = new RiverBiome();

    public static void register( IForgeRegistry<Biome> registry ) {
        registry.registerAll( MEADOW, LUSH_MEADOW, FOREST, WATERLANDS, SWAMP, RIVER );
    }

    public static class Entry {
        public final EMDDimension dimen;
        public final BiomeBase biome;
        public final int weight;

        private Entry( EMDDimension dimen, BiomeBase biome, int weight ) {
            this.dimen = dimen;
            this.biome = biome;
            this.weight = weight;
        }
    }

    public static <B extends BiomeBase> B registerBiomeGen( EMDDimension dimen, B base, int weight ) {
        ENTRIES.computeIfAbsent( dimen, dim -> new ArrayList<>() ).add( new Entry( dimen, base, weight ) );
        return base;
    }

    public static List<BiomeBase> getBiomesFor( EMDDimension dimen ) {
        return ENTRIES.get( dimen ).stream().map( elem -> elem.biome ).collect( Collectors.toList() );
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

    public static GenProfile createGenProfile( EMDDimension dimen ) {
        ArrayList<Entry> entries = ENTRIES.computeIfAbsent( dimen, dim -> new ArrayList<>() );
        int[] ids = new int[ entries.size() ];
        int[] wgs = new int[ entries.size() ];
        int twg = 0;
        int i = 0;
        for( Entry e : entries ) {
            int id = IRegistry.BIOME.getId( e.biome );
            int wg = e.weight;
            twg += wg;
            ids[ i ] = id;
            wgs[ i ] = wg;
            i++;
        }

        return new GenProfile( ids, wgs, twg );
    }
}
