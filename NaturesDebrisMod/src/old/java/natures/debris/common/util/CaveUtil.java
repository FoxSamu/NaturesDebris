/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.util;

import natures.debris.common.area.HeightmapsArea;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public final class CaveUtil {
    private CaveUtil() {
    }

    private static HeightmapsArea getHeightmapsArea(IWorld world, BlockPos pos) {
//        IWorldAreaManager am = Modernity.get().getWorldAreaManager( world.getWorld() );
//        Stream<Area> areas = am.getAreasAt( pos );
//        return areas.filter( a -> a instanceof HeightmapsArea )
//                    .map( a -> (HeightmapsArea) a )
//                    .findFirst()
//                    .orElse( null );
        // TODO
        return null;
    }

    /**
     * Get a random pos inside a cave at specified coords
     *
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosInCave(BlockPos pos, IWorld world, Random rand) {
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;

        HeightmapsArea area = getHeightmapsArea(world, pos);
        if (area == null) throw new RuntimeException("No caves found in chunk " + cx + ", " + cz + "...");
        return area.randomPosInCave(rand, cx * 16, cz * 16);
    }

    /**
     * Get a random pos outside of a cave at specified coords
     *
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosNotInCave(BlockPos pos, IWorld world, Random rand) {
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;

        HeightmapsArea area = getHeightmapsArea(world, pos);
        if (area == null) throw new RuntimeException("No caves found in chunk " + cx + ", " + cz + "...");
        return area.randomPosNotInCave(rand, cx * 16, cz * 16);
    }

    public static int caveHeight(int x, int z, IWorld world) {
        HeightmapsArea area = getHeightmapsArea(world, new BlockPos(x, 0, z));
        if (area == null) return -1;
        return area.getCaveHeightmap().getHeight(x, z);
    }
}
