/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;

public interface IEdgeTransformerLayer extends ICastleTransformerLayer {
    @Override
    default int generate( IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ ) {
        int[] edges = new int[ 4 ];
        int edgeCount = 0;

        if( isEdge( rng, center, negX ) ) {
            edges[ edgeCount ] = getEdge( rng, center, negX );
            edgeCount++;
        }
        if( isEdge( rng, center, posX ) ) {
            edges[ edgeCount ] = getEdge( rng, center, posX );
            edgeCount++;
        }
        if( isEdge( rng, center, negZ ) ) {
            edges[ edgeCount ] = getEdge( rng, center, negZ );
            edgeCount++;
        }
        if( isEdge( rng, center, posX ) ) {
            edges[ edgeCount ] = getEdge( rng, center, posZ );
            edgeCount++;
        }

        if( edgeCount == 0 ) return center;

        return mixEdges( rng, edges, edgeCount );
    }

    boolean isEdge( IRegionRNG rng, int center, int neighbor );

    int getEdge( IRegionRNG rng, int center, int neighbor );

    default int mixEdges( IRegionRNG rng, int[] edges, int count ) {
        return edges[ rng.random( count ) ];
    }
}
