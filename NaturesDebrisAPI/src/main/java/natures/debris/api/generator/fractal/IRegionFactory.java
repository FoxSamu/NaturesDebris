/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.generator.fractal;


import java.util.function.Function;

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
     *
     * @return The built region.
     */
    R buildRegion();

    /**
     * Makes a {@link FractalGenerator} from a built {@link IRegion}.
     *
     * @return The created {@link FractalGenerator} instance.
     */
    default <D extends FractalGenerator<?>> D makeGenerator(Function<IRegion, ? extends D> factory) {
        return factory.apply(buildRegion());
    }
}
