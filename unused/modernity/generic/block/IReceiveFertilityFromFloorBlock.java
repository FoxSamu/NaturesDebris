/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.generic.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IReceiveFertilityFromFloorBlock {
    void receiveFertilityFromFloor( World world, BlockPos pos, BlockState state, Random rand );
}
