/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.profile;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class BiomeMutationProfile {
    private final Map<Integer, BiomeProfile> subbiomeProfiles = new HashMap<>();

    public BiomeProfile getProfile( Biome biome ) {
        return subbiomeProfiles.get( idForBiome( biome ) );
    }

    public BiomeProfile getProfile( int id ) {
        return subbiomeProfiles.get( id );
    }

    public BiomeMutationProfile putDefault( Biome biome, int weight ) {
        return put( biome, biome, weight );
    }

    public BiomeMutationProfile putDefault( Biome biome, IBiomeRarity rarity ) {
        return put( biome, biome, rarity );
    }

    public BiomeMutationProfile put( Biome biome, Biome mutation, int weight ) {
        subbiomeProfiles.computeIfAbsent( idForBiome( biome ), key -> new BiomeProfile() )
                        .put( mutation, weight, 0 );
        return this;
    }

    public BiomeMutationProfile put( Biome biome, Biome mutation, IBiomeRarity rarity ) {
        subbiomeProfiles.computeIfAbsent( idForBiome( biome ), key -> new BiomeProfile() )
                        .put( mutation, rarity, 0 );
        return this;
    }



    private static int idForBiome( Biome biome ) {
        return ( (ForgeRegistry<Biome>) ForgeRegistries.BIOMES ).getID( biome );
    }
}
