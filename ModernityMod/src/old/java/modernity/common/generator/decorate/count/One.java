/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class One implements IDecorCount {
    @Override
    public int count(IWorld world, int cx, int cz, Random rand) {
        return 1;
    }
}
