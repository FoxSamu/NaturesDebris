/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class Always implements IDecorCondition {
    @Override
    public boolean canGenerate( IWorld world, BlockPos pos, Random rand ) {
        return true;
    }
}
