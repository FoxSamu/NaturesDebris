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

public class Root implements IDecorPosition {

    private final int y;

    public Root(int y) {
        this.y = y;
    }

    @Override
    public BlockPos findPosition(IWorld world, int cx, int cz, Random rand) {
        return new BlockPos(0, 0, 0);
    }
}
