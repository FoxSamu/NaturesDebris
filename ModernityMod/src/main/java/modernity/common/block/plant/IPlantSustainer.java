/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

@FunctionalInterface
public interface IPlantSustainer {
    boolean canSustainPlant( ILightReader world, BlockPos pos, BlockState state, Block plant, Direction side );
}
