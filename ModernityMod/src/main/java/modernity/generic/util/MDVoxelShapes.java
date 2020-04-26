/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 30 - 2020
 * Author: rgsw
 */

package modernity.generic.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public final class MDVoxelShapes {
    private MDVoxelShapes() {
    }

    /**
     * Create a voxel shape using the specified amount of voxels per block
     */
    public static VoxelShape create( double x1, double y1, double z1, double x2, double y2, double z2, double voxelSize ) {
        return VoxelShapes.create(
            x1 / voxelSize,
            y1 / voxelSize,
            z1 / voxelSize,
            x2 / voxelSize,
            y2 / voxelSize,
            z2 / voxelSize
        );
    }

    /**
     * Create a voxel shape using 8 voxel per block
     */
    public static VoxelShape create8( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 8 );
    }

    /**
     * Create a voxel shape using 16 voxel per block
     */
    public static VoxelShape create16( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 16 );
    }

    /**
     * Create a voxel shape using 64 voxel per block
     */
    public static VoxelShape create64( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 64 );
    }

    public static VoxelShape plantShape( double width, double height ) {
        double rad = width / 2;
        return create16( 8 - rad, 0, 8 - rad, 8 + rad, height, 8 + rad );
    }

    public static VoxelShape hangPlantShape( double width, double height ) {
        double rad = width / 2;
        return create16( 8 - rad, 16 - height, 8 - rad, 8 + rad, 16, 8 + rad );
    }


    public static boolean hitsSideOfCube( VoxelShape shape, Direction side ) {
        Direction.Axis axis = side.getAxis();
        double proximity;
        if( side.getAxisDirection() == Direction.AxisDirection.POSITIVE ) {
            proximity = 1 - shape.getEnd( axis );
        } else {
            proximity = shape.getStart( axis );
        }
        return proximity < 0.000001;
    }

    public static boolean coversRect( VoxelShape shape, VoxelShape covered, Direction side ) {
        VoxelShape s = shape.project( side.getOpposite() );
        VoxelShape c = covered.project( side );
        return VoxelShapes.compare( s, c, IBooleanFunction.NOT_FIRST );
    }
}
