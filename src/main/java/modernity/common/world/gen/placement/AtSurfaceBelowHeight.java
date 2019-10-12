/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.world.gen.placement;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class AtSurfaceBelowHeight extends Placement<AtSurfaceBelowHeight.Config> {

    public AtSurfaceBelowHeight() {
        super( dynamic -> new FrequencyConfig( 255, 1 ) );
    }

    @Override
    public Stream<BlockPos> getPositions( IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, Config config, BlockPos pos ) {
        int freq = config.getFrequency( rand );
        Stream.Builder<BlockPos> builder = Stream.builder();
        for( int i = 0; i < freq; i++ ) {
            int x = rand.nextInt( 16 );
            int z = rand.nextInt( 16 );

            int y = world.getHeight( Heightmap.Type.OCEAN_FLOOR_WG, x + pos.getX(), z + pos.getZ() );

            if( y > config.maxHeight ) continue;

            builder.add( new BlockPos( x + pos.getX(), y, z + pos.getZ() ) );
        }
        return builder.build();
    }

    public static abstract class Config implements IPlacementConfig {
        public final int maxHeight;

        public Config( int maxHeight ) {
            this.maxHeight = maxHeight;
        }

        public abstract int getFrequency( Random rand );

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops, ops.emptyMap() );
        }
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
