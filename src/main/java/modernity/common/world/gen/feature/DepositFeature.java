/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;
import java.util.function.Predicate;

public class DepositFeature extends Feature<DepositFeature.Config> {

    public DepositFeature() {
        super( dynamic -> new Config( 1, s -> false, Blocks.AIR.getDefaultState() ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos pos, Config config ) {
        MovingBlockPos rpos = new MovingBlockPos();
        {
            int itr = config.size * 5 + rand.nextInt( config.size );
            for( int i = 0; i < itr; i++ ) {
                double radius = rand.nextDouble() / 2 * config.size;
                double sx = ( rand.nextDouble() - rand.nextDouble() ) * config.size / 2;
                double sy = ( rand.nextDouble() - rand.nextDouble() ) * config.size / 2;
                double sz = ( rand.nextDouble() - rand.nextDouble() ) * config.size / 2;

                int nx = MathHelper.floor( sx - radius );
                int ny = MathHelper.floor( sy - radius );
                int nz = MathHelper.floor( sz - radius );
                int px = MathHelper.ceil( sx + radius );
                int py = MathHelper.ceil( sy + radius );
                int pz = MathHelper.ceil( sz + radius );

                for( int x = nx; x <= px; x++ ) {
                    double lx = x - sx;
                    for( int z = nz; z <= pz; z++ ) {
                        double lz = z - sz;
                        for( int y = ny; y <= py; y++ ) {
                            double ly = y - sy;
                            if( lx * lx + ly * ly + lz * lz < radius * radius ) {
                                rpos.setPos( pos );
                                rpos.addPos( x, y, z );
                                rpos.moveDown();
                                BlockState current = world.getBlockState( rpos );
                                if( config.replace.test( current ) ) {
                                    world.setBlockState( rpos, config.state, BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NOTIFY_CLIENTS );
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public static class Config implements IFeatureConfig {
        public final int size;
        public final Predicate<BlockState> replace;
        public final BlockState state;

        public Config( int size, Predicate<BlockState> replace, BlockState state ) {
            this.size = size;
            this.replace = replace;
            this.state = state;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
