/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 29 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
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

import javax.annotation.Nullable;

public class BlockWall extends BlockWaterlogged {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;

    private static final VoxelShape[] HITBOX_SHAPES = new VoxelShape[ 32 ];
    private static final VoxelShape[] COLLISION_SHAPES = new VoxelShape[ 32 ];

    static {
        VoxelShape pole = makeCuboidShape( 4, 0, 4, 12, 16, 12 );
        VoxelShape north = makeCuboidShape( 5, 0, 0, 11, 14, 8 );
        VoxelShape south = makeCuboidShape( 5, 0, 8, 11, 14, 16 );
        VoxelShape west = makeCuboidShape( 0, 0, 5, 8, 14, 11 );
        VoxelShape east = makeCuboidShape( 8, 0, 5, 16, 14, 11 );

        VoxelShape cpole = makeCuboidShape( 4, 0, 4, 12, 24, 12 );
        VoxelShape cnorth = makeCuboidShape( 5, 0, 0, 11, 24, 8 );
        VoxelShape csouth = makeCuboidShape( 5, 0, 8, 11, 24, 16 );
        VoxelShape cwest = makeCuboidShape( 0, 0, 5, 8, 24, 11 );
        VoxelShape ceast = makeCuboidShape( 8, 0, 5, 16, 24, 11 );

        for( int i = 0; i < 32; i++ ) {
            VoxelShape hitbox = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) hitbox = VoxelShapes.or( hitbox, north );
            if( ( i & 2 ) != 0 ) hitbox = VoxelShapes.or( hitbox, south );
            if( ( i & 4 ) != 0 ) hitbox = VoxelShapes.or( hitbox, west );
            if( ( i & 8 ) != 0 ) hitbox = VoxelShapes.or( hitbox, east );
            if( ( i & 16 ) != 0 ) hitbox = VoxelShapes.or( hitbox, pole );
            HITBOX_SHAPES[ i ] = hitbox;
            VoxelShape collision = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) collision = VoxelShapes.or( collision, cnorth );
            if( ( i & 2 ) != 0 ) collision = VoxelShapes.or( collision, csouth );
            if( ( i & 4 ) != 0 ) collision = VoxelShapes.or( collision, cwest );
            if( ( i & 8 ) != 0 ) collision = VoxelShapes.or( collision, ceast );
            if( ( i & 16 ) != 0 ) collision = VoxelShapes.or( collision, cpole );
            COLLISION_SHAPES[ i ] = collision;
        }
    }

    public BlockWall( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
                                       .with( UP, true )
        );
    }

    public BlockWall( String id, Properties properties ) {
        super( id, properties );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
                                       .with( UP, true )
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
            boolean up = ( ! north || east || ! south || west ) && ( north || ! east || south || ! west );
            return state.with( UP, up || doesBlockCausePole( world, pos.up(), world.getBlockState( pos.up() ) ) ).with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west );
        }
        return state;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        IWorld world = context.getWorld();
        BlockPos pos = context.getPos();
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        boolean north = canWallConnectTo( world, pos, EnumFacing.NORTH );
        boolean east = canWallConnectTo( world, pos, EnumFacing.EAST );
        boolean south = canWallConnectTo( world, pos, EnumFacing.SOUTH );
        boolean west = canWallConnectTo( world, pos, EnumFacing.WEST );
        boolean up = ( ! north || east || ! south || west ) && ( north || ! east || south || ! west );
        return getDefaultState().with( UP, up || doesBlockCausePole( world, pos.up(), world.getBlockState( pos.up() ) ) ).with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    public boolean doesBlockCausePole( IWorld world, BlockPos pos, IBlockState state ) {
        return ! state.isAir( world, pos );
    }

    private boolean attachesTo( IBlockState state, BlockFaceShape shape ) {
        Block block = state.getBlock();
        boolean isWallPole = shape == BlockFaceShape.MIDDLE_POLE_THICK || shape == BlockFaceShape.MIDDLE_POLE && isFenceGate( block );
        return ! isExcepBlockForAttachWithPiston( block ) && shape == BlockFaceShape.SOLID || isWallPole;
    }

    private boolean canWallConnectTo( IBlockReader world, BlockPos pos, EnumFacing facing ) {
        BlockPos off = pos.offset( facing );
        IBlockState other = world.getBlockState( off );
        return other.canBeConnectedTo( world, off, facing.getOpposite() ) || attachesTo( other, other.getBlockFaceShape( world, off, facing.getOpposite() ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( NORTH, EAST, SOUTH, WEST, UP );
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
        if( state.get( UP ) ) i |= 16;
        return HITBOX_SHAPES[ i ];
    }

    @Override
    public VoxelShape getCollisionShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        int i = 0;
        if( state.get( NORTH ) ) i |= 1;
        if( state.get( SOUTH ) ) i |= 2;
        if( state.get( WEST ) ) i |= 4;
        if( state.get( EAST ) ) i |= 8;
        if( state.get( UP ) ) i |= 16;
        return COLLISION_SHAPES[ i ];
    }

    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
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
