/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class BelowHeight implements IDecorPosition {

    private final int max;

    public BelowHeight(int max) {
        this.max = max;
    }

    @Override
    public BlockPos findPosition(IWorld world, int cx, int cz, Random rand) {
        int x = rand.nextInt(16) + cx * 16;
        int z = rand.nextInt(16) + cz * 16;
        int y = rand.nextInt(max + 1);

        return new BlockPos(x, y, z);
    }
}
