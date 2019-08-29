/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 29 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.util.EWaterlogType;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockGlassPane extends BlockWaterlogged {
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

    public BlockGlassPane( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    public BlockGlassPane( String id, Properties properties ) {
        super( id, properties );

        setDefaultState( stateContainer.getBaseState()
                                       .with( NORTH, false )
                                       .with( EAST, false )
                                       .with( SOUTH, false )
                                       .with( WEST, false )
        );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( NORTH, EAST, SOUTH, WEST );
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
        boolean north = canPaneConnectTo( world, pos, EnumFacing.NORTH );
        boolean east = canPaneConnectTo( world, pos, EnumFacing.EAST );
        boolean south = canPaneConnectTo( world, pos, EnumFacing.SOUTH );
        boolean west = canPaneConnectTo( world, pos, EnumFacing.WEST );
        return getDefaultState().with( NORTH, north ).with( EAST, east ).with( SOUTH, south ).with( WEST, west ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    @Override
    public boolean canBeConnectedTo( IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing ) {
        IBlockState other = world.getBlockState( pos.offset( facing ) );
        return attachesTo( other, other.getBlockFaceShape( world, pos.offset( facing ), facing.getOpposite() ) );
    }

    private boolean canPaneConnectTo( IBlockReader world, BlockPos pos, EnumFacing facing ) {
        BlockPos offset = pos.offset( facing );
        IBlockState other = world.getBlockState( offset );
        return other.canBeConnectedTo( world, offset, facing.getOpposite() ) || getDefaultState().canBeConnectedTo( world, pos, facing );
    }

    public final boolean attachesTo( IBlockState state, BlockFaceShape shape ) {
        Block block = state.getBlock();
        return ! shouldSkipAttachment( block ) && shape == BlockFaceShape.SOLID || shape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    public static boolean shouldSkipAttachment( Block block ) {
        return block instanceof BlockShulkerBox ||
                block instanceof BlockLeaves ||
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
    public int quantityDropped( IBlockState state, Random random ) {
        return 0;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return true;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public boolean canSilkHarvest( IBlockState state, IWorldReader world, BlockPos pos, EntityPlayer player ) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return face.getAxis().isVertical() ? BlockFaceShape.CENTER_SMALL : BlockFaceShape.MIDDLE_POLE_THIN;
    }

    @Override
    protected ItemStack getSilkTouchDrop( IBlockState state ) {
        return new ItemStack( asItem() );
    }

    @OnlyIn( Dist.CLIENT )
    public boolean isSideInvisible( IBlockState state, IBlockState adjacentBlockState, EnumFacing side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
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
