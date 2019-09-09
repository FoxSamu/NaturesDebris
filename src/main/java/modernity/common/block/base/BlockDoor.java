/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 29 - 2019
 */

package modernity.common.block.base;

import modernity.common.util.Events;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@SuppressWarnings( "deprecation" )
public class BlockDoor extends BlockBase {
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape SOUTH_AABB = makeCuboidShape( 0, 0, 0, 16, 16, 3 );
    protected static final VoxelShape NORTH_AABB = makeCuboidShape( 0, 0, 13, 16, 16, 16 );
    protected static final VoxelShape WEST_AABB = makeCuboidShape( 13, 0, 0, 16, 16, 16 );
    protected static final VoxelShape EAST_AABB = makeCuboidShape( 0, 0, 0, 3, 16, 16 );

    public BlockDoor( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        setDefaultState( stateContainer.getBaseState()
                                       .with( FACING, EnumFacing.NORTH )
                                       .with( OPEN, false )
                                       .with( HINGE, DoorHingeSide.LEFT )
                                       .with( POWERED, false )
                                       .with( HALF, DoubleBlockHalf.LOWER )
        );
    }

    public BlockDoor( String id, Properties properties ) {
        super( id, properties );
        setDefaultState( stateContainer.getBaseState()
                                       .with( FACING, EnumFacing.NORTH )
                                       .with( OPEN, false )
                                       .with( HINGE, DoorHingeSide.LEFT )
                                       .with( POWERED, false )
                                       .with( HALF, DoubleBlockHalf.LOWER )
        );
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        EnumFacing facing = state.get( FACING );
        boolean closed = ! state.get( OPEN );
        boolean rightSide = state.get( HINGE ) == DoorHingeSide.RIGHT;
        switch( facing ) {
            case EAST:
            default:
                return closed ? EAST_AABB : rightSide ? NORTH_AABB : SOUTH_AABB;
            case SOUTH:
                return closed ? SOUTH_AABB : rightSide ? EAST_AABB : WEST_AABB;
            case WEST:
                return closed ? WEST_AABB : rightSide ? SOUTH_AABB : NORTH_AABB;
            case NORTH:
                return closed ? NORTH_AABB : rightSide ? WEST_AABB : EAST_AABB;
        }
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        DoubleBlockHalf half = state.get( HALF );
        if( facing.getAxis() == EnumFacing.Axis.Y && half == DoubleBlockHalf.LOWER == ( facing == EnumFacing.UP ) ) {
            return facingState.getBlock() == this && facingState.get( HALF ) != half ?
                    state.with( FACING, facingState.get( FACING ) )
                         .with( OPEN, facingState.get( OPEN ) )
                         .with( HINGE, facingState.get( HINGE ) )
                         .with( POWERED, facingState.get( POWERED ) ) :
                    Blocks.AIR.getDefaultState();
        } else {
            return half == DoubleBlockHalf.LOWER && facing == EnumFacing.DOWN && ! state.isValidPosition( world, currentPos ) ?
                    Blocks.AIR.getDefaultState() :
                    super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        }
    }

    @Override
    public void harvestBlock( World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack ) {
        super.harvestBlock( world, player, pos, Blocks.AIR.getDefaultState(), te, stack );
    }

    @Override
    public void onBlockHarvested( World world, BlockPos pos, IBlockState state, EntityPlayer player ) {
        DoubleBlockHalf half = state.get( HALF );
        boolean isLower = half == DoubleBlockHalf.LOWER;
        BlockPos otherPos = isLower ? pos.up() : pos.down();

        IBlockState otherState = world.getBlockState( otherPos );
        if( otherState.getBlock() == this && otherState.get( HALF ) != half ) {
            world.setBlockState( otherPos, Blocks.AIR.getDefaultState(), 35 );
            world.playEvent( player, Events.BREAK_BLOCK, otherPos, getStateId( otherState ) );
            if( ! world.isRemote && ! player.isCreative() ) {
                if( isLower ) {
                    state.dropBlockAsItem( world, pos, 0 );
                } else {
                    otherState.dropBlockAsItem( world, otherPos, 0 );
                }
            }
        }

        super.onBlockHarvested( world, pos, state, player );
    }

