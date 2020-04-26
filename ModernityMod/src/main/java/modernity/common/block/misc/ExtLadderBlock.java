/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 27 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.common.block.fluid.WaterlogType;
import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

@SuppressWarnings( "deprecation" )
public class ExtLadderBlock extends WaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    protected static final VoxelShape EAST_HITBOX = makeCuboidShape( 0, 0, 0, 3, 16, 16 );
    protected static final VoxelShape WEST_HITBOX = makeCuboidShape( 13, 0, 0, 16, 16, 16 );
    protected static final VoxelShape SOUTH_HITBOX = makeCuboidShape( 0, 0, 0, 16, 16, 3 );
    protected static final VoxelShape NORTH_HITBOX = makeCuboidShape( 0, 0, 13, 16, 16, 16 );

    public ExtLadderBlock( Properties builder ) {
        super( builder );
        this.setDefaultState( this.stateContainer.getBaseState().with( FACING, Direction.NORTH ).with( WATERLOGGED, WaterlogType.NONE ) );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        switch( state.get( FACING ) ) {
            default:
            case NORTH:
                return NORTH_HITBOX;
            case SOUTH:
                return SOUTH_HITBOX;
            case WEST:
                return WEST_HITBOX;
            case EAST:
                return EAST_HITBOX;
        }
    }

    private boolean canAttachTo( IBlockReader world, BlockPos pos, Direction facing ) {
        BlockState state = world.getBlockState( pos );
        return ! state.canProvidePower() && state.isSolidSide( world, pos, facing );
    }

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader worldIn, BlockPos pos ) {
        Direction facing = state.get( FACING );
        return canAttachTo( worldIn, pos.offset( facing.getOpposite() ), facing );
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        if( facing.getOpposite() == state.get( FACING ) && ! state.isValidPosition( world, pos ) ) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.updatePostPlacement( state, facing, adjState, world, pos, adjPos );
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        if( ! ctx.replacingClickedOnBlock() ) {
            BlockState state = ctx.getWorld().getBlockState( ctx.getPos().offset( ctx.getFace().getOpposite() ) );
            if( state.getBlock() == this && state.get( FACING ) == ctx.getFace() ) {
                return null;
            }
        }

        BlockState def = getDefaultState();
        IWorldReader world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        IFluidState fstate = ctx.getWorld().getFluidState( ctx.getPos() );

        for( Direction dir : ctx.getNearestLookingDirections() ) {
            if( dir.getAxis().isHorizontal() ) {
                def = def.with( FACING, dir.getOpposite() );
                if( def.isValidPosition( world, pos ) ) {
                    return def.with( WATERLOGGED, WaterlogType.getType( fstate ) );
                }
            }
        }

        return null;
    }

    @Override
    public boolean isLadder( BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity ) {
        return true;
    }


    @Override
    public BlockState rotate( BlockState state, Rotation rot ) {
        return state.with( FACING, rot.rotate( state.get( FACING ) ) );
    }

    @Override
    public BlockState mirror( BlockState state, Mirror mir ) {
        return state.rotate( mir.toRotation( state.get( FACING ) ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( FACING, WATERLOGGED );
    }
}
