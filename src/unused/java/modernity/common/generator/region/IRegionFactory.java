/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.region;

import modernity.common.generator.biome.BiomeGenerator;

/**
 * A region factory builds {@link IRegion} instances. These factories are usually wrapped by other factories that apply
 * transformations to the {@link IRegion}s they build, forming a factory chain. A {@link IRegionBuilder} is used to
 * build the factory chain and also implements this interface.
 *
 * @param <R> The region type this factory builds.
 */
@FunctionalInterface
public interface IRegionFactory<R extends IRegion> {
    /**
     * Builds a {@link IRegion}.
     * @return The built region.
     */
    R buildRegion();

    /**
     * Makes a {@link BiomeGenerator} from a built {@link IRegion}.
     * @return The created {@link BiomeGenerator} instance.
     */
    default BiomeGenerator makeGenerator() {
        return new BiomeGenerator( buildRegion() );
    }
}
