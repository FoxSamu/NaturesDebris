/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.profile;

import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class BiomeMutationProfile {
    private final Map<Biome, BiomeProfile> subbiomeProfiles = new HashMap<>();

    public BiomeProfile getProfile( Biome biome ) {
        return subbiomeProfiles.get( biome );
    }

    public BiomeMutationProfile putDefault( Biome biome, int weight ) {
        return put( biome, biome, weight );
    }

    public BiomeMutationProfile putDefault( Biome biome, IBiomeRarity rarity ) {
        return put( biome, biome, rarity );
    }

    public BiomeMutationProfile put( Biome biome, Biome mutation, int weight ) {
        subbiomeProfiles.computeIfAbsent( biome, key -> new BiomeProfile() )
                        .put( mutation, weight, 0 );
        return this;
    }

    public BiomeMutationProfile put( Biome biome, Biome mutation, IBiomeRarity rarity ) {
        subbiomeProfiles.computeIfAbsent( biome, key -> new BiomeProfile() )
                        .put( mutation, rarity, 0 );
        return this;
    }
}
