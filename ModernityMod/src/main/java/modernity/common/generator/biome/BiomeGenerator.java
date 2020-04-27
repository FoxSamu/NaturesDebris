/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome;

import modernity.common.generator.region.IRegion;
import modernity.common.generator.region.IRegionFactory;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

/**
 * A biome generator generates biomes based on a {@link IRegion}.
 */
public class BiomeGenerator {

    private final IRegion region;

    /**
     * Creates a biome generator.
     *
     * @param region The region generator to use.
     * @throws NullPointerException Thrown when the specified region is null.
     */
    public BiomeGenerator( IRegion region ) {
        if( region == null ) throw new NullPointerException();
        this.region = region;
    }

    /**
     * Creates a biome generator.
     *
     * @param factory The region factory to build a region from.
     * @throws NullPointerException Thrown when the specified factory is null.
     */
    public BiomeGenerator( IRegionFactory<?> factory ) {
        if( factory == null ) throw new NullPointerException();
        this.region = factory.buildRegion();
    }

    /**
     * Returns the biome at the specified coordinates.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return The biome at this coords
     */
    public Biome getBiome( int x, int z ) {
        int biome = region.getValue( x, z );
        return biomeForValue( biome );
    }

    /**
     * Returns an array of biomes for the specified region.
     *
     * @param x     The lower X coord
     * @param z     The lower Z coord
     * @param xSize The region size along X-axis
     * @param zSize The region size along Z-axis
     * @return An array of biomes in the region.
     */
    public Biome[] getBiomes( int x, int z, int xSize, int zSize ) {
        return getBiomes( null, x, z, xSize, zSize );
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
     */
    public Biome[] getBiomes( Biome[] biomes, int x, int z, int xSize, int zSize ) {
        if( biomes == null || biomes.length < xSize * zSize ) {
            biomes = new Biome[ xSize * zSize ];
        }

        for( int ix = 0; ix < xSize; ix++ ) {
            for( int iz = 0; iz < zSize; iz++ ) {
                biomes[ iz * xSize + ix ] = getBiome( ix + x, iz + z );
            }
        }

        return biomes;
    }

    /**
     * Gets the biome for the specified ID.
     *
     * @param id The biome ID
     * @return The biome for the ID
     */
    private static Biome biomeForValue( int id ) {
        return ( (ForgeRegistry<Biome>) ForgeRegistries.BIOMES ).getValue( id );
    }
}
