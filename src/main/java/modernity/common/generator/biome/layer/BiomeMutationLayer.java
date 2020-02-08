/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;
import modernity.common.generator.biome.profile.BiomeMutationProfile;
import modernity.common.generator.biome.profile.BiomeProfile;
import net.minecraft.world.biome.Biome;

public class BiomeMutationLayer implements IFilterTransformerLayer {
    private final BiomeMutationProfile profile;

    public BiomeMutationLayer( BiomeMutationProfile profile ) {
        this.profile = profile;
    }

    @Override
    public int generate( IRegionRNG rng, int value ) {
        Biome biome = biome( value );
        BiomeProfile mutationProfile = profile.getProfile( biome );
        if( mutationProfile == null ) return value;

        Biome mutation = mutationProfile.random( rng.random( mutationProfile.getTotalWeight() ) ).getBiome();
        return id( mutation );
    }
}
