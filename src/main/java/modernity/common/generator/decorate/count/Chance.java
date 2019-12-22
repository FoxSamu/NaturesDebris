/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class Chance implements IDecorCount {
    private final double chance;

    public Chance( double chance ) {
        this.chance = chance;
    }

    @Override
    public int count( IWorld world, int cx, int cz, Random rand ) {
        return rand.nextDouble() < chance ? 1 : 0;
    }
}
