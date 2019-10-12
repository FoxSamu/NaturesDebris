/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum MDRiverInitLayer implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply( INoiseRandom context, int x, int z ) {
        return context.random( 299999 ) + 2;
    }
}