/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class HangingPlantBlock extends TallDirectionalPlantBlock {
    public static final VoxelShape MURINA_SHAPE = makeCuboidShape( 5, 0, 5, 11, 16, 11 );
    public static final VoxelShape HANG_MOSS_SHAPE = makeCuboidShape( 1, 0, 1, 15, 16, 15 );
    public static final VoxelShape HANG_MOSS_END_SHAPE = makeCuboidShape( 1, 0, 1, 15, 14, 15 );

    private final VoxelShape[] shapes;

    public HangingPlantBlock( Properties properties, VoxelShape base, VoxelShape... shapes ) {
        super( properties, Direction.DOWN );
        this.shapes = shapes.length == 0
                      ? new VoxelShape[] { base, base }
                      : new VoxelShape[] { base, shapes[ 0 ] };
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return shapes[ state.get( END ) ? 1 : 0 ];
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return isBlockSideSustainable( state, world, pos, Direction.DOWN );
    }
}
