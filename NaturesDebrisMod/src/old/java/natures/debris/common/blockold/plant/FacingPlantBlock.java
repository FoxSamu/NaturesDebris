/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FacingPlantBlock extends PlantBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected final VoxelShape[] shapes = new VoxelShape[6];
    protected final int thickness;
    protected final int marge;

    public FacingPlantBlock(Properties properties, int thickness, int marge) {
        super(properties);
        this.thickness = thickness;
        this.marge = marge;

        createShapes();

        setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));
    }

    public FacingPlantBlock(Properties properties, int thickness) {
        this(properties, thickness, 0);
    }

    protected void createShapes() {
        for (Direction dir : Direction.values()) {
            Direction.AxisDirection axDir = dir.getAxisDirection();
            Direction.Axis axis = dir.getAxis();

            int a = axDir == Direction.AxisDirection.NEGATIVE ? 16 : 0;
            int b = axDir == Direction.AxisDirection.NEGATIVE ? 16 - thickness : thickness;
            int min = Math.min(a, b);
            int max = Math.max(a, b);

            int xs = axis == Direction.Axis.X ? min : marge;
            int ys = axis == Direction.Axis.Y ? min : marge;
            int zs = axis == Direction.Axis.Z ? min : marge;

            int xe = axis == Direction.Axis.X ? max : 16 - marge;
            int ye = axis == Direction.Axis.Y ? max : 16 - marge;
            int ze = axis == Direction.Axis.Z ? max : 16 - marge;

            shapes[dir.ordinal()] = makeCuboidShape(xs, ys, zs, xe, ye, ze);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return shapes[state.get(FACING).ordinal()];
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state, Direction direction) {
        return isBlockSideSustainable(state, world, pos, direction);
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState) {
        Direction facing = state.get(FACING);
        if (dir == facing.getOpposite()) {
            return canBlockSustain(world, adj, adjState, dir.getOpposite());
        }
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        Direction face = ctx.getFace();

        BlockPos off = pos.offset(face, -1);
        if (canBlockSustain(world, off, world.getBlockState(off), face)) {
            return computeStateForPos(world, pos, getDefaultState().with(FACING, face));
        }

        Direction[] dirs = ctx.getNearestLookingDirections();
        for (Direction dir : dirs) {
            off = pos.offset(dir, -1);
            if (canBlockSustain(world, off, world.getBlockState(off), dir)) {
                return computeStateForPos(world, pos, getDefaultState().with(FACING, dir));
            }
        }
        return null;
    }
}
