/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@FunctionalInterface
public interface IFarmlandBlock {
    IFarmland getFarmland( IWorld world, BlockPos pos );
}
