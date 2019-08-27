/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import modernity.api.util.EcoBlockPos;
import modernity.common.world.gen.decorate.util.IBlockProvider;

import java.util.Random;

public class GroupedBushFeature extends Feature<GroupedBushFeature.Config> {

    @Override
    public boolean place( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGen, Random rand, BlockPos pos, Config config ) {
        int placed = 0;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            for( int i = 0; i < config.iterations; i++ ) {
                int radius = rand.nextInt( Math.min( 4, config.radius ) ) + 2;
                int sr = config.radius - radius;
                int r2 = sr * 2 + 1;
                int rx = rand.nextInt( r2 ) + pos.getX();
                int ry = rand.nextInt( r2 ) + pos.getY();
                int rz = rand.nextInt( r2 ) + pos.getZ();

                for( int x = - radius; x <= radius; x++ ) {
                    for( int z = - radius; z <= radius; z++ ) {
                        for( int y = - radius; y <= radius; y++ ) {
                            int posx = rx + x;
                            int posy = ry + y;
                            int posz = rz + z;
                            if( x * x + y * y + z * z <= radius * radius ) {
                                rpos.setPos( posx, posy, posz );

                                if( rand.nextInt( config.chance ) == 0 && config.provider.provide( world, rpos, rand ) ) {
                                    placed++;
                                }
                            }
                        }
                    }
                }
            }
        }

        return placed > 0;
    }

    public static class Config implements IFeatureConfig {
        public final int iterations;
        public final int radius;
        public final int chance;
        public final IBlockProvider provider;

        public Config( int iterations, int radius, int chance, IBlockProvider provider ) {
            this.iterations = iterations;
            this.radius = radius;
            this.chance = chance;
            this.provider = provider;
        }
    }
}