    @Override
    public boolean allowsMovement( IBlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        switch( type ) {
            case LAND:
                return state.get( OPEN );
            case WATER:
                return false;
            case AIR:
                return state.get( OPEN );
            default:
                return false;
        }
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    private int getCloseSound() {
        return this.material == Material.IRON ? Events.IRON_DOOR_CLOSE : Events.WOODEN_DOOR_CLOSE;
    }

    private int getOpenSound() {
        return this.material == Material.IRON ? Events.IRON_DOOR_OPEN : Events.WOODEN_DOOR_OPEN;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        BlockPos pos = context.getPos();
        if( pos.getY() < 255 && context.getWorld().getBlockState( pos.up() ).isReplaceable( context ) ) {
            World world = context.getWorld();
            boolean powered = world.isBlockPowered( pos ) || world.isBlockPowered( pos.up() );
            return getDefaultState().with( FACING, context.getPlacementHorizontalFacing() )
                                    .with( HINGE, getHingeSide( context ) )
                                    .with( POWERED, powered )
                                    .with( OPEN, powered )
                                    .with( HALF, DoubleBlockHalf.LOWER );
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack ) {
        worldIn.setBlockState( pos.up(), state.with( HALF, DoubleBlockHalf.UPPER ), 3 );
    }

    private DoorHingeSide getHingeSide( BlockItemUseContext ctx ) {
        IBlockReader world = ctx.getWorld();
        BlockPos downPos = ctx.getPos();
        BlockPos upPos = downPos.up();

        EnumFacing facing = ctx.getPlacementHorizontalFacing();

        EnumFacing rightFacing = facing.rotateYCCW();
        IBlockState rightDown = world.getBlockState( downPos.offset( rightFacing ) );
        IBlockState rightUp = world.getBlockState( upPos.offset( rightFacing ) );

        EnumFacing leftFacing = facing.rotateY();
        IBlockState leftDown = world.getBlockState( downPos.offset( leftFacing ) );
        IBlockState leftUp = world.getBlockState( upPos.offset( leftFacing ) );

        int connBlocks = ( rightDown.isBlockNormalCube() ? - 1 : 0 ) + ( rightUp.isBlockNormalCube() ? - 1 : 0 ) + ( leftDown.isBlockNormalCube() ? 1 : 0 ) + ( leftUp.isBlockNormalCube() ? 1 : 0 );
        boolean isRightThis = rightDown.getBlock() == this && rightDown.get( HALF ) == DoubleBlockHalf.LOWER;
        boolean isLeftThis = leftDown.getBlock() == this && leftDown.get( HALF ) == DoubleBlockHalf.LOWER;
        if( ( ! isRightThis || isLeftThis ) && connBlocks <= 0 ) {
            if( ( ! isLeftThis || isRightThis ) && connBlocks >= 0 ) {
                int xOff = facing.getXOffset();
                int zOff = facing.getZOffset();
                float hitX = ctx.getHitX();
                float hitZ = ctx.getHitZ();
                return ( xOff >= 0 || ! ( hitZ < 0.5F ) ) && ( xOff <= 0 || ! ( hitZ > 0.5F ) ) && ( zOff >= 0 || ! ( hitX > 0.5F ) ) && ( zOff <= 0 || ! ( hitX < 0.5F ) ) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Override
    public boolean onBlockActivated( IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ ) {
        if( this.material == Material.IRON ) {
            return false;
        } else {
            state = state.cycle( OPEN );
            world.setBlockState( pos, state, 10 );
            world.playEvent( player, state.get( OPEN ) ? getOpenSound() : getCloseSound(), pos, 0 );
            return true;
        }
    }

    public void toggleDoor( World world, BlockPos pos, boolean open ) {
        IBlockState state = world.getBlockState( pos );
        if( state.getBlock() == this && state.get( OPEN ) != open ) {
            world.setBlockState( pos, state.with( OPEN, open ), 10 );
            playSound( world, pos, open );
        }
    }

    private void playSound( World world, BlockPos pos, boolean open ) {
        world.playEvent( null, open ? getOpenSound() : getCloseSound(), pos, 0 );
    }

    @Override
    public void neighborChanged( IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos ) {
        boolean doorPowered = world.isBlockPowered( pos ) || world.isBlockPowered( pos.offset( state.get( HALF ) == DoubleBlockHalf.LOWER ? EnumFacing.UP : EnumFacing.DOWN ) );
        if( block != this && doorPowered != state.get( POWERED ) ) {
            if( doorPowered != state.get( OPEN ) ) {
                this.playSound( world, pos, doorPowered );
            }

            world.setBlockState( pos, state.with( POWERED, doorPowered ).with( OPEN, doorPowered ), 2 );
        }

    }

    @Override
    public boolean isValidPosition( IBlockState state, IWorldReaderBase world, BlockPos pos ) {
        IBlockState stateBelow = world.getBlockState( pos.down() );
        if( state.get( HALF ) == DoubleBlockHalf.LOWER ) {
            return stateBelow.isTopSolid();
        } else {
            return stateBelow.getBlock() == this;
        }
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World world, BlockPos pos, int fortune ) {
        return state.get( HALF ) == DoubleBlockHalf.UPPER ? Items.AIR : super.getItemDropped( state, world, pos, fortune );
    }

    @Override
    public EnumPushReaction getPushReaction( IBlockState state ) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState rotate( IBlockState state, Rotation rot ) {
        return state.with( FACING, rot.rotate( state.get( FACING ) ) );
    }

    @Override
    public IBlockState mirror( IBlockState state, Mirror mirr ) {
        return mirr == Mirror.NONE ? state : state.rotate( mirr.toRotation( state.get( FACING ) ) ).cycle( HINGE );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public long getPositionRandom( IBlockState state, BlockPos pos ) {
        return MathHelper.getCoordinateRandom( pos.getX(), pos.down( state.get( HALF ) == DoubleBlockHalf.LOWER ? 0 : 1 ).getY(), pos.getZ() );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( HALF, FACING, OPEN, HINGE, POWERED );
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return BlockFaceShape.UNDEFINED;
    }
}
