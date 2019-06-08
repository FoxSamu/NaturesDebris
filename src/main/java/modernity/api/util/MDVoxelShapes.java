package modernity.api.util;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class MDVoxelShapes {
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

    public static VoxelShape create8( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 8 );
    }

    public static VoxelShape create16( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 16 );
    }

    public static VoxelShape create64( double x1, double y1, double z1, double x2, double y2, double z2 ) {
        return create( x1, y1, z1, x2, y2, z2, 64 );
    }
}
