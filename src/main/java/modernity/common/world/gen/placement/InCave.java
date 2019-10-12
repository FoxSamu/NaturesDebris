/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.world.gen.placement;

import modernity.common.util.CaveUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.*;

import java.util.Random;
import java.util.stream.Stream;

public class InCave extends Placement<NoPlacementConfig> {
    public InCave() {
        super( dynamic -> IPlacementConfig.NO_PLACEMENT_CONFIG );
    }

    @Override
    public Stream<BlockPos> getPositions( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random random, NoPlacementConfig config, BlockPos pos ) {
        return Stream.of( CaveUtil.randomPosInCave( pos, world, random ) );
    }

    public static class WithFrequency extends Placement<FrequencyConfig> {

        public WithFrequency() {
            super( FrequencyConfig::deserialize );
        }

        @Override
        public Stream<BlockPos> getPositions( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random random, FrequencyConfig config, BlockPos pos ) {
            Stream.Builder<BlockPos> builder = Stream.builder();
            for( int i = 0; i < config.count; i++ ) {
                BlockPos npos = CaveUtil.randomPosInCave( pos, world, random );
                builder.add( npos );
            }
            return builder.build();
        }
    }

    public static class WithChance extends Placement<ChanceConfig> {
        public WithChance() {
            super( ChanceConfig::deserialize );
        }

        @Override
        public Stream<BlockPos> getPositions( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkgen, Random random, ChanceConfig config, BlockPos pos ) {
            if( random.nextInt( config.chance ) == 0 ) {
                return Stream.of( CaveUtil.randomPosInCave( pos, world, random ) );
            }
            return Stream.empty();
        }
    }
}
