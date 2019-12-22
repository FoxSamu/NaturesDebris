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

public class ChanceFixed implements IDecorCount {
    private final double chance;
    private final int count;

    public ChanceFixed( double chance, int count ) {
        this.chance = chance;
        this.count = count;
    }

    @Override
    public int count( IWorld world, int cx, int cz, Random rand ) {
        return rand.nextDouble() < chance ? count : 0;
    }
}
