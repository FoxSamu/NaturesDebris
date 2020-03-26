/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Map;
import java.util.Random;

public class WeightedBlockGenerator implements IBlockGenerator {
    private final Map<IBlockGenerator, Double> generators;
    private final double totalWeight;

    private WeightedBlockGenerator( Map<IBlockGenerator, Double> generators ) {
        this.generators = generators;
        double total = 0;
        for( Map.Entry<IBlockGenerator, Double> e : generators.entrySet() ) {
            total += e.getValue();
        }
        this.totalWeight = total;
    }

    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        double r = rand.nextDouble() * totalWeight;

        for( Map.Entry<IBlockGenerator, Double> e : generators.entrySet() ) {
            r -= e.getValue();
            if( r <= 0 ) {
                return e.getKey().generateBlock( world, pos, rand );
            }
        }

        return false;
    }

    public static Builder builder( IBlockGenerator gen, double wgt ) {
        return new Builder().add( gen, wgt );
    }

    public static class Builder {
        private final ImmutableMap.Builder<IBlockGenerator, Double> mapBuilder = ImmutableMap.builder();

        private Builder() {
        }

        public Builder add( IBlockGenerator gen, double wgt ) {
            mapBuilder.put( gen, wgt );
            return this;
        }

        public WeightedBlockGenerator build() {
            return new WeightedBlockGenerator( mapBuilder.build() );
        }
    }
}
