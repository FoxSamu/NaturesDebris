/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 30 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
import modernity.common.util.Events;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import static net.minecraft.util.EnumFacing.Axis;

public class BlockFenceGate extends BlockWaterlogged {
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

    public BlockFenceGate( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        setDefaultState( stateContainer.getBaseState()
                                       .with( OPEN, false )
                                       .with( POWERED, false )
                                       .with( IN_WALL, false )
        );
    }

    public BlockFenceGate( String id, Properties properties ) {
        super( id, properties );
        setDefaultState( stateContainer.getBaseState()
                                       .with( OPEN, false )
                                       .with( POWERED, false )
                                       .with( IN_WALL, false )
        );
    }

    public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        if( state.get( IN_WALL ) ) {
            return state.get( FACING ).getAxis() == Axis.X ? HITBOX_X_WALL : HITBOX_Z_WALL;
        } else {
            return state.get( FACING ).getAxis() == Axis.X ? HITBOX_X : HITBOX_Z;
        }
    }

    public VoxelShape getCollisionShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        if( state.get( OPEN ) ) {
            return VoxelShapes.empty();
        } else {
            return state.get( FACING ).getAxis() == Axis.Z ? COLLISION_Z : COLLISION_X;
        }
    }

    public VoxelShape getRenderShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        if( state.get( IN_WALL ) ) {
            return state.get( FACING ).getAxis() == Axis.X ? RENDER_X_WALL : RENDER_Z_WALL;
        } else {
            return state.get( FACING ).getAxis() == Axis.X ? RENDER_X : RENDER_Z;
        }
    }

    public boolean isFullCube( IBlockState state ) {
        return false;
    }


    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState neighborState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        state = super.updatePostPlacement( state, facing, neighborState, world, currentPos, facingPos );
        Axis axis = facing.getAxis();
        if( state.get( FACING ).rotateY().getAxis() != axis ) {
            return state;
        } else {
            boolean inWall = isWall( neighborState ) || isWall( world.getBlockState( currentPos.offset( facing.getOpposite() ) ) );
            return state.with( IN_WALL, inWall );
        }
    }

    public boolean allowsMovement( IBlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        return state.get( OPEN );
    }

    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        boolean power = world.isBlockPowered( pos );
        EnumFacing placementFacing = context.getPlacementHorizontalFacing();
        Axis axis = placementFacing.getAxis();
        boolean wall = axis == Axis.Z && ( isWall( world.getBlockState( pos.west() ) ) || isWall( world.getBlockState( pos.east() ) ) ) ||
                axis == Axis.X && ( isWall( world.getBlockState( pos.north() ) ) || isWall( world.getBlockState( pos.south() ) ) );
        return getDefaultState().with( FACING, placementFacing ).with( OPEN, power ).with( POWERED, power ).with( IN_WALL, wall )
                                .with( WATERLOGGED, EWaterlogType.getType( world.getFluidState( pos ) ) );
    }

    private boolean isWall( IBlockState state ) {
        return state.getBlock() instanceof modernity.common.block.base.BlockWall || state.getBlock() instanceof BlockWall;
    }

    public boolean onBlockActivated( IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ ) {
        if( state.get( OPEN ) ) {
            state = state.with( OPEN, false );
            world.setBlockState( pos, state, 10 );
        } else {
            EnumFacing facing = player.getHorizontalFacing();
            if( state.get( FACING ) == facing.getOpposite() ) {
                state = state.with( FACING, facing );
            }

            state = state.with( OPEN, true );
            world.setBlockState( pos, state, 10 );
        }

        world.playEvent( player, state.get( OPEN ) ? Events.FENCE_GATE_OPEN : Events.FENCE_GATE_CLOSE, pos, 0 );
        return true;
    }

    public void neighborChanged( IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos ) {
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


    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FACING, OPEN, POWERED, IN_WALL );
    }

    public BlockFaceShape getBlockFaceShape( IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face ) {
        if( face != EnumFacing.UP && face != EnumFacing.DOWN ) {
            return state.get( FACING ).getAxis() == face.rotateY().getAxis() ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED;
        } else {
            return BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    public boolean canBeConnectedTo( IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing ) {
        if( state.getBlockFaceShape( world, pos, facing ) == BlockFaceShape.MIDDLE_POLE ) {
            Block other = world.getBlockState( pos.offset( facing ) ).getBlock();
            return other instanceof BlockFence || other instanceof BlockWall;
        }
        return false;
    }
}
