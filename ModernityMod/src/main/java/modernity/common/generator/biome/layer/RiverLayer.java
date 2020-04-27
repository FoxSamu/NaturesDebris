/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.region.layer.ICastleTransformerLayer;

public class RiverLayer implements ICastleTransformerLayer, IBiomeLayer {
    public static final RiverLayer INSTANCE = new RiverLayer();

    protected RiverLayer() {
    }

    @Override
    public int generate( IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ ) {
        int centerFilter = riverFilter( center );
        int negXFilter = riverFilter( negX );
        int negZFilter = riverFilter( negZ );
        int posXFilter = riverFilter( posX );
        int posZFilter = riverFilter( posZ );
        boolean same = areAllSame( centerFilter, negXFilter, negZFilter, posXFilter, posZFilter );
        return same ? 0 : 1;
    }

    private static int riverFilter( int value ) {
        return value >= 2 ? 2 + ( value & 1 ) : value;
    }

    private static boolean areAllSame( int a, int b, int c, int d, int e ) {
        return e == a && e == b && e == c && e == d;
    }
}
