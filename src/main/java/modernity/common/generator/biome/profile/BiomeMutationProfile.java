/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.profile;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class BiomeMutationProfile {
    private final Int2ObjectMap<BiomeProfile> subbiomeProfiles = new Int2ObjectOpenCustomHashMap<>( BiomeHashStrategy.INSTANCE );

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
