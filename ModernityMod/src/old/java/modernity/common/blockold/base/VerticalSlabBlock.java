/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.base;

import modernity.common.blockold.MDBlockStateProperties;
import modernity.common.blockold.fluid.WaterlogType;
import modernity.common.blockold.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * A slab block that allows vertical placing (using shift).
 */
@SuppressWarnings("deprecation")
public class VerticalSlabBlock extends WaterloggedBlock {
    public static final EnumProperty<SlabType> TYPE = MDBlockStateProperties.SLAB_TYPE;

    public VerticalSlabBlock(Properties properties) {
        super(properties);

        setDefaultState(stateContainer.getBaseState().with(TYPE, SlabType.DOWN));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TYPE);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader reader, BlockPos pos) {
        return reader.getMaxLightLevel();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        IFluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());
        BlockState state = ctx.getWorld().getBlockState(ctx.getPos());
        if(state.getBlock() == this) {
            if(state.get(TYPE) == SlabType.DOUBLE) return null;
            else return state.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, WaterlogType.NONE);
        }
        boolean sidedPlacing = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();
        if(sidedPlacing) {
            Direction facing = ctx.getFace().getOpposite();
            switch(facing) {
                default:
                case UP:
                    return getDefaultState().with(TYPE, SlabType.UP).with(WATERLOGGED, WaterlogType.getType(fluid));
                case DOWN:
                    return getDefaultState().with(TYPE, SlabType.DOWN).with(WATERLOGGED, WaterlogType.getType(fluid));
                case NORTH:
                    return getDefaultState().with(TYPE, SlabType.NORTH).with(WATERLOGGED, WaterlogType.getType(fluid));
                case EAST:
                    return getDefaultState().with(TYPE, SlabType.EAST).with(WATERLOGGED, WaterlogType.getType(fluid));
                case SOUTH:
                    return getDefaultState().with(TYPE, SlabType.SOUTH).with(WATERLOGGED, WaterlogType.getType(fluid));
                case WEST:
                    return getDefaultState().with(TYPE, SlabType.WEST).with(WATERLOGGED, WaterlogType.getType(fluid));
            }
        } else {
            Direction facing = ctx.getFace();
            if(facing == Direction.UP)
                return getDefaultState().with(TYPE, SlabType.DOWN).with(WATERLOGGED, WaterlogType.getType(fluid));
            else if(facing == Direction.DOWN)
                return getDefaultState().with(TYPE, SlabType.UP).with(WATERLOGGED, WaterlogType.getType(fluid));
            else {
                double hitY = ctx.getHitVec().y % 1;
                if(hitY < 0.5)
                    return getDefaultState().with(TYPE, SlabType.DOWN).with(WATERLOGGED, WaterlogType.getType(fluid));
                else
                    return getDefaultState().with(TYPE, SlabType.UP).with(WATERLOGGED, WaterlogType.getType(fluid));
            }
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return state.get(TYPE).getShape();
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext ctx) {
        ItemStack stack = ctx.getItem();
        SlabType slabType = state.get(TYPE);

        if(slabType != SlabType.DOUBLE && stack.getItem() == asItem()) {
            if(ctx.replacingClickedOnBlock()) {
                boolean up = ctx.getHitVec().y % 1 > 0.5;
                boolean south = ctx.getHitVec().z % 1 > 0.5;
                boolean east = ctx.getHitVec().x % 1 > 0.5;
                Direction face = ctx.getFace();
                if(slabType == SlabType.DOWN) {
                    return face == Direction.UP || up && face.getAxis() != Direction.Axis.Y;
                } else if(slabType == SlabType.UP) {
                    return face == Direction.DOWN || !up && face.getAxis() != Direction.Axis.Y;
                } else if(slabType == SlabType.NORTH) {
                    return face == Direction.SOUTH || south && face.getAxis() != Direction.Axis.Z;
                } else if(slabType == SlabType.SOUTH) {
                    return face == Direction.NORTH || !south && face.getAxis() != Direction.Axis.Z;
                } else if(slabType == SlabType.EAST) {
                    return face == Direction.WEST || !east && face.getAxis() != Direction.Axis.X;
                } else {
                    return face == Direction.EAST || east && face.getAxis() != Direction.Axis.X;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    @Override
    public Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
        if(state.get(TYPE) == SlabType.DOUBLE) return Fluids.EMPTY;
        return super.pickupFluid(world, pos, state);
    }

    @Override
    public Fluid pickupFluidModernity(IWorld world, BlockPos pos, BlockState state) {
        if(state.get(TYPE) == SlabType.DOUBLE) return Fluids.EMPTY;
        return super.pickupFluidModernity(world, pos, state);
    }

    @Override
    public boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
        if(state.get(TYPE) == SlabType.DOUBLE) return false;
        return super.canContainFluid(world, pos, state, fluid);
    }

    @Override
    public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, IFluidState fluid) {
        if(state.get(TYPE) == SlabType.DOUBLE) return false;
        return super.receiveFluid(world, pos, state, fluid);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if(state.get(TYPE) == SlabType.DOUBLE) return state;
        return state.with(TYPE, SlabType.forFacing(mirror.mirror(state.get(TYPE).getFacing())));
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        if(state.get(TYPE) == SlabType.DOUBLE) return state;
        return state.with(TYPE, SlabType.forFacing(rotation.rotate(state.get(TYPE).getFacing())));
    }

}
