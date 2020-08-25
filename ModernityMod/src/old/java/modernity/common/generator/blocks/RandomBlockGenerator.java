/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class RandomBlockGenerator implements IBlockGenerator {
    private final IBlockGenerator[] generators;

    public RandomBlockGenerator(IBlockGenerator gen, IBlockGenerator... others) {
        generators = new IBlockGenerator[1 + others.length];
        generators[0] = gen;
        System.arraycopy(others, 0, generators, 1, others.length);
    }


    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        return generators[rand.nextInt(generators.length)].generateBlock(world, pos, rand);
    }
}
