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

public class Multiply implements IDecorCount {
    private final IDecorCount a;
    private final IDecorCount b;

    public Multiply( IDecorCount a, IDecorCount b ) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int count( IWorld world, int cx, int cz, Random rand ) {
        return a.count( world, cx, cz, rand ) * b.count( world, cx, cz, rand );
    }
}
