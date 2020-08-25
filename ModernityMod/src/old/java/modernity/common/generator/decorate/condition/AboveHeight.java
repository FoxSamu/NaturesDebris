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

public class AboveHeight implements IDecorCondition {

    private final int min;

    public AboveHeight(int min) {
        this.min = min;
    }

    @Override
    public boolean canGenerate(IWorld world, BlockPos pos, Random rand) {
        return pos.getY() >= min;
    }
}
