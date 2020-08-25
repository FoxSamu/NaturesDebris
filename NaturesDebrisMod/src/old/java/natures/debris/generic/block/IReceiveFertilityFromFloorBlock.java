/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IReceiveFertilityFromFloorBlock {
    void receiveFertilityFromFloor(World world, BlockPos pos, BlockState state, Random rand);
}
