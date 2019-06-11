/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.layer;

import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import modernity.common.biome.MDBiomes;

public enum GenLayerRiver implements ICastleTransformer {
    INSTANCE;

    public static final int RIVER = IRegistry.BIOME.getId( MDBiomes.RIVER );

    public int apply( IContext context, int north, int east, int south, int west, int center ) {
        int i = riverFilter( center );
        return i == riverFilter( west ) && i == riverFilter( north ) && i == riverFilter( east ) && i == riverFilter( south ) ? - 1 : RIVER;
    }

    private static int riverFilter( int value ) {
        return value >= 2 ? 2 + ( value & 1 ) : value;
    }
}