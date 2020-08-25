/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.block.fluid;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementing fluids have a custom vaporizing behaviour.
 *
 * @author RGSW
 */
@FunctionalInterface
public interface ICustomVaporize {
    boolean doesVaporize(World world, BlockPos pos);
}
