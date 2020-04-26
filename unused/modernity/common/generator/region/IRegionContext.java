/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.region;

import modernity.common.generator.region.layer.IGeneratorLayer;
import modernity.common.generator.region.layer.IMergerLayer;
import modernity.common.generator.region.layer.ITransformerLayer;
import modernity.common.generator.region.layer.RandomLayer;

/**
 * A generic region building context.
 *
 * @param <R> The region type this context handles.
 * @see CachingRegionContext
 */
public interface IRegionContext<R extends IRegion> {

    /**
     * Creates a wrapping generator {@link IRegion}.
     *
     * @param generator The underlying {@link IRegion} generator.
     * @return The created {@link IRegion}
     */
    R create( IRegion generator );

    /**
     * Creates a wrapping transformer {@link IRegion}.
     *
     * @param generator The underlying {@link IRegion} generator.
     * @param region    The {@link IRegion} that's being transformed.
     * @return The created {@link IRegion}
     */
    R create( IRegion generator, R region );

    /**
     * Creates a wrapping merger {@link IRegion}.
     *
     * @param generator The underlying {@link IRegion} generator.
     * @param regionA   The first {@link IRegion} that's being merged.
     * @param regionB   The second {@link IRegion} that's being merged.
     * @return The created {@link IRegion}
     */
    R create( IRegion generator, R regionA, R regionB );

    /**
     * Returns the world seed this context relies on, which is used to create {@link IRegion} factories.
     *
     * @return The world seed.
     */
    long worldSeed();

    /**
     * Creates a {@linkplain IRegionRNG fast random number generator} instance for the specified seed.
     *
     * @param seed The seed for this instance.
     * @return The created {@link IRegionRNG} instance.
     */
    IRegionRNG getRNG( long seed );

    /**
     * Creates a {@link IRegionBuilder} by extending the specified {@link IRegionFactory}.
     *
     * @param factory The factory to extend.
     * @param seed    The seed to use.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #extend(IRegionFactory)
     */
    IRegionBuilder<R, ?> extend( IRegionFactory<R> factory, long seed );

    /**
     * Creates a {@link IRegionBuilder} by extending the specified {@link IRegionFactory}. The seed is computed from the
     * world seed.
     *
     * @param factory The factory to extend.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #extend(IRegionFactory, long)
     */
    default IRegionBuilder<R, ?> extend( IRegionFactory<R> factory ) {
        return extend( factory, worldSeed() & 0xFFFF );
    }

    /**
     * Creates a {@link IRegionBuilder} by creating a {@link IRegionFactory} from the specified {@link
     * IGeneratorLayer}.
     *
     * @param layer The layer to create factory from.
     * @param seed  The seed to use.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #generate(IGeneratorLayer)
     */
    default IRegionBuilder<R, ?> generate( IGeneratorLayer layer, long seed ) {
        return extend( layer.factory( this, seed ), seed );
    }

    /**
     * Creates a {@link IRegionBuilder} by creating a {@link IRegionFactory} from the specified {@link IGeneratorLayer}.
     * The seed is computed from the world seed.
     *
     * @param layer The layer to create factory from.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #generate(IGeneratorLayer, long)
     */
    default IRegionBuilder<R, ?> generate( IGeneratorLayer layer ) {
        return generate( layer, worldSeed() & 0xFFFF );
    }

    /**
     * Creates a {@link IRegionBuilder} by transforming a {@link IRegionFactory} using the specified {@link
     * ITransformerLayer}.
     *
     * @param layer   The layer used to transform the specified factory.
     * @param factory The factory to transform.
     * @param seed    The seed to use.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #transform(ITransformerLayer, IRegionFactory)
     */
    default IRegionBuilder<R, ?> transform( ITransformerLayer layer, IRegionFactory<R> factory, long seed ) {
        return extend( layer.factory( this, seed, factory ), seed );
    }

    /**
     * Creates a {@link IRegionBuilder} by transforming a {@link IRegionFactory} using the specified {@link
     * ITransformerLayer}. The seed is computed from the world seed.
     *
     * @param layer   The layer used to transform the specified factory.
     * @param factory The factory to transform.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #transform(ITransformerLayer, IRegionFactory, long)
     */
    default IRegionBuilder<R, ?> transform( ITransformerLayer layer, IRegionFactory<R> factory ) {
        return transform( layer, factory, worldSeed() & 0xFFFF );
    }

    /**
     * Creates a {@link IRegionBuilder} by merging two {@link IRegionFactory}s using the specified {@link
     * IMergerLayer}.
     *
     * @param layer    The layer used to merge the specified factories.
     * @param factoryA The first factory to transform.
     * @param factoryB The second factory to transform.
     * @param seed     The seed to use.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #merge(IMergerLayer, IRegionFactory, IRegionFactory)
     */
    default IRegionBuilder<R, ?> merge( IMergerLayer layer, IRegionFactory<R> factoryA, IRegionFactory<R> factoryB, long seed ) {
        return extend( layer.factory( this, seed, factoryA, factoryB ), seed );
    }

    /**
     * Creates a {@link IRegionBuilder} by merging two {@link IRegionFactory}s using the specified {@link IMergerLayer}.
     * The seed is computed from the world seed.
     *
     * @param layer    The layer used to merge the specified factories.
     * @param factoryA The first factory to transform.
     * @param factoryB The second factory to transform.
     * @return The created {@link IRegionBuilder}.
     *
     * @see #merge(IMergerLayer, IRegionFactory, IRegionFactory, long)
     */
    default IRegionBuilder<R, ?> merge( IMergerLayer layer, IRegionFactory<R> factoryA, IRegionFactory<R> factoryB ) {
        return merge( layer, factoryA, factoryB, worldSeed() & 0xFFFF );
    }

    default IRegionBuilder<R, ?> random( RandomLayer.RandomFunction func, long seed ) {
        return generate( new RandomLayer( func ), seed );
    }

    default IRegionBuilder<R, ?> random( RandomLayer.RandomFunction func ) {
        return generate( new RandomLayer( func ) );
    }

    default IRegionBuilder<R, ?> random( int min, int max, long seed ) {
        return generate( new RandomLayer( min, max ), seed );
    }

    default IRegionBuilder<R, ?> random( int min, int max ) {
        return generate( new RandomLayer( min, max ) );
    }

    default IRegionBuilder<R, ?> binary( double oneChance, long seed ) {
        return generate( new RandomLayer( oneChance ), seed );
    }

    default IRegionBuilder<R, ?> binary( double oneChance ) {
        return generate( new RandomLayer( oneChance ) );
    }

    default IRegionBuilder<R, ?> pick( int[] ints, long seed ) {
        return generate( new RandomLayer( ints ), seed );
    }

    default IRegionBuilder<R, ?> pick( int[] ints ) {
        return generate( new RandomLayer( ints ) );
    }
}
