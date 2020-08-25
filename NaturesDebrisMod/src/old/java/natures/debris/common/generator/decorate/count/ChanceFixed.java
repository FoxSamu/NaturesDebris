/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class ChanceFixed implements IDecorCount {
    private final double chance;
    private final int count;

    public ChanceFixed(double chance, int count) {
        this.chance = chance;
        this.count = count;
    }

    @Override
    public int count(IWorld world, int cx, int cz, Random rand) {
        return rand.nextDouble() < chance ? count : 0;
    }
}
