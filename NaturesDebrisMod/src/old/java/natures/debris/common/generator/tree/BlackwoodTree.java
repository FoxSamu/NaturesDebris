/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.tree;

import natures.debris.common.blockold.MDBlockTags;
import natures.debris.common.blockold.MDNatureBlocks;
import natures.debris.common.blockold.MDPlantBlocks;
import natures.debris.common.blockold.MDTreeBlocks;
import natures.debris.common.blockold.base.AxisBlock;
import natures.debris.common.blockold.plant.FacingPlantBlock;
import natures.debris.common.blockold.prop.SignedIntegerProperty;
import natures.debris.common.blockold.tree.HangLeavesBlock;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;
import java.util.function.Consumer;

public class BlackwoodTree extends Tree {

    private static final BlockState LEAVES = MDTreeBlocks.BLACKWOOD_LEAVES.getDefaultState();
    private static final BlockState HANGING_LEAVES = MDTreeBlocks.BLACKWOOD_LEAVES.getDefaultState().with(HangLeavesBlock.DISTANCE, -1);
    private static final BlockState MURINA = MDPlantBlocks.MURINA.getDefaultState();
    private static final BlockState LOG_Y = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Y);
    private static final BlockState WOOD_X = MDTreeBlocks.BLACKWOOD.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.X);
    private static final BlockState WOOD_Z = MDTreeBlocks.BLACKWOOD.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Z);
    private static final BlockState DIRT = MDNatureBlocks.MURKY_DIRT.getDefaultState();

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
    public boolean isSustainable(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.DIRTLIKE);
    }

    @Override
    public boolean canGenerate(IWorldReader world, Random rand, BlockPos pos) {
        if (world.getBlockState(pos.up()).getMaterial().blocksMovement() || world.getBlockState(pos.up()).getMaterial().isLiquid()) {
            return false;
        }


        int height = rand.nextInt(7) + 5;

        MovingBlockPos rpos = new MovingBlockPos(pos);
        for (int y = 2; y < height; y++) {
            for (int x = -2; x <= 1; x++) {
                for (int z = -2; z <= 1; z++) {
                    rpos.setPos(pos).addPos(x, y, z);

                    if (!isAirOrLeaves(world, rpos))
                        return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void generateTree(Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos) {
        MovingBlockPos mpos = new MovingBlockPos(pos);


        int height = rand.nextInt(5) + 7;

        // Log
        for (int y = -2; y <= height; y++) {
            mpos.setPos(pos).moveUp(y);

            if (y == height) {
                setLogStateRandom(world, mpos, LOG_Y, rand, logs);
                setLogStateRandom(world, mpos.moveNorth(), LOG_Y, rand, logs);
                setLogStateRandom(world, mpos.moveWest(), LOG_Y, rand, logs);
                setLogStateRandom(world, mpos.moveSouth(), LOG_Y, rand, logs);
            } else if (y > -2) {
                setLogState(world, mpos, LOG_Y, logs);
                setLogState(world, mpos.moveNorth(), LOG_Y, logs);
                setLogState(world, mpos.moveWest(), LOG_Y, logs);
                setLogState(world, mpos.moveSouth(), LOG_Y, logs);
            } else {
                setBlockState(world, mpos, DIRT);
                setBlockState(world, mpos.moveNorth(), DIRT);
                setBlockState(world, mpos.moveWest(), DIRT);
                setBlockState(world, mpos.moveSouth(), DIRT);
            }
        }

        createRoots(world, pos, mpos, logs);

        boolean moss = rand.nextInt(6) == 0;
        int mossHeight = rand.nextInt(4) + 1;

        for (int x = -2; x <= 1; x++) {
            for (int z = -2; z <= 1; z++) {
                boolean xd = x == -2 || x == 1;
                boolean zd = z == -2 || z == 1;

                if (xd != zd) {
                    mpos.setPos(pos).addPos(x, -1, z);
                    setLogState(world, mpos, xd ? WOOD_X : WOOD_Z, logs);

                    if (moss) {
                        if (x == -2) {
                            createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction.WEST);
                        }
                        if (x == 1) {
                            createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction.EAST);
                        }
                        if (z == -2) {
                            createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction.NORTH);
                        }
                        if (z == 1) {
                            createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction.SOUTH);
                        }
                    }
                }

                if (xd || zd) {
                    boolean random = rand.nextBoolean();
                    if (xd && zd) random &= rand.nextBoolean();

                    if (random) {
                        createExtraLog(world, pos, mpos, logs, x, height - rand.nextInt(2), z, rand);
                    }
                }
            }
        }

        double radius = rand.nextInt(3) + 4.4;
        int rad = (int) (radius + 1);

        FractalPerlin3D noise = new FractalPerlin3D(rand.nextInt(), 2);
        for (int x = -rad; x <= rad; x++) {
            for (int z = -rad; z <= rad; z++) {
                for (int y = 0; y <= rad / 5D * 4; y++) {
                    double dx = x + 0.5;
                    double dz = z + 0.5;

                    double r = radius + noise.generate(x / 5.3, y / 5.3, z / 5.3);

                    double sqsum = dx * dx + 2 * y * y + dz * dz;
                    if (sqsum <= r * r) {
                        mpos.setPos(pos).moveUp(height).addPos(x, y, z);

                        if (isAir(world, mpos)) {
                            setBlockState(world, mpos, LEAVES);

                            if (y == 0 && rand.nextInt(3) == 0) {
                                boolean murina = rand.nextBoolean();
                                BlockState hanger = murina ? MURINA : HANGING_LEAVES;
                                int len = rand.nextInt(murina ? 8 : 4) + 1;

                                for (int i = 0; i < len; i++) {
                                    mpos.moveDown();
                                    if (isAir(world, mpos)) {
                                        setBlockState(world, mpos, hanger);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createMoss(IWorld world, BlockPos pos, MovingBlockPos mpos, int x, int z, int height, Random rand, Direction dir) {
        int h = height + rand.nextInt(3) - 1;
        for (int y = 0; y < h; y++) {
            mpos.setPos(pos).addPos(x, y, z);
            if (isAir(world, mpos)) {
                setBlockState(world, mpos, MDPlantBlocks.MOSS.getDefaultState().with(FacingPlantBlock.FACING, dir));
            }
        }
    }

    private void createRoots(IWorld world, BlockPos pos, MovingBlockPos mpos, Consumer<BlockPos> logs) {
        for (int x = -2; x <= 1; x++) {
            for (int z = -2; z <= 1; z++) {
                boolean xd = x == -2 || x == 1;
                boolean zd = z == -2 || z == 1;
                if (xd != zd) {
                    mpos.setPos(pos).addPos(x, -1, z);

                    setLogState(world, mpos, xd ? WOOD_X : WOOD_Z, logs);
                }
            }
        }
    }

    private void createExtraLog(IWorld world, BlockPos pos, MovingBlockPos mpos, Consumer<BlockPos> logs, int x, int y, int z, Random rand) {
        int len = rand.nextInt(4) + 1;
        for (int i = 0; i < len; i++) {
            mpos.setPos(pos).addPos(x, y, z).moveDown(i);
            setLogState(world, mpos, LOG_Y, logs);
        }
    }

    private void setLogStateRandom(IWorld world, BlockPos pos, BlockState state, Random rand, Consumer<BlockPos> logs) {
        if (rand.nextBoolean()) setLogState(world, pos, state, logs);
    }

    @Override
    protected boolean isDecayableLeaf(BlockState state) {
        return state.has(HangLeavesBlock.DISTANCE) && state.get(HangLeavesBlock.DISTANCE) > 0;
    }

    @Override
    protected SignedIntegerProperty getLeafDistanceProperty() {
        return HangLeavesBlock.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }
}
