/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.world.gen.decorate.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;

import modernity.common.util.CaveUtil;

import java.util.Random;

public class InCave extends BasePlacement<NoPlacementConfig> {
    @Override
    public <C extends IFeatureConfig> boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkgen, Random random, BlockPos pos, NoPlacementConfig noConfig, Feature<C> feature, C cfg ) {
        BlockPos npos = CaveUtil.randomPosInCave( pos, world, random );
        return feature.place( world, chunkgen, random, npos, cfg );
    }

    public static class WithFrequency extends BasePlacement<FrequencyConfig> {
        @Override
        public <C extends IFeatureConfig> boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkgen, Random random, BlockPos pos, FrequencyConfig config, Feature<C> feature, C cfg ) {
            boolean placed = false;
            for( int i = 0; i < config.frequency; i++ ) {
                BlockPos npos = CaveUtil.randomPosInCave( pos, world, random );
                placed |= feature.place( world, chunkgen, random, npos, cfg );
            }
            return placed;
        }
    }

    public static class WithChance extends BasePlacement<ChanceConfig> {
        @Override
        public <C extends IFeatureConfig> boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkgen, Random random, BlockPos pos, ChanceConfig config, Feature<C> feature, C cfg ) {
            if( random.nextInt( config.chance ) == 0 ) {
                BlockPos npos = CaveUtil.randomPosInCave( pos, world, random );
                return feature.place( world, chunkgen, random, npos, cfg );
            }
            return false;
        }
    }
}
