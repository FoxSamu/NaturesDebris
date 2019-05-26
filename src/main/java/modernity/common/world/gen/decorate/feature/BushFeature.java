package modernity.common.world.gen.decorate.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;

import modernity.api.util.EcoBlockPos;
import modernity.common.world.gen.decorate.util.IBlockProvider;

import java.util.Random;

public class BushFeature extends ImprovedFeature<BushFeature.Config> {

    @Override
    public boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGen, Random rand, BlockPos pos, Config config ) {
        int placed = 0;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            for( int i = 0; i < config.iterations; i++ ) {
                int r2 = config.radius * 2 + 1;
                int rx = rand.nextInt( r2 ) + pos.getX();
                int ry = rand.nextInt( r2 ) + pos.getY();
                int rz = rand.nextInt( r2 ) + pos.getZ();

                rpos.setPos( rx, ry, rz );

                if( config.provider.provide( world, rpos, rand ) ) {
                    placed++;
                }
            }
        }

        return placed > 0;
    }

    public static class Config implements IFeatureConfig {
        public final int iterations;
        public final int radius;
        public final IBlockProvider provider;

        public Config( int iterations, int radius, IBlockProvider provider ) {
            this.iterations = iterations;
            this.radius = radius;
            this.provider = provider;
        }
    }
}
