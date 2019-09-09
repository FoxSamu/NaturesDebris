/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.world.gen.decorate.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;

import java.util.Random;

public class AtSurfaceBelowHeight extends BasePlacement<AtSurfaceBelowHeight.Config> {
    @Override
    public <C extends IFeatureConfig> boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, Random rand, BlockPos pos, AtSurfaceBelowHeight.Config placementConfig, Feature<C> feature, C featureConfig ) {
        boolean placed = false;
        int freq = placementConfig.getFrequency( rand );
        for( int i = 0; i < freq; i++ ) {
            int x = rand.nextInt( 16 );
            int z = rand.nextInt( 16 );

            int y = world.getHeight( Heightmap.Type.OCEAN_FLOOR_WG, x + pos.getX(), z + pos.getZ() );

            if( y > placementConfig.maxHeight ) continue;

            placed |= feature.place( world, chunkGenerator, rand, new BlockPos( x + pos.getX(), y, z + pos.getZ() ), featureConfig );
        }
        return placed;
    }

    public static abstract class Config implements IPlacementConfig {
        public final int maxHeight;

        public Config( int maxHeight ) {
            this.maxHeight = maxHeight;
        }

        public abstract int getFrequency( Random rand );
    }

    public static class FrequencyConfig extends Config {
        public final int frequency;

        public FrequencyConfig( int maxHeight, int frequency ) {
            super( maxHeight );
            this.frequency = frequency;
        }

        @Override
        public int getFrequency( Random rand ) {
            return frequency;
        }
    }

    public static class ChanceConfig extends Config {
        public final int chance;

        public ChanceConfig( int maxHeight, int chance ) {
            super( maxHeight );
            this.chance = chance;
        }

        @Override
        public int getFrequency( Random rand ) {
            return rand.nextInt( chance ) == 0 ? 1 : 0;
        }
    }

    public static class MultiChanceConfig extends Config {
        public final int chance;
        public final int max;

        public MultiChanceConfig( int maxHeight, int chance, int max ) {
            super( maxHeight );
            this.chance = chance;
            this.max = max;
        }

        @Override
        public int getFrequency( Random rand ) {
            return rand.nextInt( chance ) == 0 ? rand.nextInt( max ) + 1 : 0;
        }
    }
}
