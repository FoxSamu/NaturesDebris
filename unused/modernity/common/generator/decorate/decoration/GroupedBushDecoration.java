/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.generic.util.MovingBlockPos;
import modernity.common.generator.blocks.IBlockGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class GroupedBushDecoration implements IDecoration {
    private final int iterations;
    private final int radius;
    private final double chance;
    private final IBlockGenerator gen;

    public GroupedBushDecoration( int iterations, int radius, int chance, IBlockGenerator gen ) {
        this.iterations = iterations;
        this.radius = radius;
        this.chance = 1D / chance;
        this.gen = gen;
    }

    public GroupedBushDecoration( int iterations, int radius, double chance, IBlockGenerator gen ) {
        this.iterations = iterations;
        this.radius = radius;
        this.chance = chance;
        this.gen = gen;
    }

    public int getIterations() {
        return iterations;
    }

    public int getRadius() {
        return radius;
    }

    public double getChance() {
        return chance;
    }

    public IBlockGenerator getGen() {
        return gen;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        MovingBlockPos rpos = new MovingBlockPos();

        for( int i = 0; i < iterations; i++ ) {
            int rad = Math.min( radius, rand.nextInt( Math.min( 4, radius ) ) + 1 );
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

                            if( rand.nextDouble() < chance ) {
                                gen.generateBlock( world, rpos, rand );
                            }
                        }
                    }
                }
            }
        }
    }
}
