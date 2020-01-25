/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.common.block.fluid.WaterlogType;
import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * Describes a fence block.
 */
@SuppressWarnings( "deprecation" )
public class FenceBlock extends WaterloggedBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final VoxelShape[] HITBOX_SHAPES = new VoxelShape[ 16 ];
    private static final VoxelShape[] COLLISION_SHAPES = new VoxelShape[ 16 ];
    private static final VoxelShape[] RENDER_SHAPES = new VoxelShape[ 16 ];

    static {
        VoxelShape pole = makeCuboidShape( 6, 0, 6, 10, 16, 10 );
        VoxelShape north = makeCuboidShape( 6, 0, 0, 10, 16, 8 );
        VoxelShape south = makeCuboidShape( 6, 0, 8, 10, 16, 16 );
        VoxelShape west = makeCuboidShape( 0, 0, 6, 8, 16, 10 );
        VoxelShape east = makeCuboidShape( 8, 0, 6, 16, 16, 10 );

        VoxelShape cpole = makeCuboidShape( 6, 0, 6, 10, 24, 10 );
        VoxelShape cnorth = makeCuboidShape( 6, 0, 0, 10, 24, 8 );
        VoxelShape csouth = makeCuboidShape( 6, 0, 8, 10, 24, 16 );
        VoxelShape cwest = makeCuboidShape( 0, 0, 6, 8, 24, 10 );
        VoxelShape ceast = makeCuboidShape( 8, 0, 6, 16, 24, 10 );

        VoxelShape rpole = makeCuboidShape( 7, 0, 7, 9, 16, 9 );
        VoxelShape rnorth = makeCuboidShape( 7, 6, 0, 9, 15, 8 );
        VoxelShape rsouth = makeCuboidShape( 7, 6, 8, 9, 15, 16 );
        VoxelShape rwest = makeCuboidShape( 0, 6, 7, 8, 15, 9 );
        VoxelShape reast = makeCuboidShape( 8, 6, 7, 16, 15, 9 );

        for( int i = 0; i < 16; i++ ) {
            VoxelShape hitbox = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) hitbox = VoxelShapes.or( hitbox, north );
            if( ( i & 2 ) != 0 ) hitbox = VoxelShapes.or( hitbox, south );
            if( ( i & 4 ) != 0 ) hitbox = VoxelShapes.or( hitbox, west );
            if( ( i & 8 ) != 0 ) hitbox = VoxelShapes.or( hitbox, east );
            hitbox = VoxelShapes.or( hitbox, pole );
            HITBOX_SHAPES[ i ] = hitbox;
            VoxelShape collision = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) collision = VoxelShapes.or( collision, cnorth );
            if( ( i & 2 ) != 0 ) collision = VoxelShapes.or( collision, csouth );
            if( ( i & 4 ) != 0 ) collision = VoxelShapes.or( collision, cwest );
            if( ( i & 8 ) != 0 ) collision = VoxelShapes.or( collision, ceast );
            collision = VoxelShapes.or( collision, cpole );
            COLLISION_SHAPES[ i ] = collision;
            VoxelShape render = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) render = VoxelShapes.or( render, rnorth );
            if( ( i & 2 ) != 0 ) render = VoxelShapes.or( render, rsouth );
            if( ( i & 4 ) != 0 ) render = VoxelShapes.or( render, rwest );
            if( ( i & 8 ) != 0 ) render = VoxelShapes.or( render, reast );
            render = VoxelShapes.or( render, rpole );
            RENDER_SHAPES[ i ] = render;
        }
    }

    public FenceBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, pos, facingPos );

        if( facing != Direction.DOWN ) {
            boolean north = facing == Direction.NORTH
                            ? attachesTo( facingState, facingState.func_224755_d( world, facingPos, facing.getOpposite() ), facing )
                            : state.get( NORTH );
            boolean east = facing == Direction.EAST
                           ? attachesTo( facingState, facingState.func_224755_d( world, facingPos, facing.getOpposite() ), facing )
                           : state.get( EAST );
            boolean south = facing == Direction.SOUTH
                            ? attachesTo( facingState, facingState.func_224755_d( world, facingPos, facing.getOpposite() ), facing )
                            : state.get( SOUTH );
            boolean west = facing == Direction.WEST
                           ? attachesTo( facingState, facingState.func_224755_d( world, facingPos, facing.getOpposite() ), facing )
                           : state.get( WEST );
            return state.with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west );
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        IWorld world = context.getWorld();
        BlockPos pos = context.getPos();
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        boolean north = canFenceConnectTo( world, pos, Direction.NORTH );
        boolean east = canFenceConnectTo( world, pos, Direction.EAST );
        boolean south = canFenceConnectTo( world, pos, Direction.SOUTH );
        boolean west = canFenceConnectTo( world, pos, Direction.WEST );
        return getDefaultState().with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west ).with( WATERLOGGED, WaterlogType.getType( fluid ) );
    }

    private boolean attachesTo( BlockState state, boolean solidSide, Direction direction ) {
        Block block = state.getBlock();
        boolean fence = block.isIn( BlockTags.FENCES ) && state.getMaterial() == this.material;
        boolean gate = isFenceGate( block ) && FenceGateBlock.isParallel( state, direction );
        return ! cannotAttach( block ) && solidSide || fence || gate;
    }

    /**
     * Can we connect to the block at the specified position block?
     */
    private boolean canFenceConnectTo( IBlockReader world, BlockPos pos, Direction facing ) {
        BlockPos off = pos.offset( facing );
        BlockState other = world.getBlockState( off );
        return other.canBeConnectedTo( world, off, facing.getOpposite() ) || attachesTo( other, other.func_224755_d( world, off, facing.getOpposite() ), facing );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( NORTH, EAST, SOUTH, WEST );
    }

    /**
     * Is the given block a fence gate?
     */
    private boolean isFenceGate( Block block ) {
        return block instanceof modernity.common.block.misc.FenceGateBlock || block instanceof FenceGateBlock;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return HITBOX_SHAPES[ i ];
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return COLLISION_SHAPES[ i ];
    }

    @Override
    public VoxelShape getRenderShape( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return RENDER_SHAPES[ i ];
    }

    @Override
    public BlockState rotate( BlockState state, Rotation rot ) {
        switch( rot ) {
            case CLOCKWISE_180:
                return state.with( NORTH, state.get( SOUTH ) ).with( EAST, state.get( WEST ) ).with( SOUTH, state.get( NORTH ) ).with( WEST, state.get( EAST ) );
            case COUNTERCLOCKWISE_90:
                return state.with( NORTH, state.get( EAST ) ).with( EAST, state.get( SOUTH ) ).with( SOUTH, state.get( WEST ) ).with( WEST, state.get( NORTH ) );
            case CLOCKWISE_90:
                return state.with( NORTH, state.get( WEST ) ).with( EAST, state.get( NORTH ) ).with( SOUTH, state.get( EAST ) ).with( WEST, state.get( SOUTH ) );
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror( BlockState state, Mirror mirr ) {
        switch( mirr ) {
            case LEFT_RIGHT:
                return state.with( NORTH, state.get( SOUTH ) ).with( SOUTH, state.get( NORTH ) );
            case FRONT_BACK:
                return state.with( EAST, state.get( WEST ) ).with( WEST, state.get( EAST ) );
            default:
                return super.mirror( state, mirr );
        }
    }
}
