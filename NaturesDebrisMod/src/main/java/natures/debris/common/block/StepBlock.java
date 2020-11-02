package natures.debris.common.block;

import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

@SuppressWarnings("deprecation")
public class StepBlock extends Block implements IWaterLoggable {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_SLAB_TOP = makeCuboidShape(0, 8, 0, 16, 16, 16);
    protected static final VoxelShape AABB_SLAB_BOTTOM = makeCuboidShape(0, 0, 0, 16, 8, 16);

    protected static final VoxelShape NWD_CORNER = makeCuboidShape(0, 0, 0, 8, 8, 8);
    protected static final VoxelShape SWD_CORNER = makeCuboidShape(0, 0, 8, 8, 8, 16);
    protected static final VoxelShape NWU_CORNER = makeCuboidShape(0, 8, 0, 8, 16, 8);
    protected static final VoxelShape SWU_CORNER = makeCuboidShape(0, 8, 8, 8, 16, 16);
    protected static final VoxelShape NED_CORNER = makeCuboidShape(8, 0, 0, 16, 8, 8);
    protected static final VoxelShape SED_CORNER = makeCuboidShape(8, 0, 8, 16, 8, 16);
    protected static final VoxelShape NEU_CORNER = makeCuboidShape(8, 8, 0, 16, 16, 8);
    protected static final VoxelShape SEU_CORNER = makeCuboidShape(8, 8, 8, 16, 16, 16);

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
            shape = VoxelShapes.or(shape, neCorner);
        }

        if ((bits & 4) != 0) {
            shape = VoxelShapes.or(shape, swCorner);
        }

        if ((bits & 8) != 0) {
            shape = VoxelShapes.or(shape, seCorner);
        }

        return shape;
    }

    public StepBlock(Block.Properties props) {
        super(props);
        setDefaultState(
            stateContainer.getBaseState()
                          .with(FACING, Direction.NORTH)
                          .with(HALF, Half.BOTTOM)
                          .with(SHAPE, StairsShape.STRAIGHT)
                          .with(WATERLOGGED, false)
        );
    }

    @Override // isTransparent
    public boolean func_220074_n(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape[] shapes = state.get(HALF) == Half.TOP
                              ? UPPER_SHAPES
                              : LOWER_SHAPES;
        return shapes[STATE_TO_SHAPE_BITMASK[stateIndex(state)]];
    }

    private int stateIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontalIndex();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction dir = ctx.getFace();
        BlockPos pos = ctx.getPos();

        Half half;
        if (dir == Direction.DOWN) {
            half = Half.TOP;
        } else if (dir == Direction.UP) {
            half = Half.BOTTOM;
        } else {
            half = ctx.getHitVec().y - pos.getY() <= 0.5
                   ? Half.BOTTOM
                   : Half.TOP;
        }

        FluidState fstate = ctx.getWorld().getFluidState(pos);
        BlockState bstate = getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing())
                                             .with(HALF, half)
                                             .with(WATERLOGGED, fstate.getFluid() == Fluids.WATER);

        return bstate.with(SHAPE, connect(bstate, ctx.getWorld(), pos));
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks()
                 .scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return facing.getAxis().isHorizontal()
               ? state.with(SHAPE, connect(state, world, pos))
               : super.updatePostPlacement(state, facing, adjState, world, pos, adjPos);
    }

    private static StairsShape connect(BlockState myState, IBlockReader world, BlockPos pos) {
        Direction myFacing = myState.get(FACING);

        // Check back side, generating outer corner
        BlockState backState = world.getBlockState(pos.offset(myFacing));
        if (isStepBlock(backState) && myState.get(HALF) == backState.get(HALF)) {
            Direction backFacing = backState.get(FACING);
            if (backFacing.getAxis() != myState.get(FACING).getAxis() && isDifferentStep(myState, world, pos, backFacing.getOpposite())) {
                return backFacing == myFacing.rotateYCCW()
                       ? StairsShape.OUTER_LEFT
                       : StairsShape.OUTER_RIGHT;
            }
        }

        // Check front side, generating inner corner
        BlockState frontState = world.getBlockState(pos.offset(myFacing.getOpposite()));
        if (isStepBlock(frontState) && myState.get(HALF) == frontState.get(HALF)) {
            Direction frontFacing = frontState.get(FACING);
            if (frontFacing.getAxis() != myState.get(FACING).getAxis() && isDifferentStep(myState, world, pos, frontFacing)) {
                return frontFacing == myFacing.rotateYCCW()
                       ? StairsShape.INNER_LEFT
                       : StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean isDifferentStep(BlockState state, IBlockReader world, BlockPos pos, Direction dir) {
        BlockState otherState = world.getBlockState(pos.offset(dir));
        return !isStepBlock(otherState)
                   || otherState.get(FACING) != state.get(FACING)
                   || otherState.get(HALF) != state.get(HALF);
    }

    public static boolean isStepBlock(BlockState state) {
        return state.getBlock() instanceof StepBlock;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirr) {
        Direction facing = state.get(FACING);
        StairsShape shape = state.get(SHAPE);
        switch (mirr) {
            case LEFT_RIGHT:
                if (facing.getAxis() == Direction.Axis.Z) {
                    switch (shape) {
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (facing.getAxis() == Direction.Axis.X) {
                    switch (shape) {
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.mirror(state, mirr);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED)
               ? Fluids.WATER.getStillFluidState(false)
               : super.getFluidState(state);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
        return type == PathType.WATER && world.getFluidState(pos).isTagged(FluidTags.WATER);
    }
}
