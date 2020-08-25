/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.tree;

import natures.debris.common.generator.tree.MDTrees;
import natures.debris.common.generator.tree.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlackwoodSaplingBlock extends AbstractSaplingBlock {
    public BlackwoodSaplingBlock(Properties properties) {
        super(properties);
    }

    private boolean findLarge(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this
                   && world.getBlockState(pos.east()).getBlock() == this
                   && world.getBlockState(pos.south()).getBlock() == this
                   && world.getBlockState(pos.south().east()).getBlock() == this;
    }

    @Override
    protected int findGrowState(IWorld world, BlockPos pos) {
        if (findLarge(world, pos)) return 1;
        if (findLarge(world, pos.north())) return 2;
        if (findLarge(world, pos.west())) return 3;
        if (findLarge(world, pos.west().north())) return 4;
        return 0;
    }

    @Override
    protected BlockPos getGrowPos(IWorld world, BlockPos pos, int growState) {
        switch (growState) {
            default:
                return pos;
            case 1:
                return pos.east().south();
            case 2:
                return pos.east();
            case 3:
                return pos.south();
        }
    }

    @Override
    protected Tree getTree(IWorld world, BlockPos pos, int growState) {
        return growState == 0 ? MDTrees.BLACKWOOD_TINY : MDTrees.BLACKWOOD;
    }

    @Override
    protected void removeSaplings(IWorld world, BlockPos pos, int growState) {
        if (growState == 0) {
            world.removeBlock(pos, false);
        } else {
            BlockPos growPos = getGrowPos(world, pos, growState);
            world.removeBlock(growPos, false);
            world.removeBlock(growPos.north(), false);
            world.removeBlock(growPos.west(), false);
            world.removeBlock(growPos.north().west(), false);
        }
    }
}
