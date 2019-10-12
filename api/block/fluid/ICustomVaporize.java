/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.api.block.fluid;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICustomVaporize {
    boolean doesVaporize( World world, BlockPos pos );
}
