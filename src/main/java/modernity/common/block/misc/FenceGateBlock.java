/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.api.util.Events;
import modernity.common.block.fluid.WaterlogType;
import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import static net.minecraft.util.Direction.*;

/**
 * Describes a fence gate block.
 */
@SuppressWarnings( "deprecation" )
public class FenceGateBlock extends WaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;

    protected static final VoxelShape HITBOX_Z = Block.makeCuboidShape( 0, 0, 6, 16, 16, 10 );
    protected static final VoxelShape HITBOX_X = Block.makeCuboidShape( 6, 0, 0, 10, 16, 16 );
    protected static final VoxelShape HITBOX_Z_WALL = Block.makeCuboidShape( 0, 0, 6, 16, 13, 10 );
    protected static final VoxelShape HITBOX_X_WALL = Block.makeCuboidShape( 6, 0, 0, 10, 13, 16 );

    protected static final VoxelShape COLLISION_Z = Block.makeCuboidShape( 0, 0, 6, 16, 24, 10 );
    protected static final VoxelShape COLLISION_X = Block.makeCuboidShape( 6, 0, 0, 10, 24, 16 );

    protected static final VoxelShape RENDER_Z = VoxelShapes.or( Block.makeCuboidShape( 0, 5, 7, 2, 16, 9 ), Block.makeCuboidShape( 14, 5, 7, 16, 16, 9 ) );
    protected static final VoxelShape RENDER_X = VoxelShapes.or( Block.makeCuboidShape( 7, 5, 0, 9, 16, 2 ), Block.makeCuboidShape( 7, 5, 14, 9, 16, 16 ) );
    protected static final VoxelShape RENDER_Z_WALL = VoxelShapes.or( Block.makeCuboidShape( 0, 2, 7, 2, 13, 9 ), Block.makeCuboidShape( 14, 2, 7, 16, 13, 9 ) );
    protected static final VoxelShape RENDER_X_WALL = VoxelShapes.or( Block.makeCuboidShape( 7, 2, 0, 9, 13, 2 ), Block.makeCuboidShape( 7, 2, 14, 9, 13, 16 ) );

    public FenceGateBlock( Properties properties ) {
        super( properties );
        setDefaultState( stateContainer.getBaseState()
                                       .with( OPEN, false )
                                       .with( POWERED, false )
                                       .with( IN_WALL, false )
        );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx ) {
        if( state.get( IN_WALL ) ) {
            return state.get( FACING ).getAxis() == Axis.X ? HITBOX_X_WALL : HITBOX_Z_WALL;
        } else {
            return state.get( FACING ).getAxis() == Axis.X ? HITBOX_X : HITBOX_Z;
        }
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx ) {
        if( state.get( OPEN ) ) {
            return VoxelShapes.empty();
        } else {
            return state.get( FACING ).getAxis() == Axis.Z ? COLLISION_Z : COLLISION_X;
        }
    }

    @Override
    public VoxelShape getRenderShape( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        if( state.get( IN_WALL ) ) {
            return state.get( FACING ).getAxis() == Axis.X ? RENDER_X_WALL : RENDER_Z_WALL;
        } else {
            return state.get( FACING ).getAxis() == Axis.X ? RENDER_X : RENDER_Z;
        }
    }


    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        state = super.updatePostPlacement( state, facing, neighborState, world, currentPos, facingPos );
        Axis axis = facing.getAxis();
        if( state.get( FACING ).rotateY().getAxis() != axis ) {
            return state;
        } else {
            boolean inWall = isWall( neighborState ) || isWall( world.getBlockState( currentPos.offset( facing.getOpposite() ) ) );
            return state.with( IN_WALL, inWall );
        }
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        return state.get( OPEN );
    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        boolean power = world.isBlockPowered( pos );
        Direction placementFacing = context.getPlacementHorizontalFacing();
        Axis axis = placementFacing.getAxis();
        boolean wall = axis == Axis.Z && ( isWall( world.getBlockState( pos.west() ) ) || isWall( world.getBlockState( pos.east() ) ) ) ||
                           axis == Axis.X && ( isWall( world.getBlockState( pos.north() ) ) || isWall( world.getBlockState( pos.south() ) ) );
        return getDefaultState().with( FACING, placementFacing ).with( OPEN, power ).with( POWERED, power ).with( IN_WALL, wall )
                                .with( WATERLOGGED, WaterlogType.getType( world.getFluidState( pos ) ) );
    }

    /**
     * Is the specified block a wall block?
     */
    private boolean isWall( BlockState state ) {
        return state.getBlock() instanceof WallBlock || state.getBlock() instanceof WallBlock;
    }

    @Override
    public ActionResultType onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( state.get( OPEN ) ) {
            state = state.with( OPEN, false );
            world.setBlockState( pos, state, 10 );
        } else {
            Direction facing = player.getHorizontalFacing();
            if( state.get( FACING ) == facing.getOpposite() ) {
                state = state.with( FACING, facing );
            }

            state = state.with( OPEN, true );
            world.setBlockState( pos, state, 10 );
        }

        world.playEvent( player, state.get( OPEN ) ? Events.FENCE_GATE_OPEN : Events.FENCE_GATE_CLOSE, pos, 0 );
        return ActionResultType.SUCCESS;
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving ) {
        if( ! world.isRemote ) {
            boolean power = world.isBlockPowered( pos );
            if( state.get( POWERED ) != power ) {
                world.setBlockState( pos, state.with( POWERED, power ).with( OPEN, power ), 2 );
                if( state.get( OPEN ) != power ) {
                    world.playEvent( null, power ? Events.FENCE_GATE_OPEN : Events.FENCE_GATE_CLOSE, pos, 0 );
                }
            }
        }
    }


    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FACING, OPEN, POWERED, IN_WALL );
    }

    @Override
    public boolean canBeConnectedTo( BlockState state, IBlockReader world, BlockPos pos, Direction facing ) {
        Block other = world.getBlockState( pos.offset( facing ) ).getBlock();
        return other instanceof FenceBlock || other instanceof net.minecraft.block.WallBlock;
    }
}
