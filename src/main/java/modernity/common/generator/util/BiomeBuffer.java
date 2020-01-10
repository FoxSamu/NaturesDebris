/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.util;

import net.minecraft.world.biome.Biome;

public class BiomeBuffer<B extends Biome> {
    private final Biome[] biomes;
    private int x;
    private int z;
    private final int xSize;
    private final int zSize;

    public BiomeBuffer( int x, int z, int xSize, int zSize ) {
        this.biomes = new Biome[ xSize * zSize ];
        this.x = x;
        this.z = z;
        this.xSize = xSize;
        this.zSize = zSize;
    }

    public BiomeBuffer( int xSize, int zSize ) {
        this.biomes = new Biome[ xSize * zSize ];
        this.xSize = xSize;
        this.zSize = zSize;
    }

    public int xSize() {
        return xSize;
    }

    public int zSize() {
        return zSize;
    }

    public int x() {
        return x;
    }

    public int z() {
        return z;
    }

    public void setPos( int x, int z ) {
        this.x = x;
        this.z = z;
    }

    private int index( int px, int pz ) {
        return ( px - x ) * zSize + pz - z;
    }

    @SuppressWarnings( "unchecked" )
    public B get( int x, int z ) {
        return (B) biomes[ index( x, z ) ];
    }

    public void set( int x, int z, B biome ) {
        biomes[ index( x, z ) ] = biome;
    }
}
