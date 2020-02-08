/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.profile;

import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BiomeProfile {
    private final Map<Biome, Entry> entries = new HashMap<>();
    private final Map<Biome, Entry> entriesUnmodifiable = Collections.unmodifiableMap( entries );
    private int totalWeight = - 1;

    public BiomeProfile put( Biome biome, int weight, double dominance, double largeChance ) {
        entries.put( biome, new Entry( weight, dominance, largeChance, biome ) );
        totalWeight = - 1;
        return this;
    }

    public BiomeProfile put( Biome biome, IBiomeRarity rarity, double dominance, double largeChance ) {
        return put( biome, rarity.weight(), dominance, largeChance );
    }

    public BiomeProfile put( Biome biome, int weight, double dominance ) {
        return put( biome, weight, dominance, 0 );
    }

    public BiomeProfile put( Biome biome, IBiomeRarity rarity, double dominance ) {
        return put( biome, rarity.weight(), dominance, 0 );
    }

    public Map<Biome, Entry> getEntries() {
        return entriesUnmodifiable;
    }

    private void computeTotalWeight() {
        if( totalWeight < 0 ) {
            totalWeight = 0;
            for( Entry e : entries.values() ) {
                totalWeight += e.getWeight();
            }
        }
    }

    public int getTotalWeight() {
        computeTotalWeight();
        return totalWeight;
    }

    public Entry getEntry( Biome biome ) {
        return entries.get( biome );
    }

    public Entry random( int random ) {
        int tw = getTotalWeight();
        if( tw == 0 ) return null;

        if( random < 0 ) random = - random;
        if( random > tw ) {
            random %= tw;
        }

        for( Entry e : entries.values() ) {
            random -= e.weight;
            if( random <= 0 ) {
                return e;
            }
        }

        return null;
    }

    public static final class Entry {
        private final int weight;
        private final double dominance;
        private final double largeChance;
        private final Biome biome;

        public Entry( int weight, double dominance, double largeChance, Biome biome ) {
            this.weight = weight;
            this.dominance = dominance;
            this.largeChance = largeChance;
            this.biome = biome;
        }

        public int getWeight() {
            return weight;
        }

        public double getLargeChance() {
            return largeChance;
        }

        public double getDominance() {
            return dominance;
        }

        public Biome getBiome() {
            return biome;
        }
    }
}
