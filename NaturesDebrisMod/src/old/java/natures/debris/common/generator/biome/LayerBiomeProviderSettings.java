/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome;

import natures.debris.common.biome.MDBiomes;
import natures.debris.generic.util.MDDimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

import java.util.Collection;

public class LayerBiomeProviderSettings implements IBiomeProviderSettings {
    private BiomeGenerator[] generators; // TODO Make one generator instead of array
    private Biome[] biomes;

    public LayerBiomeProviderSettings(WorldInfo info) {

    }

    public BiomeGenerator[] getGenerators() {
        return generators;
    }

    public LayerBiomeProviderSettings setGenerators(BiomeGenerator[] generators) {
        this.generators = generators;
        return this;
    }

    public Biome[] getBiomes() {
        return biomes;
    }

    public LayerBiomeProviderSettings setBiomes(Biome[] biomes) {
        this.biomes = biomes;
        return this;
    }

    public LayerBiomeProviderSettings setBiomes(Collection<? extends Biome> biomes) {
        this.biomes = biomes.toArray(new Biome[0]);
        return this;
    }

    public LayerBiomeProviderSettings setBiomes(MDDimension dim) {
        this.biomes = MDBiomes.getBiomesFor(dim).toArray(new Biome[0]);
        return this;
    }
}
