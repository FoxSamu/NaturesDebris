/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.tree;

import modernity.api.util.exc.UnexpectedCaseException;
import modernity.api.util.math.MathUtil;
import natures.debris.common.blockold.MDBlockTags;
import natures.debris.common.blockold.tree.DecayLeavesBlock;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.Random;
import java.util.function.Consumer;

import static net.minecraft.util.Direction.*;

public class SphereTree extends Tree {

    private final BlockState logX;
    private final BlockState logY;
    private final BlockState logZ;
    private final BlockState leaves;
    private final int minHeight;
    private final int maxHeight;
    private final double minCanopySize;
    private final double maxCanopySize;

    public SphereTree(BlockState logX, BlockState logY, BlockState logZ, BlockState leaves) {
        this(logX, logY, logZ, leaves, 6, 9, 3, 4);
    }

    public SphereTree(BlockState logX, BlockState logY, BlockState logZ, BlockState leaves, int minHeight, int maxHeight, double minCanopySize, double maxCanopySize) {
        this.logX = logX;
        this.logY = logY;
        this.logZ = logZ;
        this.leaves = leaves;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.minCanopySize = minCanopySize;
        this.maxCanopySize = maxCanopySize;
    }

    public boolean isSustainable(IWorld world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.DIRTLIKE);
    }

    @Override
    public boolean canGenerate(IWorldReader world, Random rand, BlockPos pos) {
        if (world.getBlockState(pos.up()).getMaterial().blocksMovement() || world.getBlockState(pos.up()).getMaterial().isLiquid()) {
            return false;
        }

        MovingBlockPos rpos = new MovingBlockPos(pos);

        int height = rand.nextInt(maxHeight - minHeight + 1) + minHeight;

        // Check for space
        MovingBlockPos tpos = new MovingBlockPos();

        for (int i = 0; i < height; i++) {
            if (i > 4) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        tpos.setPos(rpos);
                        tpos.addPos(x, 0, z);
                        if (!isAirOrLeaves(world, tpos))
                            return false;
                    }
                }
            } else {
                if (!isAirOrLeaves(world, rpos))
                    return false;
            }
            rpos.moveUp();
        }


        return true;
    }

    @Override
    protected void generateTree(Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos) {
        int height = rand.nextInt(maxHeight - minHeight + 1) + minHeight;

        MovingBlockPos mpos = new MovingBlockPos(pos);
        for (int i = 0; i < height; i++) {
            world.setBlockState(mpos, logY, 2);
            logs.accept(mpos.toImmutable());
            mpos.moveUp();
        }

        for (Direction dir : Plane.HORIZONTAL) {
            createBranch(logs, world, pos, height - 3, dir.getXOffset(), dir.getZOffset(), 1, log(dir.getAxis()), mpos);
        }

        mpos.setPos(pos);
        mpos.moveUp(height - 2);

        createCanopy(world, mpos, rand, MathUtil.lerp(minCanopySize, maxCanopySize, rand.nextDouble()));
    }

    private void createBranch(Consumer<BlockPos> logs, IWorld world, BlockPos pos, int height, int xOff, int zOff, int length, BlockState log, MovingBlockPos mpos) {
        mpos.setPos(pos).moveUp(height);
        for (int i = 0; i < length; i++) {
            mpos.move(xOff, 1, zOff);

            if (!isAirOrLeaves(world, mpos)) {
                break;
            }

            world.setBlockState(mpos, log, 2);
            logs.accept(mpos.toImmutable());
        }
    }

    private void createCanopy(IWorld world, BlockPos pos, Random rand, double radius) {
        MovingBlockPos mpos = new MovingBlockPos();

        int rad = (int) (radius + 1);

        double logRad = radius - 2.5;

        for (int y = -2; y < rad; y++) {
            double h = Math.abs(y < 0 ? y * 2 : y * 0.9);

            for (int x = -rad; x <= rad; x++) {
                for (int z = -rad; z <= rad; z++) {
                    double p = x * x + h * h * h + z * z;
                    if (p < radius * radius) {
                        mpos.setPos(pos).move(x, y, z);

                        if (isAir(world, mpos)) {
                            BlockState place = leaves;
                            if (y >= 0 && p < logRad * logRad) {
                                if (rand.nextInt(16) == 0) {
                                    int d = rand.nextInt(3);
                                    if (d == 0) place = logX;
                                    if (d == 1) place = logY;
                                    if (d == 2) place = logZ;
                                }
                            }
                            world.setBlockState(mpos, place, 2);
                        }
                    }
                }
            }
        }
    }

    private BlockState log(Axis axis) {
        switch (axis) {
            case X: return logX;
            case Y: return logY;
            case Z: return logZ;
            default: throw new UnexpectedCaseException("4D Minecraft! Wait...what?!");
        }
    }


    private static boolean isAirOrLeaves(IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        IFluidState fluid = state.getFluidState();
        if (!mat.blocksMovement() && fluid.isEmpty()) return true;
        if (mat == Material.LEAVES) return true;
        if (state.isIn(BlockTags.LEAVES)) return true;
        return state.isAir(world, pos);
    }

    private static boolean isAir(IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        IFluidState fluid = state.getFluidState();
        return !mat.blocksMovement() && fluid.isEmpty();
    }


    @Override
    protected boolean isDecayableLeaf(BlockState state) {
        return state.getBlock() == leaves.getBlock();
    }

    @Override
    protected IntegerProperty getLeafDistanceProperty() {
        return DecayLeavesBlock.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }
}
