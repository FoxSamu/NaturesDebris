/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome;

import modernity.common.biome.MDBiomes;
import modernity.generic.util.MDDimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

import java.util.Collection;

public class LayerBiomeProviderSettings implements IBiomeProviderSettings {
    private BiomeGenerator[] generators; // TODO Make one generator instead of array
    private Biome[] biomes;

    public LayerBiomeProviderSettings( WorldInfo info ) {

    }

    public LayerBiomeProviderSettings setGenerators( BiomeGenerator[] generators ) {
        this.generators = generators;
        return this;
    }

    public LayerBiomeProviderSettings setBiomes( Biome[] biomes ) {
        this.biomes = biomes;
        return this;
    }

    public LayerBiomeProviderSettings setBiomes( Collection<? extends Biome> biomes ) {
        this.biomes = biomes.toArray( new Biome[ 0 ] );
        return this;
    }

    public LayerBiomeProviderSettings setBiomes( MDDimension dim ) {
        this.biomes = MDBiomes.getBiomesFor( dim ).toArray( new Biome[ 0 ] );
        return this;
    }

    public BiomeGenerator[] getGenerators() {
        return generators;
    }

    public Biome[] getBiomes() {
        return biomes;
    }
}
