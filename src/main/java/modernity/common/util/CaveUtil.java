/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 17 - 2020
 * Author: rgsw
 */

package modernity.common.util;

import modernity.common.Modernity;
import modernity.common.area.HeightmapsArea;
import modernity.common.area.core.Area;
import modernity.common.area.core.IWorldAreaManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;
import java.util.stream.Stream;

public final class CaveUtil {
    private CaveUtil() {
    }

    private static HeightmapsArea getHeightmapsArea( IWorld world, BlockPos pos ) {
        IWorldAreaManager am = Modernity.get().getWorldAreaManager( world.getWorld() );
        Stream<Area> areas = am.getAreasAt( pos );
        return areas.filter( a -> a instanceof HeightmapsArea )
                    .map( a -> (HeightmapsArea) a )
                    .findFirst()
                    .orElse( null );
    }

    /**
     * Get a random pos inside a cave at specified coords
     *
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;

        HeightmapsArea area = getHeightmapsArea( world, pos );
        if( area == null ) throw new RuntimeException( "No caves found in chunk " + cx + ", " + cz + "..." );
        return area.randomPosInCave( rand, cx * 16, cz * 16 );
    }

    /**
     * Get a random pos outside of a cave at specified coords
     *
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosNotInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;

        HeightmapsArea area = getHeightmapsArea( world, pos );
        if( area == null ) throw new RuntimeException( "No caves found in chunk " + cx + ", " + cz + "..." );
        return area.randomPosNotInCave( rand, cx * 16, cz * 16 );
    }

    public static int caveHeight( int x, int z, IWorld world ) {
        HeightmapsArea area = getHeightmapsArea( world, new BlockPos( x, 0, z ) );
        if( area == null ) return - 1;
        return area.getCaveHeightmap().getHeight( x, z );
    }
}
