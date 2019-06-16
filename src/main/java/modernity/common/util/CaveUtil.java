/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureStart;

import modernity.api.err.NoCavesException;
import modernity.common.world.gen.structure.CaveStructure;

import java.util.Random;

public class CaveUtil {
    public static BlockPos randomPosInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4, cz = pos.getZ() >> 4;
        IChunk chunk = world.getChunk( cx, cz );
        StructureStart start = chunk.getStructureStart( CaveStructure.NAME );
        if( ! ( start instanceof CaveStructure.Start ) ) {
            throw new NoCavesException( "No caves found in chunk " + cx + ", " + cz + "..." );
        }
        return ( (CaveStructure.Start) start ).randomPosInCave( rand, cx * 16, cz * 16 );
    }

    public static BlockPos randomPosNotInCave( BlockPos pos, IWorld world, Random rand ) {
        int cx = pos.getX() >> 4, cz = pos.getZ() >> 4;
        IChunk chunk = world.getChunk( cx, cz );
        StructureStart start = chunk.getStructureStart( CaveStructure.NAME );
        if( ! ( start instanceof CaveStructure.Start ) ) {
            throw new NoCavesException( "No caves found in chunk " + cx + ", " + cz + "..." );
        }
        return ( (CaveStructure.Start) start ).randomPosNotInCave( rand, cx * 16, cz * 16 );
    }
}
