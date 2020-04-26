/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class RangeHeight implements IDecorCondition {

    private final int min;
    private final int max;

    public RangeHeight( int min, int max ) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean canGenerate( IWorld world, BlockPos pos, Random rand ) {
        return pos.getY() >= min && pos.getY() <= max;
    }
}
