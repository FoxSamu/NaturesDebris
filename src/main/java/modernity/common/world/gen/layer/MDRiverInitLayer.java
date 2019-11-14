/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

/**
 * Biome layer that initializes the river field.
 */
public enum MDRiverInitLayer implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply( INoiseRandom context, int x, int z ) {
        return context.random( 299999 ) + 2;
    }
}