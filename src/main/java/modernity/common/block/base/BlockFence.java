/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 30 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import modernity.api.util.EWaterlogType;

import javax.annotation.Nullable;

public class BlockFence extends BlockWaterlogged {
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

    public BlockFence( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    public BlockFence( String id, Properties properties ) {
        super( id, properties );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, pos, facingPos );

        if( facing != EnumFacing.DOWN ) {
            boolean north = facing == EnumFacing.NORTH ? attachesTo( facingState, facingState.getBlockFaceShape( world, facingPos, facing.getOpposite() ) ) : state.get( NORTH );
            boolean east = facing == EnumFacing.EAST ? attachesTo( facingState, facingState.getBlockFaceShape( world, facingPos, facing.getOpposite() ) ) : state.get( EAST );
            boolean south = facing == EnumFacing.SOUTH ? attachesTo( facingState, facingState.getBlockFaceShape( world, facingPos, facing.getOpposite() ) ) : state.get( SOUTH );
            boolean west = facing == EnumFacing.WEST ? attachesTo( facingState, facingState.getBlockFaceShape( world, facingPos, facing.getOpposite() ) ) : state.get( WEST );
            return state.with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west );
        }
        return state;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        IWorld world = context.getWorld();
        BlockPos pos = context.getPos();
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        boolean north = canFenceConnectTo( world, pos, EnumFacing.NORTH );
        boolean east = canFenceConnectTo( world, pos, EnumFacing.EAST );
        boolean south = canFenceConnectTo( world, pos, EnumFacing.SOUTH );
        boolean west = canFenceConnectTo( world, pos, EnumFacing.WEST );
        return getDefaultState().with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    private boolean attachesTo( IBlockState state, BlockFaceShape shape ) {
        Block block = state.getBlock();
        boolean isFencePole = shape == BlockFaceShape.MIDDLE_POLE && ( state.getMaterial() == material || isFenceGate( block ) );
        return ! isExcepBlockForAttachWithPiston( block ) && shape == BlockFaceShape.SOLID || isFencePole;
    }

    private boolean canFenceConnectTo( IBlockReader world, BlockPos pos, EnumFacing facing ) {
        BlockPos off = pos.offset( facing );
        IBlockState other = world.getBlockState( off );
        return other.canBeConnectedTo( world, off, facing.getOpposite() ) || attachesTo( other, other.getBlockFaceShape( world, off, facing.getOpposite() ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( NORTH, EAST, SOUTH, WEST );
    }

    public static boolean isExcepBlockForAttachWithPiston( Block block ) {
        return Block.isExceptBlockForAttachWithPiston( block ) || block == Blocks.BARRIER || block == Blocks.MELON || block == Blocks.PUMPKIN || block == Blocks.CARVED_PUMPKIN || block == Blocks.JACK_O_LANTERN || block == Blocks.FROSTED_ICE || block == Blocks.TNT;
    }

    private boolean isFenceGate( Block block ) {
        return block instanceof modernity.common.block.base.BlockFenceGate || block instanceof BlockFenceGate;
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return HITBOX_SHAPES[ i ];
    }

    @Override
    public VoxelShape getCollisionShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return COLLISION_SHAPES[ i ];
    }

    @Override
    public VoxelShape getRenderShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        return RENDER_SHAPES[ i ];
    }

    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    public IBlockState rotate( IBlockState state, Rotation rot ) {
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

    public IBlockState mirror( IBlockState state, Mirror mirr ) {
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
