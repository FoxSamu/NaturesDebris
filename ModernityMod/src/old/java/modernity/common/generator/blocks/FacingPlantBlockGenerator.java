/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.blocks;

import modernity.common.blockold.plant.FacingPlantBlock;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class FacingPlantBlockGenerator implements IBlockGenerator {
    private final FacingPlantBlock block;

    public FacingPlantBlockGenerator(FacingPlantBlock block) {
        this.block = block;
    }

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        if(world.getBlockState(pos).isAir(world, pos)) {
            MovingBlockPos mpos = new MovingBlockPos();

            Direction[] dirs = new Direction[6];
            int count = 0;
            for(Direction dir : Direction.values()) {
                mpos.setPos(pos).move(dir, -1);
                BlockState offState = world.getBlockState(mpos);

                if(block.canBlockSustain(world, mpos, offState, dir)) {
                    dirs[count] = dir;
                    count++;
                }
            }

            if(count > 0) {
                int index = rand.nextInt(count);

                Direction dir = dirs[index];
                BlockState state = block.computeStateForPos(world, pos, block.getDefaultState().with(FacingPlantBlock.FACING, dir));
                world.setBlockState(pos, state, 2);
            }
        }
        return false;
    }
}
