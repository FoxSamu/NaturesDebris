/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;
import modernity.common.generator.biome.profile.BiomeProfile;
import net.minecraft.world.biome.Biome;

public class BiomeBaseLayer implements IGeneratorLayer {
    private final BiomeProfile profile;

    public BiomeBaseLayer( BiomeProfile profile ) {
        this.profile = profile;
    }

    @Override
    public int generate( IRegionRNG rng, int x, int z ) {
        BiomeProfile.Entry entry = profile.random( rng.random( profile.getTotalWeight() ) );
        Biome biome = entry.getBiome();
        return id( biome );
    }
}
