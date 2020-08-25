/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.blocks;

import modernity.common.blockold.plant.DoubleDirectionalPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

import static modernity.common.blockold.plant.DoubleDirectionalPlantBlock.*;

public class DoublePlantBlockGenerator implements IBlockGenerator {
    private final DoubleDirectionalPlantBlock block;

    public DoublePlantBlockGenerator(DoubleDirectionalPlantBlock block) {
        this.block = block;
    }

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        if(block.canGenerateAt(world, pos, world.getBlockState(pos))) {
            BlockState lower = block.computeStateForPos(world, pos, block.getDefaultState().with(TYPE, ROOT));
            BlockState upper = block.computeStateForPos(world, pos, block.getDefaultState().with(TYPE, END));
            world.setBlockState(pos, lower, 2);
            world.setBlockState(pos.offset(block.getGrowDirection()), upper, 2);
            return true;
        }
        return false;
    }
}
