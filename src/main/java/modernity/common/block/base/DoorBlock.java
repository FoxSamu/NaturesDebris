package modernity.common.block.base;

import modernity.api.util.Events;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Describes a door block. This is vanilla's door implementation with a few tweaks.
 */
@SuppressWarnings( "deprecation" )
public class DoorBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape( 0, 0, 0, 16, 16, 3 );
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape( 0, 0, 13, 16, 16, 16 );
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape( 13, 0, 0, 16, 16, 16 );
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape( 0, 0, 0, 3, 16, 16 );

    public DoorBlock( Block.Properties builder ) {
        super( builder );
        this.setDefaultState(
            stateContainer.getBaseState()
                          .with( FACING, Direction.NORTH )
                          .with( OPEN, false )
                          .with( HINGE, DoorHingeSide.LEFT )
                          .with( POWERED, false )
                          .with( HALF, DoubleBlockHalf.LOWER )
        );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        Direction facing = state.get( FACING );
        boolean closed = ! state.get( OPEN );
        boolean rightHinge = state.get( HINGE ) == DoorHingeSide.RIGHT;
        switch( facing ) {
            case EAST:
            default:
                return closed ? EAST_AABB : rightHinge ? NORTH_AABB : SOUTH_AABB;
            case SOUTH:
                return closed ? SOUTH_AABB : rightHinge ? EAST_AABB : WEST_AABB;
            case WEST:
                return closed ? WEST_AABB : rightHinge ? SOUTH_AABB : NORTH_AABB;
            case NORTH:
                return closed ? NORTH_AABB : rightHinge ? WEST_AABB : EAST_AABB;
        }
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState adjacentState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        DoubleBlockHalf half = state.get( HALF );
        if( facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == ( facing == Direction.UP ) ) {
            return adjacentState.getBlock() == this && adjacentState.get( HALF ) != half
                   ? state.with( FACING, adjacentState.get( FACING ) )
                          .with( OPEN, adjacentState.get( OPEN ) )
                          .with( HINGE, adjacentState.get( HINGE ) )
                          .with( POWERED, adjacentState.get( POWERED ) )
                   : Blocks.AIR.getDefaultState();
        } else {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && ! state.isValidPosition( world, currentPos )
                   ? Blocks.AIR.getDefaultState()
                   : super.updatePostPlacement( state, facing, adjacentState, world, currentPos, facingPos );
        }
    }

    @Override
    public void harvestBlock( World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack ) {
        super.harvestBlock( world, player, pos, Blocks.AIR.getDefaultState(), te, stack );
    }

    @Override
    public void onBlockHarvested( World world, BlockPos pos, BlockState state, PlayerEntity player ) {
        DoubleBlockHalf half = state.get( HALF );
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherState = world.getBlockState( otherPos );
        if( otherState.getBlock() == this && otherState.get( HALF ) != half ) {
            world.setBlockState( otherPos, Blocks.AIR.getDefaultState(), 35 );
            world.playEvent( player, Events.BREAK_BLOCK, otherPos, Block.getStateId( otherState ) );
            ItemStack itemstack = player.getHeldItemMainhand();
            if( ! world.isRemote && ! player.isCreative() ) {
                Block.spawnDrops( state, world, pos, null, player, itemstack );
                Block.spawnDrops( otherState, world, otherPos, null, player, itemstack );
            }
        }

        super.onBlockHarvested( world, pos, state, player );
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        switch( type ) {
            case LAND:
            case AIR:
                return state.get( OPEN );
            case WATER:
            default:
                return false;
        }
    }

    /**
     * Gets the close sound event
     */
    private int getCloseSound() {
        return this.material == Material.IRON ? Events.IRON_DOOR_CLOSE : Events.WOODEN_DOOR_CLOSE;
    }

    /**
     * Gets the opening sound event
     */
    private int getOpenSound() {
        return this.material == Material.IRON ? Events.IRON_DOOR_OPEN : Events.WOODEN_DOOR_OPEN;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        BlockPos pos = context.getPos();
        if( pos.getY() < 255 && context.getWorld().getBlockState( pos.up() ).isReplaceable( context ) ) {
            World world = context.getWorld();
            boolean powered = world.isBlockPowered( pos ) || world.isBlockPowered( pos.up() );
            return this.getDefaultState()
                       .with( FACING, context.getPlacementHorizontalFacing() )
                       .with( HINGE, this.getHingeSide( context ) )
                       .with( POWERED, powered )
                       .with( OPEN, powered )
                       .with( HALF, DoubleBlockHalf.LOWER );
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack ) {
        world.setBlockState( pos.up(), state.with( HALF, DoubleBlockHalf.UPPER ), 3 );
    }

    /**
     * Computes the hinge side in a specific context
     */
    private DoorHingeSide getHingeSide( BlockItemUseContext ctx ) {
        IBlockReader world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        Direction facing = ctx.getPlacementHorizontalFacing();
        BlockPos upPos = pos.up();

        Direction rightFacing = facing.rotateYCCW();
        BlockPos rightPos = pos.offset( rightFacing );
        BlockState rightState = world.getBlockState( rightPos );
        BlockPos rightUpPos = upPos.offset( rightFacing );
        BlockState rightUpState = world.getBlockState( rightUpPos );

        Direction leftFacing = facing.rotateY();
        BlockPos leftPos = pos.offset( leftFacing );
        BlockState leftState = world.getBlockState( leftPos );
        BlockPos leftUpPos = upPos.offset( leftFacing );
        BlockState leftUpState = world.getBlockState( leftUpPos );

        int hingeWeight = (
            rightState.func_224756_o( world, rightPos ) ? - 1 : 0
        ) + (
            rightUpState.func_224756_o( world, rightUpPos ) ? - 1 : 0
        ) + (
            leftState.func_224756_o( world, leftPos ) ? 1 : 0
        ) + (
            leftUpState.func_224756_o( world, leftUpPos ) ? 1 : 0
        );

        boolean rightDoor = rightState.getBlock() == this && rightState.get( HALF ) == DoubleBlockHalf.LOWER;
        boolean leftDoor = leftState.getBlock() == this && leftState.get( HALF ) == DoubleBlockHalf.LOWER;

        if( ( ! rightDoor || leftDoor ) && hingeWeight <= 0 ) {
            if( ( ! leftDoor || rightDoor ) && hingeWeight >= 0 ) {
                int xoff = facing.getXOffset();
                int zoff = facing.getZOffset();
                Vec3d vec3d = ctx.getHitVec();
                double hitX = vec3d.x - (double) pos.getX();
                double hitZ = vec3d.z - (double) pos.getZ();

                return ( xoff >= 0 || ! ( hitZ < 0.5 ) ) &&
                           ( xoff <= 0 || ! ( hitZ > 0.5 ) ) &&
                           ( zoff >= 0 || ! ( hitX > 0.5 ) ) &&
                           ( zoff <= 0 || ! ( hitX < 0.5 ) )
                       ? DoorHingeSide.LEFT
                       : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( this.material == Material.IRON ) {
            return false;
        } else {
            state = state.cycle( OPEN );
            world.setBlockState( pos, state, 10 );
            world.playEvent( player, state.get( OPEN ) ? getOpenSound() : getCloseSound(), pos, 0 );
            return true;
        }
    }

    /**
     * Toggles the door
     */
    public void toggleDoor( World worldIn, BlockPos pos, boolean open ) {
        BlockState state = worldIn.getBlockState( pos );
        if( state.getBlock() == this && state.get( OPEN ) != open ) {
            worldIn.setBlockState( pos, state.with( OPEN, open ), 10 );
            this.playSound( worldIn, pos, open );
        }
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving ) {
        boolean powered = world.isBlockPowered( pos ) || world.isBlockPowered(
            pos.offset(
                state.get( HALF ) == DoubleBlockHalf.LOWER
                ? Direction.UP
                : Direction.DOWN
            )
        );
        if( block != this && powered != state.get( POWERED ) ) {
            if( powered != state.get( OPEN ) ) {
                this.playSound( world, pos, powered );
            }

            world.setBlockState( pos, state.with( POWERED, powered ).with( OPEN, powered ), 2 );
        }

    }

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader worldIn, BlockPos pos ) {
        BlockPos posBelow = pos.down();
        BlockState stateBelow = worldIn.getBlockState( posBelow );
        if( state.get( HALF ) == DoubleBlockHalf.LOWER ) {
            return stateBelow.func_224755_d( worldIn, posBelow, Direction.UP );
        } else {
            return stateBelow.getBlock() == this;
        }
    }

    /**
     * Plays the door sound
     */
    protected void playSound( World world, BlockPos pos, boolean open ) {
        world.playEvent( null, open ? getOpenSound() : getCloseSound(), pos, 0 );
    }

    @Override
    public PushReaction getPushReaction( BlockState state ) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockState rotate( BlockState state, Rotation rot ) {
        return state.with( FACING, rot.rotate( state.get( FACING ) ) );
    }

    @Override
    public BlockState mirror( BlockState state, Mirror mirror ) {
        return mirror == Mirror.NONE
               ? state
               : state.rotate( mirror.toRotation( state.get( FACING ) ) ).cycle( HINGE );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public long getPositionRandom( BlockState state, BlockPos pos ) {
        return MathHelper.getCoordinateRandom(
            pos.getX(),
            pos.down(
                state.get( HALF ) == DoubleBlockHalf.LOWER
                ? 0
                : 1
            ).getY(),
            pos.getZ()
        );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( HALF, FACING, OPEN, HINGE, POWERED );
    }

    @Override
    public boolean hasCustomBreakingProgress( BlockState state ) {
        return super.hasCustomBreakingProgress( state );
    }
}