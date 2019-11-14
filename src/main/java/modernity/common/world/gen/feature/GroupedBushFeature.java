/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.IBlockProvider;
import modernity.api.util.MovingBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

/**
 * A feature that generates a grouped bush.
 * <pre>
 *         * * *
 *       * * *   *
 *           * * *
 *         * *
 *     * * * *   * *
 *         * * * *
 * </pre>
 *
 * @see ClusterBushFeature
 */
public class GroupedBushFeature extends Feature<GroupedBushFeature.Config> {

    public GroupedBushFeature() {
        super( dynamic -> new Config( 1, 1, 1, null ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random rand, BlockPos pos, Config config ) {
        int placed = 0;

        MovingBlockPos rpos = new MovingBlockPos();
        {
            for( int i = 0; i < config.iterations; i++ ) {
                int radius = rand.nextInt( Math.min( 4, config.radius ) ) + 2;
                int sr = config.radius - radius;
                int r2 = sr * 2 + 1;
                int rx = rand.nextInt( r2 ) + pos.getX() - config.radius;
                int ry = rand.nextInt( r2 ) + pos.getY() - config.radius;
                int rz = rand.nextInt( r2 ) + pos.getZ() - config.radius;

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

    /**
     * A configuration for the {@link GroupedBushFeature}.
     */
    public static class Config implements IFeatureConfig {
        public final int iterations;
        public final int radius;
        public final int chance;
        public final IBlockProvider provider;

        /**
         * Creates a grouped bush config.
         * @param iterations The amount of iterations (attempts to generate a sphere of plants)
         * @param radius     The radius of the feature
         * @param chance     The chance that a plant generates in an attempt.
         * @param provider   The block provider to generate.
         */
        public Config( int iterations, int radius, int chance, IBlockProvider provider ) {
            this.iterations = iterations;
            this.radius = radius;
            this.chance = chance;
            this.provider = provider;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
