/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome;

import modernity.api.generator.fractal.FractalGenerator;
import modernity.api.generator.fractal.IRegion;
import modernity.api.generator.fractal.IRegionFactory;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

/**
 * A biome generator generates biomes based on a {@link IRegion}.
 */
public class BiomeGenerator extends FractalGenerator<Biome> {
    /**
     * Creates a biome generator.
     *
     * @param region The region generator to use.
     * @throws NullPointerException Thrown when the specified region is null.
     */
    public BiomeGenerator(IRegion region) {
        super(region);
    }

    /**
     * Creates a biome generator.
     *
     * @param factory The region factory to build a region from.
     * @throws NullPointerException Thrown when the specified factory is null.
     */
    public BiomeGenerator(IRegionFactory<?> factory) {
        super(factory);
    }

    /**
     * Returns the biome at the specified coordinates.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return The biome at this coords
     *
     * @deprecated Use {@link #generate(int, int)}
     */
    @Deprecated
    public Biome getBiome(int x, int z) {
        return generate(x, z);
    }

    /**
     * Returns an array of biomes for the specified region.
     *
     * @param x     The lower X coord
     * @param z     The lower Z coord
     * @param xSize The region size along X-axis
     * @param zSize The region size along Z-axis
     * @return An array of biomes in the region.
     *
     * @deprecated Use {@link #generate(int, int, int, int)}
     */
    @Deprecated
    public Biome[] getBiomes(int x, int z, int xSize, int zSize) {
        return generate(x, z, xSize, zSize);
    }

    /**
     * Gets the biomes for the specified region, storing them in the specified buffer if possible. A new buffer is
     * created when the specified buffer is null or too small.
     *
     * @param biomes The biome buffer to use. Can be null.
     * @param x      The lower X coord
     * @param z      The lower Z coord
     * @param xSize  The region size along X-axis
     * @param zSize  The region size along Z-axis
     * @return The used biome array filled with biomes for the region. This may or may not the same instance as the
     *     specified array.
     *
     * @deprecated Use {@link #generate(Biome[], int, int, int, int)}
     */
    @Deprecated
    public Biome[] getBiomes(Biome[] biomes, int x, int z, int xSize, int zSize) {
        return generate(biomes, x, z, xSize, zSize);
    }

    /**
     * {@inheritDoc}
     *
     * @param id The biome ID
     * @return The biome for the ID
     */
    @Override
    protected Biome toValue(int id) {
        return ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getValue(id);
    }

    /**
     * Creates a biome array.
     *
     * @param size The array size
     * @return A biome array of the specified size
     */
    @Override
    protected Biome[] createArray(int size) {
        return new Biome[size];
    }
}
