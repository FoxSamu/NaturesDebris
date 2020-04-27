/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.farmland;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@FunctionalInterface
public interface IFarmlandBlock {
    IFarmland getFarmland( IWorld world, BlockPos pos );
}
