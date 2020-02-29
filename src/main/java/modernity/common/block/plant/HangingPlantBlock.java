/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class HangingPlantBlock extends TallDirectionalPlantBlock {
    public static final VoxelShape MURINA_SHAPE = makeHangPlantShape( 6, 16 );
    public static final VoxelShape HANG_MOSS_SHAPE = makeHangPlantShape( 14, 16 );
    public static final VoxelShape HANG_MOSS_END_SHAPE = makeHangPlantShape( 14, 14 );

    private final VoxelShape[] shapes;

    public HangingPlantBlock( Properties properties, VoxelShape base, VoxelShape... shapes ) {
        super( properties, Direction.DOWN );
        this.shapes = shapes.length == 0
                      ? new VoxelShape[] { base, base }
                      : new VoxelShape[] { base, shapes[ 0 ] };
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        Vec3d off = getOffset( state, world, pos );
        return shapes[ state.get( END ) ? 1 : 0 ].withOffset( off.x, off.y, off.z );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return isBlockSideSustainable( state, world, pos, Direction.DOWN );
    }
}
