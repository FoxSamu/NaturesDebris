/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.blocks;

import natures.debris.common.blockold.MDNatureBlocks;
import natures.debris.common.blockold.misc.PuddleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class PuddleBlockGenerator implements IBlockGenerator {
    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        if (world.isAirBlock(pos) && MDNatureBlocks.PUDDLE.canRemain(world, pos)) {
            world.setBlockState(pos, MDNatureBlocks.PUDDLE.getDefaultState().with(PuddleBlock.DISTANCE, -1), 2 | 16);
        }
        return false;
    }
}
