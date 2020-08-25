/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.api.generator.fractal.layer;

import modernity.api.generator.fractal.IRegionRNG;

public interface IEdgeTransformerLayer extends ICastleTransformerLayer {
    @Override
    default int generate(IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ) {
        int[] edges = new int[4];
        int edgeCount = 0;

        if(isEdge(rng, center, negX)) {
            edges[edgeCount] = getEdge(rng, center, negX);
            edgeCount++;
        }
        if(isEdge(rng, center, posX)) {
            edges[edgeCount] = getEdge(rng, center, posX);
            edgeCount++;
        }
        if(isEdge(rng, center, negZ)) {
            edges[edgeCount] = getEdge(rng, center, negZ);
            edgeCount++;
        }
        if(isEdge(rng, center, posX)) {
            edges[edgeCount] = getEdge(rng, center, posZ);
            edgeCount++;
        }

        if(edgeCount == 0) return center;

        return mixEdges(rng, edges, edgeCount);
    }

    boolean isEdge(IRegionRNG rng, int center, int neighbor);

    int getEdge(IRegionRNG rng, int center, int neighbor);

    default int mixEdges(IRegionRNG rng, int[] edges, int count) {
        return edges[rng.random(count)];
    }
}
