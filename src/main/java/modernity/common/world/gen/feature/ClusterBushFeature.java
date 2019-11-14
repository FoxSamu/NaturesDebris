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
 * A feature that generates a cluster bush.
 * <pre>
 *         *       *
 *           *   *
 *       *           *
 *     *     *
 *             *
 *         *       *
 * </pre>
 * @see GroupedBushFeature
 */
public class ClusterBushFeature extends Feature<ClusterBushFeature.Config> {

    public ClusterBushFeature() {
        super( dynamic -> new Config( 1, 1, null ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random rand, BlockPos pos, Config config ) {
        int placed = 0;

        MovingBlockPos rpos = new MovingBlockPos();
        {
            for( int i = 0; i < config.iterations; i++ ) {
                int r2 = config.radius * 2 + 1;
                int rx = rand.nextInt( r2 ) + pos.getX() - config.radius;
                int ry = rand.nextInt( r2 ) + pos.getY() - config.radius;
                int rz = rand.nextInt( r2 ) + pos.getZ() - config.radius;

                rpos.setPos( rx, ry, rz );

                if( config.provider.provide( world, rpos, rand ) ) {
                    placed++;
                }
            }
        }

        return placed > 0;
    }

    /**
     * Configuration for the cluster bush feature.
     */
    public static class Config implements IFeatureConfig {
        final int iterations;
        final int radius;
        final IBlockProvider provider;

        /**
         * Creates a cluster bush configuration.
         * @param iterations The amount of iterations (attempts to place a plant at a random position)
         * @param radius     The radius of the square to generate in.
         * @param provider   The {@linkplain IBlockProvider block provider} to place.
         */
        public Config( int iterations, int radius, IBlockProvider provider ) {
            this.iterations = iterations;
            this.radius = radius;
            this.provider = provider;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
