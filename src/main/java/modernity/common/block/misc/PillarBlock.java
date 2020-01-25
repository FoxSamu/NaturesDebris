/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class PillarBlock extends WaterloggedBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private static final VoxelShape X_SHAPE = makeCuboidShape( 0, 1, 1, 16, 15, 15 );
    private static final VoxelShape Y_SHAPE = makeCuboidShape( 1, 0, 1, 15, 16, 15 );
    private static final VoxelShape Z_SHAPE = makeCuboidShape( 1, 1, 0, 15, 15, 16 );

    private static final VoxelShape[] SHAPES = { X_SHAPE, Y_SHAPE, Z_SHAPE };

    public PillarBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState().with( AXIS, Direction.Axis.Y ) );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        Direction.Axis axis = state.get( AXIS );
        return SHAPES[ axis.ordinal() ];
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return false;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( AXIS );
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return super.getStateForPlacement( context ).with( AXIS, context.getFace().getAxis() );
    }
}
