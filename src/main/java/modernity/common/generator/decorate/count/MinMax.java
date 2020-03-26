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

public class MinMax implements IDecorCount {
    private final int min;
    private final int max;

    public MinMax( int min, int max ) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int count( IWorld world, int cx, int cz, Random rand ) {
        return rand.nextInt( max - min + 1 ) + min;
    }
}
