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

public class GroupedBushDecoration implements IDecoration {
    private final int iterations;
    private final int radius;
    private final int chance;
    private final IBlockProvider provider;

    public GroupedBushDecoration( int iterations, int radius, int chance, IBlockProvider provider ) {
        this.iterations = iterations;
        this.radius = radius;
        this.chance = chance;
        this.provider = provider;
    }

    public int getIterations() {
        return iterations;
    }

    public int getRadius() {
        return radius;
    }

    public int getChance() {
        return chance;
    }

    public IBlockProvider getProvider() {
        return provider;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        MovingBlockPos rpos = new MovingBlockPos();

        for( int i = 0; i < iterations; i++ ) {
            int rad = rand.nextInt( Math.min( 4, radius ) ) + 2;
            int sr = radius - rad;
            int r2 = sr * 2 + 1;
            int rx = rand.nextInt( r2 ) + pos.getX() - rad;
            int ry = rand.nextInt( r2 ) + pos.getY() - rad;
            int rz = rand.nextInt( r2 ) + pos.getZ() - rad;

            for( int x = - rad; x <= rad; x++ ) {
                for( int z = - rad; z <= rad; z++ ) {
                    for( int y = - rad; y <= rad; y++ ) {
                        int posx = rx + x;
                        int posy = ry + y;
                        int posz = rz + z;
                        if( x * x + y * y + z * z <= rad * rad ) {
                            rpos.setPos( posx, posy, posz );

                            if( rand.nextInt( chance ) == 0 ) {
                                provider.provide( world, rpos, rand );
                            }
                        }
                    }
                }
            }
        }
    }
}
