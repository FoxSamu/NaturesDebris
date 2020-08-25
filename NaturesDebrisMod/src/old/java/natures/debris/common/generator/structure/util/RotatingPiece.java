/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.structure.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.IFluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class RotatingPiece extends StructurePiece {
    private Rotation rotation;
    private Direction coordBaseMode;

    private static final Set<Block> BLOCKS_NEEDING_POSTPROCESSING =
        ImmutableSet.<Block>builder()
            .add(Blocks.NETHER_BRICK_FENCE)
            .add(Blocks.TORCH)
            .add(Blocks.WALL_TORCH)
            .add(Blocks.OAK_FENCE)
            .add(Blocks.SPRUCE_FENCE)
            .add(Blocks.DARK_OAK_FENCE)
            .add(Blocks.ACACIA_FENCE)
            .add(Blocks.BIRCH_FENCE)
            .add(Blocks.JUNGLE_FENCE)
            .add(Blocks.LADDER)
            .add(Blocks.IRON_BARS)
            .add(Blocks.DARK_OAK_STAIRS)
            .add(Blocks.OAK_STAIRS)
            .add(Blocks.JUNGLE_STAIRS)
            .add(Blocks.BIRCH_STAIRS)
            .add(Blocks.ACACIA_STAIRS)
            .add(Blocks.SPRUCE_STAIRS)
            .build();

    protected RotatingPiece(IStructurePieceType type, int depth) {
        super(type, depth);
    }

    public RotatingPiece(IStructurePieceType type, CompoundNBT nbt) {
        super(type, nbt);
    }

    @Override
    protected int getXWithOffset(int x, int z) {
        return StructureUtil.getX(x, z, getCoordBaseMode(), boundingBox);
    }

    @Override
    protected int getZWithOffset(int x, int z) {
        return StructureUtil.getZ(x, z, getCoordBaseMode(), boundingBox);
    }

    @Override
    protected void setBlockState(IWorld world, BlockState state, int x, int y, int z, MutableBoundingBox box) {
        BlockPos pos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
        if (box.isVecInside(pos)) {
            if (rotation != Rotation.NONE) {
                state = state.rotate(rotation);
            }

            world.setBlockState(pos, state, 2);
            IFluidState fluid = world.getFluidState(pos);
            if (!fluid.isEmpty()) {
                world.getPendingFluidTicks().scheduleTick(pos, fluid.getFluid(), 0);
            }

            if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                world.getChunk(pos).markBlockForPostprocessing(pos);
            }
        }
    }

    @Override
    public void setCoordBaseMode(Direction coordBaseMode) {
        this.coordBaseMode = coordBaseMode;
        this.rotation = StructureUtil.getRotation(coordBaseMode);
    }

    @Nullable
    @Override
    public Direction getCoordBaseMode() {
        return coordBaseMode;
    }
}
