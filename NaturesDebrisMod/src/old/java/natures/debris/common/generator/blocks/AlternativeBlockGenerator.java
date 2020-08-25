/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class AlternativeBlockGenerator implements IBlockGenerator {
    private final IBlockGenerator[] generators;

    public AlternativeBlockGenerator(IBlockGenerator gen, IBlockGenerator... others) {
        generators = new IBlockGenerator[1 + others.length];
        generators[0] = gen;
        System.arraycopy(others, 0, generators, 1, others.length);
    }

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        for (IBlockGenerator generator : generators) {
            if (generator.generateBlock(world, pos, rand)) {
                return true;
            }
        }
        return false;
    }
}
