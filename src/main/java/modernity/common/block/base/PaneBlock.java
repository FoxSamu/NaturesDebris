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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Describes a pane block.
 */
public class PaneBlock extends WaterloggedBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final VoxelShape[] HITBOX_SHAPES = new VoxelShape[ 16 ];

    static {
        VoxelShape pole = makeCuboidShape( 7, 0, 7, 9, 16, 9 );
        VoxelShape north = makeCuboidShape( 7, 0, 0, 9, 16, 9 );
        VoxelShape south = makeCuboidShape( 7, 0, 8, 9, 16, 16 );
        VoxelShape west = makeCuboidShape( 0, 0, 7, 8, 16, 9 );
        VoxelShape east = makeCuboidShape( 8, 0, 7, 16, 16, 9 );

        for( int i = 0; i < 16; i++ ) {
            VoxelShape hitbox = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) hitbox = VoxelShapes.or( hitbox, north );
            if( ( i & 2 ) != 0 ) hitbox = VoxelShapes.or( hitbox, south );
            if( ( i & 4 ) != 0 ) hitbox = VoxelShapes.or( hitbox, west );
            if( ( i & 8 ) != 0 ) hitbox = VoxelShapes.or( hitbox, east );
            hitbox = VoxelShapes.or( hitbox, pole );
            HITBOX_SHAPES[ i ] = hitbox;
        }
    }


    public PaneBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( NORTH, EAST, SOUTH, WEST );
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
        boolean north = canPaneConnectTo( world, pos, Direction.NORTH );
        boolean east = canPaneConnectTo( world, pos, Direction.EAST );
        boolean south = canPaneConnectTo( world, pos, Direction.SOUTH );
        boolean west = canPaneConnectTo( world, pos, Direction.WEST );
        return getDefaultState().with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    @Override
    public boolean canBeConnectedTo( BlockState state, IBlockReader world, BlockPos pos, Direction facing ) {
        BlockState other = world.getBlockState( pos.offset( facing ) );
        return attachesTo( other, other.func_224755_d( world, pos.offset( facing ), facing.getOpposite() ), facing );
    }

    /**
     * Can we connect to a specific direction?
     */
    private boolean canPaneConnectTo( IBlockReader world, BlockPos pos, Direction facing ) {
        BlockPos offset = pos.offset( facing );
        BlockState other = world.getBlockState( offset );
        return other.canBeConnectedTo( world, offset, facing.getOpposite() ) || getDefaultState().canBeConnectedTo( world, pos, facing );
    }

    private boolean attachesTo( BlockState state, boolean solidSide, Direction facing ) {
        Block block = state.getBlock();
        return ! shouldSkipAttachment( block ) && solidSide || state.getBlock() instanceof PaneBlock;
    }

    /**
     * Checks if attaching can be ignored for these blocks.
     */
    public static boolean shouldSkipAttachment( Block block ) {
        return block instanceof ShulkerBoxBlock ||
                   block instanceof LeavesBlock ||
                   block == Blocks.BEACON ||
                   block == Blocks.CAULDRON ||
                   block == Blocks.GLOWSTONE ||
                   block == Blocks.ICE ||
                   block == Blocks.SEA_LANTERN ||
                   block == Blocks.PISTON ||
                   block == Blocks.STICKY_PISTON ||
                   block == Blocks.PISTON_HEAD ||
                   block == Blocks.MELON ||
                   block == Blocks.PUMPKIN ||
                   block == Blocks.CARVED_PUMPKIN ||
                   block == Blocks.JACK_O_LANTERN ||
                   block == Blocks.BARRIER;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean isSideInvisible( BlockState state, BlockState adjacentBlockState, Direction side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
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
