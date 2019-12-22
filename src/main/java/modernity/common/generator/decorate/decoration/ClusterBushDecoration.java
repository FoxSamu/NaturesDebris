/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.api.util.IBlockProvider;
import modernity.api.util.MovingBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class ClusterBushDecoration implements IDecoration {
    private final int iterations;
    private final int radius;
    private final IBlockProvider provider;

    public ClusterBushDecoration( int iterations, int radius, IBlockProvider provider ) {
        this.iterations = iterations;
        this.radius = radius;
        this.provider = provider;
    }

    public int getIterations() {
        return iterations;
    }

    public int getRadius() {
        return radius;
    }

    public IBlockProvider getProvider() {
        return provider;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        int r2 = radius * 2 + 1;
        MovingBlockPos rpos = new MovingBlockPos();
        for( int i = 0; i < iterations; i++ ) {
            int rx = rand.nextInt( r2 ) + pos.getX() - radius;
            int ry = rand.nextInt( r2 ) + pos.getY() - radius;
            int rz = rand.nextInt( r2 ) + pos.getZ() - radius;

            rpos.setPos( rx, ry, rz );

            provider.provide( world, rpos, rand );
        }
    }
}
