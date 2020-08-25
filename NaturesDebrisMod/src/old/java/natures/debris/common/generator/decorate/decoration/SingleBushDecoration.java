/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.decoration;

import natures.debris.common.generator.blocks.IBlockGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class SingleBushDecoration implements IDecoration {
    private final IBlockGenerator gen;

    public SingleBushDecoration(IBlockGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void generate(IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator) {
        gen.generateBlock(world, pos, rand);
    }

    public IBlockGenerator getGen() {
        return gen;
    }
}
