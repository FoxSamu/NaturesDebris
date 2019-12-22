/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class FeatureDecoration<T extends IFeatureConfig> implements IDecoration {

    private final Feature<T> feature;
    private final T config;

    public FeatureDecoration( Feature<T> feature, T config ) {
        this.feature = feature;
        this.config = config;
    }

    public Feature<T> getFeature() {
        return feature;
    }

    public T getConfig() {
        return config;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        feature.place( world, chunkGenerator, rand, pos, config );
    }
}
