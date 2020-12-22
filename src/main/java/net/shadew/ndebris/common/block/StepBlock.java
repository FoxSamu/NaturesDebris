package net.shadew.ndebris.common.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class StepBlock extends Block implements Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape AABB_SLAB_TOP = createCuboidShape(0, 8, 0, 16, 16, 16);
    protected static final VoxelShape AABB_SLAB_BOTTOM = createCuboidShape(0, 0, 0, 16, 8, 16);

    protected static final VoxelShape NWD_CORNER = createCuboidShape(0, 0, 0, 8, 8, 8);
    protected static final VoxelShape SWD_CORNER = createCuboidShape(0, 0, 8, 8, 8, 16);
    protected static final VoxelShape NWU_CORNER = createCuboidShape(0, 8, 0, 8, 16, 8);
    protected static final VoxelShape SWU_CORNER = createCuboidShape(0, 8, 8, 8, 16, 16);
    protected static final VoxelShape NED_CORNER = createCuboidShape(8, 0, 0, 16, 8, 8);
    protected static final VoxelShape SED_CORNER = createCuboidShape(8, 0, 8, 16, 8, 16);
    protected static final VoxelShape NEU_CORNER = createCuboidShape(8, 8, 0, 16, 16, 8);
    protected static final VoxelShape SEU_CORNER = createCuboidShape(8, 8, 8, 16, 16, 16);

    protected static final VoxelShape[] UPPER_SHAPES = makeShapes(
        NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER
    );
    protected static final VoxelShape[] LOWER_SHAPES = makeShapes(
        NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER
    );

    private static final int[] STATE_TO_SHAPE_BITMASK = {
        // S       W       N       E
        0b1100, 0b0101, 0b0011, 0b1010, // Straight
        0b1110, 0b1101, 0b0111, 0b1011, // Inner left
        0b1101, 0b0111, 0b1011, 0b1110, // Inner right
        0b1000, 0b0100, 0b0001, 0b0010, // Outer left
        0b0100, 0b0001, 0b0010, 0b1000  // Outer right
    };

    private static VoxelShape[] makeShapes(VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        return IntStream.range(0, 16).mapToObj(bits -> combineShapes(
            bits,
            nwCorner, neCorner,
            swCorner, seCorner
        )).toArray(VoxelShape[]::new);
    }

    private static VoxelShape combineShapes(int bits, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        VoxelShape shape = VoxelShapes.empty();
        if ((bits & 1) != 0) {
            shape = nwCorner;
        }

        if ((bits & 2) != 0) {
            shape = VoxelShapes.union(shape, neCorner);
        }

        if ((bits & 4) != 0) {
            shape = VoxelShapes.union(shape, swCorner);
        }

        if ((bits & 8) != 0) {
            shape = VoxelShapes.union(shape, seCorner);
        }

        return shape;
    }

    public StepBlock(AbstractBlock.Settings props) {
        super(props);
        setDefaultState(
            stateManager.getDefaultState()
                        .with(FACING, Direction.NORTH)
                        .with(HALF, BlockHalf.BOTTOM)
                        .with(SHAPE, StairShape.STRAIGHT)
                        .with(WATERLOGGED, false)
        );
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape[] shapes = state.get(HALF) == BlockHalf.TOP
                              ? UPPER_SHAPES
                              : LOWER_SHAPES;
        return shapes[STATE_TO_SHAPE_BITMASK[stateIndex(state)]];
    }

    private int stateIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontal();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction dir = ctx.getSide();
        BlockPos pos = ctx.getBlockPos();

        BlockHalf half;
        if (dir == Direction.DOWN) {
            half = BlockHalf.TOP;
        } else if (dir == Direction.UP) {
            half = BlockHalf.BOTTOM;
        } else {
            half = ctx.getHitPos().y - pos.getY() <= 0.5
                   ? BlockHalf.BOTTOM
                   : BlockHalf.TOP;
        }

        FluidState fstate = ctx.getWorld().getFluidState(pos);
        BlockState bstate = getDefaultState().with(FACING, ctx.getPlayerFacing())
                                             .with(HALF, half)
                                             .with(WATERLOGGED, fstate.getFluid() == Fluids.WATER);

        return bstate.with(SHAPE, connect(bstate, ctx.getWorld(), pos));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState adjState, WorldAccess world, BlockPos pos, BlockPos adjPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler()
                 .schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return facing.getAxis().isHorizontal()
               ? state.with(SHAPE, connect(state, world, pos))
               : super.getStateForNeighborUpdate(state, facing, adjState, world, pos, adjPos);
    }

    private static StairShape connect(BlockState myState, BlockView world, BlockPos pos) {
        Direction myFacing = myState.get(FACING);

        // Check back side, generating outer corner
        BlockState backState = world.getBlockState(pos.offset(myFacing));
        if (isStepBlock(backState) && myState.get(HALF) == backState.get(HALF)) {
            Direction backFacing = backState.get(FACING);
            if (backFacing.getAxis() != myState.get(FACING).getAxis() && isDifferentStep(myState, world, pos, backFacing.getOpposite())) {
                return backFacing == myFacing.rotateYCounterclockwise()
                       ? StairShape.OUTER_LEFT
                       : StairShape.OUTER_RIGHT;
            }
        }

        // Check front side, generating inner corner
        BlockState frontState = world.getBlockState(pos.offset(myFacing.getOpposite()));
        if (isStepBlock(frontState) && myState.get(HALF) == frontState.get(HALF)) {
            Direction frontFacing = frontState.get(FACING);
            if (frontFacing.getAxis() != myState.get(FACING).getAxis() && isDifferentStep(myState, world, pos, frontFacing)) {
                return frontFacing == myFacing.rotateYCounterclockwise()
                       ? StairShape.INNER_LEFT
                       : StairShape.INNER_RIGHT;
            }
        }

        return StairShape.STRAIGHT;
    }

    private static boolean isDifferentStep(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState otherState = world.getBlockState(pos.offset(dir));
        return !isStepBlock(otherState)
                   || otherState.get(FACING) != state.get(FACING)
                   || otherState.get(HALF) != state.get(HALF);
    }

    public static boolean isStepBlock(BlockState state) {
        return state.getBlock() instanceof StepBlock;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction facing = state.get(FACING);
        StairShape shape = state.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (facing.getAxis() == Direction.Axis.Z) {
                    switch (shape) {
                        case INNER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                        default:
                            return state.rotate(BlockRotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (facing.getAxis() == Direction.Axis.X) {
                    switch (shape) {
                        case INNER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                        case STRAIGHT:
                            return state.rotate(BlockRotation.CLOCKWISE_180);
                    }
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED)
               ? Fluids.WATER.getStill(false)
               : super.getFluidState(state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.WATER && world.getFluidState(pos).isIn(FluidTags.WATER);
    }
}
