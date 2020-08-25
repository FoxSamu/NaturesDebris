/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.blocks;

import natures.debris.common.blockold.MDPlantBlocks;
import natures.debris.common.blockold.plant.MurkReedBlock;
import natures.debris.common.fluidold.MDFluids;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.Random;

public class MurkReedBlockGenerator implements IBlockGenerator {

    private final MurkReedBlock block;

    public MurkReedBlockGenerator(MurkReedBlock block) {
        this.block = block;
    }

    public MurkReedBlockGenerator() {
        this(MDPlantBlocks.MURK_REED);
    }


    private boolean blocked(IWorldReader world, BlockPos pos, BlockState state) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() && state.getFluidState().getFluid() != MDFluids.MURKY_WATER || block.isSelfState(world, pos, state);
    }

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        if (block.canGenerateAt(world, pos, world.getBlockState(pos)) && !blocked(world, pos, world.getBlockState(pos))) {
            int uwHeight = rand.nextInt(10);
            int owHeight = rand.nextInt(3);
            if (rand.nextInt(4) == 0) owHeight++;

            int m = 0;
            MovingBlockPos rpos = new MovingBlockPos(pos);

            int height = 0;
            for (int i = 0; i < uwHeight; i++) {
                IFluidState state = world.getFluidState(rpos);
                if (state.getFluid() == MDFluids.MURKY_WATER) {
                    height++;
                } else {
                    break;
                }
                rpos.moveUp();
            }

            for (int i = 0; i < owHeight; i++) {
                IFluidState state = world.getFluidState(rpos);
                if (state.getFluid() != MDFluids.MURKY_WATER) {
                    height++;
                } else {
                    break;
                }
                rpos.moveUp();
            }

            if (height > 10) height = 10;

            rpos.setPos(pos);

            for (int i = 0; i < height; i++) {
                rpos.moveUp();
                boolean end = i == height - 1;
                if (blocked(world, rpos, world.getBlockState(rpos))) {
                    end = true;
                }
                rpos.moveDown();
                boolean start = i == 0;
                if (!blocked(world, rpos, world.getBlockState(rpos))) {
                    boolean water = world.getFluidState(rpos).getFluid() == MDFluids.MURKY_WATER;
                    world.setBlockState(rpos, block.getDefaultState().with(MurkReedBlock.WATERLOGGED, water).with(MurkReedBlock.ROOT, start).with(MurkReedBlock.END, end), 2 | 16);
                    m++;
                } else {
                    break;
                }
                rpos.moveUp();
            }
            return m > 0;
        }
        return false;
    }
}
