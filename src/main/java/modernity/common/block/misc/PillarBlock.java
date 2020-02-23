/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 23 - 2020
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

    private final VoxelShape[] shapes;

    public PillarBlock( Properties properties, int width ) {
        super( properties );

        int r = width / 2;
        int n = 8 - r;
        int p = 8 + r;

        VoxelShape xshape = makeCuboidShape( 0, n, n, 16, p, p );
        VoxelShape yshape = makeCuboidShape( n, 0, n, p, 16, p );
        VoxelShape zshape = makeCuboidShape( n, n, 0, p, p, 16 );
        shapes = new VoxelShape[] { xshape, yshape, zshape };

        setDefaultState( stateContainer.getBaseState().with( AXIS, Direction.Axis.Y ) );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        Direction.Axis axis = state.get( AXIS );
        return shapes[ axis.ordinal() ];
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
