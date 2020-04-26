/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.structure.util;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;

public final class StructureUtil {
    private StructureUtil() {
    }

    public static MutableBoundingBox getOrientedBox( int structX, int structY, int structZ, int x, int y, int z, int xsize, int ysize, int zsize, Direction dir ) {
        int x2 = x + xsize - 1;
        int y2 = y + ysize - 1;
        int z2 = z + zsize - 1;

        switch( dir ) {
            default:
            case EAST:
                return new MutableBoundingBox( structX + x, structY + y, structZ + z, structX + x2, structY + y2, structZ + z2 );
            case WEST:
                return new MutableBoundingBox( structX - x2, structY + y, structZ - z2, structX - x, structY + y2, structZ - z );
            case NORTH:
                return new MutableBoundingBox( structX + z, structY + y, structZ - x2, structX + z2, structY + y2, structZ - x );
            case SOUTH:
                return new MutableBoundingBox( structX - z2, structY + y, structZ + x, structX - z, structY + y2, structZ + x2 );
        }
    }

    public static int getX( int x, int z, Direction dir, MutableBoundingBox box ) {
        if( dir == null ) {
            return x;
        } else {
            switch( dir ) {
                default:
                    return x;
                case EAST:
                    return box.minX + x;
                case WEST:
                    return box.maxX - x;
                case NORTH:
                    return box.minX + z;
                case SOUTH:
                    return box.maxX - z;
            }
        }
    }

    public static int getZ( int x, int z, Direction dir, MutableBoundingBox box ) {
        if( dir == null ) {
            return z;
        } else {
            switch( dir ) {
                default:
                    return z;
                case EAST:
                    return box.minZ + z;
                case WEST:
                    return box.maxZ - z;
                case NORTH:
                    return box.maxZ - x;
                case SOUTH:
                    return box.minZ + x;
            }
        }
    }

    public static Rotation getRotation( Direction dir ) {
        if( dir == null ) {
            return Rotation.NONE;
        } else {
            switch( dir ) {
                default:
                case EAST:
                    return Rotation.NONE;
                case WEST:
                    return Rotation.CLOCKWISE_180;
                case NORTH:
                    return Rotation.COUNTERCLOCKWISE_90;
                case SOUTH:
                    return Rotation.CLOCKWISE_90;
            }
        }
    }
}

