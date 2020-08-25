/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class Chance implements IDecorCount {
    private final double chance;

    public Chance(double chance) {
        this.chance = chance;
    }

    public Chance(double chance, double max) {
        this.chance = chance / max;
    }

    @Override
    public int count(IWorld world, int cx, int cz, Random rand) {
        return rand.nextDouble() < chance ? 1 : 0;
    }
}
