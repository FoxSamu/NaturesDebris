/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegion;
import modernity.common.generator.biome.core.IRegionRNG;
import modernity.common.generator.biome.profile.BiomeProfile;
import net.minecraft.world.biome.Biome;

public class LargeBiomeLayer implements ITransformerLayer {
    private final BiomeProfile profile;

    public LargeBiomeLayer( BiomeProfile profile ) {
        this.profile = profile;
    }

    @Override
    public int generate( IRegionRNG rng, IRegion region, int x, int z ) {
        int xval = region.getValue( x - 1, z );
        int zval = region.getValue( x, z - 1 );
        int xzval = region.getValue( x - 1, z - 1 );

        Biome xbiome = biome( xval );
        Biome zbiome = biome( zval );
        Biome xzbiome = biome( xzval );

        Biome out = biome( region.getValue( x, z ) );

        rng.setPosition( x - 1, z );
        if( isLarge( rng, xbiome ) ) out = applyDominance( rng, out, xbiome );

        rng.setPosition( x, z - 1 );
        if( isLarge( rng, zbiome ) ) out = applyDominance( rng, out, zbiome );

        rng.setPosition( x - 1, z - 1 );
        if( isLarge( rng, xzbiome ) ) out = applyDominance( rng, out, xzbiome );

        return id( out );
    }

    private Biome applyDominance( IRegionRNG rng, Biome biome1, Biome biome2 ) {
        double dominance1 = profile.getEntry( biome1 ).getDominance();
        double dominance2 = profile.getEntry( biome2 ).getDominance();

        if( dominance1 == dominance2 ) {
            return rng.randomBool() ? biome1 : biome2;
        } else {
            return dominance1 > dominance2 ? biome1 : biome2;
        }
    }

    private boolean isLarge( IRegionRNG rng, Biome biome ) {
        double chance = profile.getEntry( biome ).getLargeChance();
        return chance != 0 && rng.randomDouble() < chance;
    }
}
