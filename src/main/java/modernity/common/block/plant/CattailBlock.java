/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class CattailBlock extends DoubleDirectionalPlantBlock {
    private static final VoxelShape SHAPE = makeCuboidShape( 1, 0, 1, 15, 16, 15 );

    public CattailBlock( Properties properties ) {
        super( properties, Direction.UP );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.SOIL ) && isBlockSideSustainable( state, world, pos, Direction.UP );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        Vec3d offset = getOffset( state, world, pos );
        return SHAPE.withOffset( offset.x, offset.y, offset.z );
    }
}
