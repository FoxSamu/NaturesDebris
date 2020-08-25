/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.decoration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

@FunctionalInterface
public interface IDecoration {
    void generate(IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator);
}
