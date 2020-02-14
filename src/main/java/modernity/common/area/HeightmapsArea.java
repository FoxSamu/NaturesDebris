/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.area;

import modernity.api.util.HeightMap;
import modernity.common.area.core.Area;
import modernity.common.area.core.AreaBox;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class HeightmapsArea extends Area {
    private final HeightMap caveHeightmap = new HeightMap();

    public HeightmapsArea( World world, AreaBox box ) {
        super( MDAreas.HEIGHTMAPS, world, box );
    }

    public HeightMap getCaveHeightmap() {
        return caveHeightmap;
    }

    @Override
    public void write( CompoundNBT nbt, SerializeType type ) {
        nbt.putLongArray( "caveHeightmap", caveHeightmap.getLongArray() );
    }

    @Override
    public void read( CompoundNBT nbt, SerializeType type ) {
        caveHeightmap.applyLongArray( nbt.getLongArray( "caveHeightmap" ) );
    }

    public void applyCaveHeights( int[] hm ) {
        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                caveHeightmap.setHeight( x, z, hm[ x + z * 16 ] );
            }
        }
    }

    /**
     * Checks if the specified position is in a cave.
     */
    public boolean contains( int x, int y, int z ) {
        return y < caveHeightmap.getHeight( x, z );
    }

    /**
     * Returns a random position inside a cave.
     */
    public BlockPos randomPosInCave( Random rand, int xoff, int zoff ) {
        int x = rand.nextInt( 16 );
        int z = rand.nextInt( 16 );
        int y = rand.nextInt( caveHeightmap.getHeight( x, z ) + 1 );
        return new BlockPos( x + xoff, y, z + zoff );
    }

    /**
     * Returns a random position outside a cave.
     */
    public BlockPos randomPosNotInCave( Random rand, int xoff, int zoff ) {
        int x = rand.nextInt( 16 );
        int z = rand.nextInt( 16 );
        int y = 255 - rand.nextInt( 255 - caveHeightmap.getHeight( x, z ) );
        return new BlockPos( x + xoff, y, z + zoff );
    }
}
