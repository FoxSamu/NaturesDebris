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

@FunctionalInterface
public interface IBlockGenerator {
    boolean generateBlock(IWorld world, BlockPos pos, Random rand);

    default IBlockGenerator orElse(IBlockGenerator other) {
        return (world, pos, rand) -> {
            if (generateBlock(world, pos, rand)) return true;
            else return other.generateBlock(world, pos, rand);
        };
    }
}
