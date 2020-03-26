/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.region;

import modernity.common.generator.biome.BiomeGenerator;
import modernity.common.generator.region.layer.*;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A generic builder for {@link IRegion}s.
 *
 * @param <R> The region type.
 * @param <T> The type of this builder to return in each property setter.
 * @see CachingRegionBuilder
 */
public interface IRegionBuilder<R extends IRegion, T extends IRegionBuilder<R, T>> extends IRegionFactory<R> {
    /**
     * @return The context this builder depends on
     */
    IRegionContext<R> getContext();

    /**
     * @return The underlying {@link IRegionFactory} chain
     */
    IRegionFactory<R> getFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    default R buildRegion() {
        return getFactory().buildRegion();
    }

    /**
     * Applies a factory function to the underlying {@link IRegionFactory} chain.
     *
     * @param function The function to apply
     * @return This instance for chaining.
     */
    T apply( Function<IRegionFactory<R>, IRegionFactory<R>> function );

    /**
     * Sets the current seed.
     *
     * @param seed The new seed
     * @return This instance for chaining
     */
    T setSeed( long seed );

    /**
     * Computes the next, pseudorandom seed.
     *
     * @return The computed seed
     */
    long nextSeed();

    /**
     * Applies multiple {@linkplain ZoomLayer zoom layers} to the underlying {@link IRegionFactory} chain.
     *
     * @param amount The amount of zoom layers to add
     * @return This instance for chaining
     *
     * @see #zoom()
     */
    default T zoom( int amount ) {
        return apply( factory -> ZoomLayer.INSTANCE.magnify( getContext(), nextSeed(), factory, amount ) );
    }

    /**
     * Applies a {@linkplain ZoomLayer zoom layer} to the underlying {@link IRegionFactory} chain.
     *
     * @return This instance for chaining
     *
     * @see #zoom(int)
     */
    default T zoom() {
        return apply( factory -> ZoomLayer.INSTANCE.factory( getContext(), nextSeed(), factory ) );
    }

    /**
     * Applies multiple {@linkplain FuzzyZoomLayer fuzzy zoom layers} to the underlying {@link IRegionFactory} chain.
     *
     * @param amount The amount of fuzzy zoom layers to add
     * @return This instance for chaining
     *
     * @see #zoomFuzzy()
     */
    default T zoomFuzzy( int amount ) {
        return apply( factory -> FuzzyZoomLayer.INSTANCE.magnify( getContext(), nextSeed(), factory, amount ) );
    }

    /**
     * Applies a {@linkplain FuzzyZoomLayer fuzzy zoom layer} to the underlying {@link IRegionFactory} chain.
     *
     * @return This instance for chaining
     *
     * @see #zoomFuzzy(int)
     */
    default T zoomFuzzy() {
        return apply( factory -> FuzzyZoomLayer.INSTANCE.factory( getContext(), nextSeed(), factory ) );
    }

    /**
     * Applies a {@linkplain SmoothingLayer smoothing layer} to the underlying {@link IRegionFactory} chain.
     *
     * @return This instance for chaining
     */
    default T smooth() {
        return apply( factory -> SmoothingLayer.INSTANCE.factory( getContext(), nextSeed(), factory ) );
    }

    /**
     * Applies a {@linkplain VoronoiZoomLayer voronoi zoom layer} to the underlying {@link IRegionFactory} chain.
     *
     * @return This instance for chaining
     */
    default T zoomVoronoi() {
        return apply( factory -> VoronoiZoomLayer.INSTANCE.factory( getContext(), nextSeed(), factory ) );
    }

    /**
     * Applies a {@linkplain ITransformerLayer transformer layer} to the underlying {@link IRegionFactory} chain.
     *
     * @param layer The {@link ITransformerLayer} to apply.
     * @return This instance for chaining
     */
    default T transform( ITransformerLayer layer ) {
        return apply( factory -> layer.factory( getContext(), nextSeed(), factory ) );
    }

    /**
     * Applies a {@linkplain IMergerLayer merger layer} to the underlying {@link IRegionFactory} chain, using the
     * specified {@link IRegionFactory} as second chain.
     *
     * @param layer The {@link IMergerLayer} to apply.
     * @return This instance for chaining
     */
    default T merge( IMergerLayer layer, IRegionFactory<R> otherFactory ) {
        return apply( factory -> layer.factory( getContext(), nextSeed(), factory, otherFactory ) );
    }

    /**
     * Builds a {@link IRegion} and creates a {@link BiomeGenerator} instance, which is then passed as parameter to the
     * specified consumer.
     *
     * @param consumer The consumer that handles the new {@link BiomeGenerator} instance.
     * @return This instance for chaining
     */
    @SuppressWarnings( "unchecked" )
    default T export( Consumer<BiomeGenerator> consumer ) {
        consumer.accept( makeGenerator() );
        return (T) this;
    }

    /**
     * Builds a {@link IRegion} and creates a {@link BiomeGenerator} instance, which is then set in an array at the
     * specified index.
     *
     * @param arr   The array to set the new {@link BiomeGenerator} instance in.
     * @param index The index in the array.
     * @return This instance for chaining
     *
     * @throws ArrayIndexOutOfBoundsException When the specified index is out of the array's bounds.
     */
    @SuppressWarnings( "unchecked" )
    default T export( BiomeGenerator[] arr, int index ) {
        arr[ index ] = makeGenerator();
        return (T) this;
    }
}
