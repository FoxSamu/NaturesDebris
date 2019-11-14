/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.util;

import modernity.common.world.gen.structure.CaveStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureStart;

import java.util.Random;

public final class CaveUtil {
    private CaveUtil() {
    }

    /**
     * Get a random pos inside a cave at specified coords
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4, cz = pos.getZ() >> 4;
        IChunk chunk = world.getChunk( cx, cz );
        StructureStart start = chunk.getStructureStart( CaveStructure.NAME );
        if( ! ( start instanceof CaveStructure.Start ) ) {
            throw new RuntimeException( "No caves found in chunk " + cx + ", " + cz + "..." );
        }
        return ( (CaveStructure.Start) start ).randomPosInCave( rand, cx * 16, cz * 16 );
    }

    /**
     * Get a random pos outside of a cave at specified coords
     * @param pos   The position, y doesn't matter
     * @param world The world
     * @param rand  A random number generator
     */
    public static BlockPos randomPosNotInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4, cz = pos.getZ() >> 4;
        IChunk chunk = world.getChunk( cx, cz );
        StructureStart start = chunk.getStructureStart( CaveStructure.NAME );
        if( ! ( start instanceof CaveStructure.Start ) ) {
            throw new RuntimeException( "No caves found in chunk " + cx + ", " + cz + "..." );
        }
        return ( (CaveStructure.Start) start ).randomPosNotInCave( rand, cx * 16, cz * 16 );
    }
}
