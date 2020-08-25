/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.base;

import natures.debris.common.blockold.fluid.WaterlogType;
import natures.debris.common.blockold.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
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
import java.util.stream.IntStream;

/**
 * Describes a stairs block. This is basically vanilla's default behaviour but with a twist.
 */
@SuppressWarnings("deprecation")
public class StairsBlock extends WaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    protected static final VoxelShape BOTTOM_SHAPE = makeCuboidShape(0, 0, 0, 16, 8, 16);
    protected static final VoxelShape TOP_SHAPE = makeCuboidShape(0, 8, 0, 16, 16, 16);
    protected static final VoxelShape EMPTY = VoxelShapes.empty();

    protected static final VoxelShape CORNER_000 = makeCuboidShape(0, 0, 0, 8, 8, 8);
    protected static final VoxelShape CORNER_001 = makeCuboidShape(0, 0, 8, 8, 8, 16);
    protected static final VoxelShape CORNER_010 = makeCuboidShape(0, 8, 0, 8, 16, 8);
    protected static final VoxelShape CORNER_011 = makeCuboidShape(0, 8, 8, 8, 16, 16);
    protected static final VoxelShape CORNER_100 = makeCuboidShape(8, 0, 0, 16, 8, 8);
    protected static final VoxelShape CORNER_101 = makeCuboidShape(8, 0, 8, 16, 8, 16);
    protected static final VoxelShape CORNER_110 = makeCuboidShape(8, 8, 0, 16, 16, 8);
    protected static final VoxelShape CORNER_111 = makeCuboidShape(8, 8, 8, 16, 16, 16);

    protected static final VoxelShape[] TOP_STAIRS_CONNS = createShapeArray(TOP_SHAPE, CORNER_000, CORNER_100, CORNER_001, CORNER_101);
    protected static final VoxelShape[] BOTTOM_STAIRS_CONNS = createShapeArray(BOTTOM_SHAPE, CORNER_010, CORNER_110, CORNER_011, CORNER_111);

    protected static final VoxelShape[] BOTTOM_STEP_CONNS = createShapeArray(EMPTY, CORNER_000, CORNER_100, CORNER_001, CORNER_101);
    protected static final VoxelShape[] TOP_STEP_CONNS = createShapeArray(EMPTY, CORNER_010, CORNER_110, CORNER_011, CORNER_111);

    private static final int[] CONN_INDEX_MAP = {
        12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8
    };

    private final boolean isStep;

    /**
     * @param isStep Whether this is a step instead of full stairs
     */
    public StairsBlock(boolean isStep, Properties properties) {
        super(properties);
        this.isStep = isStep;

        setDefaultState(stateContainer.getBaseState().with(SHAPE, StairsShape.STRAIGHT).with(HALF, Half.BOTTOM).with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(SHAPE, HALF, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx) {
        int index = CONN_INDEX_MAP[getConnIndex(state)];
        boolean top = state.get(HALF) == Half.TOP;
        if (isStep) return (top ? TOP_STEP_CONNS : BOTTOM_STEP_CONNS)[index];
        else return (top ? TOP_STAIRS_CONNS : BOTTOM_STAIRS_CONNS)[index];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction face = ctx.getFace();
        BlockState state = getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing())
                                            .with(HALF, face != Direction.DOWN && (face == Direction.UP || !(ctx.getHitVec().y % 1 > 0.5))
                                                        ? Half.BOTTOM
                                                        : Half.TOP)
                                            .with(WATERLOGGED, WaterlogType.getType(ctx.getWorld().getFluidState(ctx.getPos())));
        return state.with(SHAPE, computeStairsShape(state, ctx.getWorld(), ctx.getPos()));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        state = super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
        return facing.getAxis().isHorizontal()
               ? state.with(SHAPE, computeStairsShape(state, world, currentPos))
               : state;
    }

    /**
     * Checks whether the specified block state is a matching stairs block.
     */
    public boolean isBlockStairs(BlockState state) {
        return state.getBlock() instanceof StairsBlock && ((StairsBlock) state.getBlock()).isStep == isStep;
    }

    /**
     * Computes the stairs shape in the specified context.
     */
    private StairsShape computeStairsShape(BlockState state, IBlockReader world, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockState frontState = world.getBlockState(pos.offset(facing));
        if (isBlockStairs(frontState) && state.get(HALF) == frontState.get(HALF)) {
            Direction frontFacing = frontState.get(FACING);
            if (frontFacing.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, world, pos, frontFacing.getOpposite())) {
                if (frontFacing == facing.rotateYCCW()) {
                    return StairsShape.OUTER_LEFT;
                }
                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState backState = world.getBlockState(pos.offset(facing.getOpposite()));
        if (isBlockStairs(backState) && state.get(HALF) == backState.get(HALF)) {
            Direction backFacing = backState.get(FACING);
            if (backFacing.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, world, pos, backFacing)) {
                if (backFacing == facing.rotateYCCW()) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    /**
     * Checks if this block can <b>not</b> connect to an adjacent block.
     */
    private boolean isDifferentStairs(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        BlockState offState = world.getBlockState(pos.offset(face));
        return !isBlockStairs(offState) || offState.get(FACING) != state.get(FACING) || offState.get(HALF) != state.get(HALF);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        Direction facing = state.get(FACING);
        StairsShape shape = state.get(SHAPE);
        switch (mirrorIn) {
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
                        case STRAIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180);
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).with(SHAPE, StairsShape.OUTER_LEFT);
                    }
                }
        }

        return super.mirror(state, mirrorIn);
    }

    private static VoxelShape[] createShapeArray(VoxelShape base, VoxelShape conn1, VoxelShape conn2, VoxelShape conn3, VoxelShape conn4) {
        return IntStream.range(0, 16).mapToObj(connections -> joinShapesForConnection(connections, base, conn1, conn2, conn3, conn4)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape joinShapesForConnection(int connections, VoxelShape base, VoxelShape conn1, VoxelShape conn2, VoxelShape conn3, VoxelShape conn4) {
        VoxelShape shape = base;
        if ((connections & 1) != 0) {
            shape = VoxelShapes.or(shape, conn1);
        }

        if ((connections & 2) != 0) {
            shape = VoxelShapes.or(shape, conn2);
        }

        if ((connections & 4) != 0) {
            shape = VoxelShapes.or(shape, conn3);
        }

        if ((connections & 8) != 0) {
            shape = VoxelShapes.or(shape, conn4);
        }

        return shape;
    }

    /**
     * Returns the index in the voxel shape array for a specific state.
     */
    private static int getConnIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontalIndex();
    }
}
