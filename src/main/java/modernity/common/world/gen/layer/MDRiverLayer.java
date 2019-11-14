/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.layer;

import modernity.common.biome.MDBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

/**
 * Biome layer that actually generates the river shapes from the {@linkplain MDRiverInitLayer river fields}.
 */
public enum MDRiverLayer implements ICastleTransformer {
    INSTANCE;

    public static final int RIVER = Registry.BIOME.getId( MDBiomes.RIVER );

    private static int riverFilter( int value ) {
        return value >= 2 ? 2 + ( value & 1 ) : value;
    }

    @Override
    public int apply( INoiseRandom context, int north, int west, int south, int east, int center ) {
        int i = riverFilter( center );
        return i == riverFilter( west ) && i == riverFilter( north ) && i == riverFilter( east ) && i == riverFilter( south )
               ? - 1
               : RIVER;
    }
}