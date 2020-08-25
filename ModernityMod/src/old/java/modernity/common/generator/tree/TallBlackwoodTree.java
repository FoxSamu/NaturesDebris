/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.tree;

import modernity.common.blockold.MDBlockTags;
import modernity.common.blockold.MDNatureBlocks;
import modernity.common.blockold.MDPlantBlocks;
import modernity.common.blockold.MDTreeBlocks;
import modernity.common.blockold.base.AxisBlock;
import modernity.common.blockold.plant.FacingPlantBlock;
import modernity.common.blockold.prop.SignedIntegerProperty;
import modernity.common.blockold.tree.HangLeavesBlock;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction8;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import modernity.api.util.exc.UnexpectedCaseException;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;
import java.util.function.Consumer;

public class TallBlackwoodTree extends Tree {

    private static final BlockState LEAVES = MDTreeBlocks.BLACKWOOD_LEAVES.getDefaultState().with(HangLeavesBlock.DISTANCE, 0);
    private static final BlockState HANGING_LEAVES = LEAVES;
    private static final BlockState MURINA = MDPlantBlocks.MURINA.getDefaultState();
    private static final BlockState LOG_Y = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Y);
    private static final BlockState LOG_X = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.X);
    private static final BlockState LOG_Z = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Z);
    private static final BlockState WOOD_X = MDTreeBlocks.BLACKWOOD.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.X);
    private static final BlockState WOOD_Z = MDTreeBlocks.BLACKWOOD.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Z);
    private static final BlockState DIRT = MDNatureBlocks.MURKY_DIRT.getDefaultState();

    @Override
    public boolean isSustainable(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.SOIL);
    }

    private int generateHeight(Random rand) {
        return rand.nextInt(10) + 27;
    }

    @Override
    public boolean canGenerate(IWorldReader world, Random rand, BlockPos pos) {
        if(world.getBlockState(pos.up()).getMaterial().blocksMovement() || world.getBlockState(pos.up()).getMaterial().isLiquid()) {
            return false;
        }


        int height = generateHeight(rand);

        MovingBlockPos rpos = new MovingBlockPos(pos);
        for(int y = 5; y <= height; y++) {
            for(int x = -3; x <= 2; x++) {
                for(int z = -3; z <= 2; z++) {
                    rpos.setPos(pos).addPos(x, y, z);

                    if(!isAirOrLeaves(world, rpos))
                        return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void generateTree(Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos) {
        MovingBlockPos mpos = new MovingBlockPos(pos);


        int height = generateHeight(rand);
        int rad = rand.nextInt(5) + 9;

        createLog(world, pos, mpos, rand, height + 2, logs);
        createRoots(world, pos, mpos, rand, logs);
        createAuxiliaries(world, pos, mpos, rand, height + 2, logs);
        createRandomBranches(world, pos, mpos, rand, logs, height, rad - 2);
        createLeaves(world, pos, mpos, rand, height, rad);
    }

    private void createLog(IWorld world, BlockPos pos, MovingBlockPos mpos, Random rand, int height, Consumer<BlockPos> logs) {
        for(int x = -2; x <= 1; x++) {
            for(int z = -2; z <= 1; z++) {
                boolean xd = x == -2 || x == 1;
                boolean zd = z == -2 || z == 1;

                if(xd != zd) {
                    createLogColumn(world, pos, mpos, x, z, height + rand.nextInt(2), logs);
                } else if(!xd) {
                    createLogColumn(world, pos, mpos, x, z, height + rand.nextInt(2), logs);
                }
            }
        }
    }

    private void createLogColumn(IWorld world, BlockPos pos, MovingBlockPos mpos, int x, int z, int height, Consumer<BlockPos> logs) {
        for(int y = -1; y < height; y++) {
            mpos.setPos(pos).move(x, y, z);
            setLogState(world, mpos, LOG_Y, logs);
        }

        mpos.setPos(pos).move(x, -2, z);
        setBlockState(world, mpos, DIRT);
    }

    private void createRoots(IWorld world, BlockPos pos, MovingBlockPos mpos, Random rand, Consumer<BlockPos> logs) {
        for(int x = -3; x <= 2; x++) {
            for(int z = -3; z <= 2; z++) {
                boolean xd = x == -2 || x == 1;
                boolean zd = z == -2 || z == 1;

                if(xd && zd) {
                    mpos.setPos(pos).addPos(x, -1, z);
                    setLogState(world, mpos, rand.nextBoolean() ? WOOD_X : WOOD_Z, logs);
                }

                boolean xe = x == -3 || x == 2;
                boolean ze = z == -3 || z == 2;
                boolean xs = x == -1 || x == 0;
                boolean zs = z == -1 || z == 0;

                if(xe || ze) {
                    mpos.setPos(pos).addPos(x, -1, z);
                    if(zs) setLogState(world, mpos, WOOD_X, logs);
                    if(xs) setLogState(world, mpos, WOOD_Z, logs);
                }
            }
        }
    }

    private void createAuxiliaries(IWorld world, BlockPos pos, MovingBlockPos mpos, Random rand, int height, Consumer<BlockPos> logs) {
        boolean moss = rand.nextInt(6) == 0;
        int mossHeight = rand.nextInt(6) + 2;

        for(int x = -3; x <= 2; x++) {
            for(int z = -3; z <= 2; z++) {
                boolean xd = x == -2 || x == 1;
                boolean zd = z == -2 || z == 1;
                boolean xs = x == -1 || x == 0;
                boolean zs = z == -1 || z == 0;
                boolean xe = x == -3 || x == 2;
                boolean ze = z == -3 || z == 2;

                if(xd && zd || xe && zs || xs && ze) {
                    if(rand.nextBoolean()) {
                        createExtraLog(world, pos, mpos, logs, x, height - rand.nextInt(2), z, rand);
                    }
                }

                if(moss) {
                    if(x == -2 && z == -2) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.NORTH_WEST);
                    }
                    if(x == -2 && z == 1) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.SOUTH_WEST);
                    }
                    if(x == 1 && z == 1) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.SOUTH_EAST);
                    }
                    if(x == 1 && z == -2) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.NORTH_EAST);
                    }

                    if(xs && z == -3) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.NORTH);
                    }
                    if(xs && z == 2) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.SOUTH);
                    }
                    if(zs && x == -3) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.WEST);
                    }
                    if(zs && x == 2) {
                        createMoss(world, pos, mpos, x, z, mossHeight, rand, Direction8.EAST);
                    }
                }
            }
        }
    }

    private void createExtraLog(IWorld world, BlockPos pos, MovingBlockPos mpos, Consumer<BlockPos> logs, int x, int y, int z, Random rand) {
        int len = rand.nextInt(4) + 1;
        for(int i = 0; i < len; i++) {
            mpos.setPos(pos).addPos(x, y, z).moveDown(i);
            setLogState(world, mpos, LOG_Y, logs);
        }
    }

    private void createMoss(IWorld world, BlockPos pos, MovingBlockPos mpos, int x, int z, int height, Random rand, Direction8 dir) {
        int h = height + rand.nextInt(5) - 2;
        if(h > 0) {
            for(int y = 0; y < h; y++) {
                mpos.setPos(pos).addPos(x, y, z);
                if(isAir(world, mpos)) {
                    setBlockState(world, mpos, MDPlantBlocks.MOSS.getDefaultState().with(FacingPlantBlock.FACING, dirFrom8(dir, rand)));
                }
            }
        }
    }

    private Direction dirFrom8(Direction8 dir8, Random rand) {
        switch(dir8) {
            case NORTH: return Direction.NORTH;
            case EAST: return Direction.EAST;
            case SOUTH: return Direction.SOUTH;
            case WEST: return Direction.WEST;
            case NORTH_EAST: return rand.nextBoolean() ? Direction.NORTH : Direction.EAST;
            case NORTH_WEST: return rand.nextBoolean() ? Direction.NORTH : Direction.WEST;
            case SOUTH_EAST: return rand.nextBoolean() ? Direction.SOUTH : Direction.EAST;
            case SOUTH_WEST: return rand.nextBoolean() ? Direction.SOUTH : Direction.WEST;
            default:
                throw new UnexpectedCaseException("What happened to our universe?");
        }
    }

    private void createRandomBranches(IWorld world, BlockPos pos, MovingBlockPos mpos, Random rand, Consumer<BlockPos> logs, int height, int branchRad) {
        double branchR = branchRad - 0.6;
        for(int x = -branchRad; x <= branchRad; x++) {
            for(int z = -branchRad; z <= branchRad; z++) {
                double dx = x + 0.5;
                double dz = z + 0.5;

                if(dx * dx + dz * dz < branchR * branchR) {
                    if(rand.nextInt(12) == 0) {
                        mpos.setPos(pos).addPos(x, height + rand.nextInt(2), z);
                        if(isAirOrLeaves(world, mpos)) {
                            setLogState(world, mpos, rand.nextBoolean() ? LOG_X : LOG_Z, logs);
                        }
                    }
                }
            }
        }
    }

    private void createLeaves(IWorld world, BlockPos pos, MovingBlockPos mpos, Random rand, int height, int rad) {
        double radius = rad - 0.6;

        FractalPerlin3D noise = new FractalPerlin3D(rand.nextInt(), 2);
        for(int x = -rad; x <= rad; x++) {
            for(int z = -rad; z <= rad; z++) {
                for(int y = 0; y <= rad / 5D * 4; y++) {
                    double dx = x + 0.5;
                    double dz = z + 0.5;

                    double r = radius + noise.generate(x / 5.3, y / 5.3, z / 5.3);

                    double sqsum = dx * dx + 2 * y * y + dz * dz;
                    if(sqsum <= r * r) {
                        mpos.setPos(pos).moveUp(height).addPos(x, y, z);

                        if(isAir(world, mpos)) {
                            setBlockState(world, mpos, LEAVES);

                            if(y == 0 && rand.nextInt(3) == 0) {
                                boolean murina = rand.nextBoolean();
                                BlockState hanger = murina ? MURINA : HANGING_LEAVES;
                                int len = rand.nextInt(murina ? 12 : 4) + 1;

                                for(int i = 0; i < len; i++) {
                                    mpos.moveDown();
                                    if(isAir(world, mpos)) {
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

    private static boolean isAirOrLeaves(IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        IFluidState fluid = state.getFluidState();
        if(!mat.blocksMovement() && fluid.isEmpty()) return true;
        if(mat == Material.LEAVES) return true;
        if(state.isIn(BlockTags.LEAVES)) return true;
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
        return false;
    }

    @Override
    protected SignedIntegerProperty getLeafDistanceProperty() {
        return HangLeavesBlock.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 2;
    }
}
