/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

@FunctionalInterface
public interface IDecoration {
    void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator );
}
