/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
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
    boolean doesVaporize( World world, BlockPos pos );
}
