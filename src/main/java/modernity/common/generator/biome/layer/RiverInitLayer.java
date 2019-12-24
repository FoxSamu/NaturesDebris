/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

/**
 * Biome layer that initializes the river field.
 */
public enum RiverInitLayer implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply( INoiseRandom context, int x, int z ) {
        return context.random( 299999 ) + 2;
    }
}