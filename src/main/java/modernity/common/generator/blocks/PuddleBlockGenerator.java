/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import modernity.common.block.MDBlocks;
import modernity.common.block.misc.PuddleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class PuddleBlockGenerator implements IBlockGenerator {
    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        if( world.isAirBlock( pos ) && MDBlocks.PUDDLE.canRemain( world, pos ) ) {
            world.setBlockState( pos, MDBlocks.PUDDLE.getDefaultState().with( PuddleBlock.DISTANCE, - 1 ), 2 | 16 );
        }
        return false;
    }
}
