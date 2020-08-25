/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.blocks;

import modernity.common.blockold.plant.TallDirectionalPlantBlock;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;
import java.util.function.Function;

import static modernity.common.blockold.plant.TallDirectionalPlantBlock.*;

public class TallPlantBlockGenerator implements IBlockGenerator {
    private final TallDirectionalPlantBlock block;
    private final Function<Random, Integer> heightFunction;

    public TallPlantBlockGenerator(TallDirectionalPlantBlock block, Function<Random, Integer> heightFunction) {
        this.block = block;
        this.heightFunction = heightFunction;
    }

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        Direction growDir = block.getGrowDirection();
        BlockPos support = pos.offset(growDir, -1);
        if(block.canGenerateAt(world, pos, world.getBlockState(pos)) && block.canBlockSustain(world, support, world.getBlockState(support))) {
            int h = block.deformGenerationHeight(heightFunction.apply(rand), rand);
            MovingBlockPos mpos = new MovingBlockPos(pos);

            BlockState lastPlaced = null;
            for(int i = 0; i < h; i++) {
                if(block.canGenerateAt(world, mpos, world.getBlockState(mpos))) {
                    BlockState state = block.computeStateForPos(world, mpos, block.getDefaultState());
                    if(state == null) break;

                    state = state.with(ROOT, i == 0);
                    state = state.with(END, false);

                    lastPlaced = state;

                    world.setBlockState(mpos, state, 2);

                    mpos.move(growDir);
                } else {
                    break;
                }
            }

            mpos.move(growDir, -1);
            if(lastPlaced != null) {
                world.setBlockState(mpos, lastPlaced.with(END, true), 2);
            }

            return true;
        }
        return false;
    }
}
