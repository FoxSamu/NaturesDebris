/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.tree;

import modernity.api.util.exc.UnexpectedCaseException;
import natures.debris.common.blockold.MDBlockTags;
import natures.debris.common.blockold.tree.DecayLeavesBlock;
import natures.debris.generic.util.BlockUpdates;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.Random;
import java.util.function.Consumer;

public class SphericalTree extends Tree {

    private final BlockState leaves;
    private final BlockState logX;
    private final BlockState logY;
    private final BlockState logZ;
    private final int minheight;
    private final int maxheight;

    public SphericalTree(BlockState leaves, BlockState logX, BlockState logY, BlockState logZ) {
        this(leaves, logX, logY, logZ, 5, 12);
    }

    public SphericalTree(BlockState leaves, BlockState logX, BlockState logY, BlockState logZ, int minheight, int maxheight) {
        this.leaves = leaves;
        this.logX = logX;
        this.logY = logY;
        this.logZ = logZ;
        this.minheight = minheight;
        this.maxheight = maxheight;
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

        int height = rand.nextInt(maxheight - minheight + 1) + minheight;

        // Check for space
        MovingBlockPos tpos = new MovingBlockPos();
        {
            for (int i = 0; i < height + 4; i++) {
                if (i > 4) {
                    for (int x = -3; x <= 3; x++) {
                        for (int z = -3; z <= 3; z++) {
                            tpos.setPos(rpos);
                            tpos.addPos(x, 0, z);
                            if (world.getBlockState(tpos).getMaterial().blocksMovement())
                                return false;
                        }
                    }
                } else {
                    if (world.getBlockState(rpos).getMaterial().blocksMovement())
                        return false;
                }
                rpos.moveUp();
            }
        }

        return true;
    }

    @Override
    protected void generateTree(Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos) {
        MovingBlockPos rpos = new MovingBlockPos(pos);

        int height = rand.nextInt(maxheight - minheight + 1) + minheight;

        // Log
        rpos.setPos(pos);
        rpos.moveDown();
        for (int i = -1; i <= height; i++) {
            world.setBlockState(rpos, logY, 2 | 16);
            logs.accept(rpos.toImmutable());
            rpos.moveUp();
        }

        // Roots
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            rpos.setPos(pos).move(facing);
            Direction.Axis axis = facing.getAxis();


            // Root
            rpos.setPos(pos).moveDown();
            int len = rand.nextInt(4) == 0 ? 2 : 1;

            for (int i = 0; i < len; i++) {
                rpos.move(facing);
                world.setBlockState(rpos, log(axis), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER);
                logs.accept(rpos.toImmutable());
            }
        }

        rpos.setPos(pos).moveUp(height);
        addCanopy(world, rand, rpos, rand.nextDouble() * 1.4 + 3);

        for (int i = 0; i < 8; i++) {
            if (rand.nextInt(10) != 0) {
                rpos.setPos(pos).moveUp(height - 3 + rand.nextInt(3));
                addBranch(logs, world, rand, rpos, i);
            }
        }
    }

    private void addBranch(Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos, int dir) {
        int length = rand.nextInt(2) + 1;
        int xdir = 0;
        switch (dir) {
            case 7:
            case 0:
            case 1:
                xdir = 1;
                break;
            case 3:
            case 4:
            case 5:
                xdir = -1;
                break;
        }
        int zdir = 0;
        switch (dir) {
            case 1:
            case 2:
            case 3:
                zdir = 1;
                break;
            case 5:
            case 6:
            case 7:
                zdir = -1;
                break;
        }
        int ydir = 1;

        BlockState log;
        if (zdir == 0) {
            log = this.logX;
        } else if (xdir == 0) {
            log = this.logZ;
        } else {
            log = rand.nextBoolean() ? logX : logZ;
        }

        MovingBlockPos rpos = new MovingBlockPos();

        rpos.setPos(pos);
        for (int i = 0; i < length; i++) {
            rpos.addPos(xdir, ydir, zdir);
            world.setBlockState(rpos, log, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER);
            logs.accept(rpos.toImmutable());
        }
        addCanopy(world, rand, rpos, rand.nextDouble() * 1.2 + 2.2);
    }

    private void addCanopy(IWorld world, Random rand, BlockPos pos, double radius) {
        MovingBlockPos rpos = new MovingBlockPos();
        {
            int ri = (int) (radius + 2);
            double spherex = pos.getX() + rand.nextDouble();
            double spherey = pos.getY() + rand.nextDouble();
            double spherez = pos.getZ() + rand.nextDouble();
            for (int x = -ri; x <= ri; x++) {
                for (int z = -ri; z <= ri; z++) {
                    for (int y = -ri; y <= ri; y++) {
                        rpos.setPos(pos).addPos(x, y, z);
                        double cx = rpos.getX() + 0.5 - spherex;
                        double cy = rpos.getY() + 0.5 - spherey;
                        double cz = rpos.getZ() + 0.5 - spherez;

                        if (cx * cx + cy * cy + cz * cz < radius * radius) {
                            if (!world.getBlockState(rpos).getMaterial().blocksMovement()) {
                                world.setBlockState(rpos, leaves, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER);
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockState log(Direction.Axis axis) {
        switch (axis) {
            case X: return logX;
            case Y: return logY;
            case Z: return logZ;
            default: throw new UnexpectedCaseException("4th axis...what?");
        }
    }

    @Override
    protected boolean isDecayableLeaf(BlockState state) {
        return state.has(DecayLeavesBlock.DISTANCE) && state.get(DecayLeavesBlock.DISTANCE) > 0;
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
