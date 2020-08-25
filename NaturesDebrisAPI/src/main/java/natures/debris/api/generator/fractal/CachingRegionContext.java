/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.generator.fractal;

import natures.debris.api.generator.fractal.layer.IGeneratorLayer;
import natures.debris.api.generator.fractal.layer.IMergerLayer;
import natures.debris.api.generator.fractal.layer.ITransformerLayer;

/**
 * A {@link IRegionContext} implementation that uses {@link IRegion}s that cache their values: {@link CachingRegion}s.
 */
public class CachingRegionContext implements IRegionContext<CachingRegion> {
    private final int initCacheSize;
    private final int cacheSizeMul;
    private final int cacheLimit;
    private final long worldSeed;

    /**
     * Creates a {@link CachingRegionContext} with default configuration.
     *
     * @param initCacheSize The initial cache size used when creating a generator region.
     * @param worldSeed     The world seed.
     */
    public CachingRegionContext(int initCacheSize, long worldSeed) {
        this(initCacheSize, 4, 1024, worldSeed);
    }

    /**
     * Creates a {@link CachingRegionContext} with custom configuration.
     *
     * @param initCacheSize The initial cache size used when creating a generator region.
     * @param cacheSizeMul  The cache size multiplier used when creating a transformer or merger region.
     * @param cacheLimit    The cache size limit. Computed cache sizes can't be more than this value.
     * @param worldSeed     The world seed.
     */
    public CachingRegionContext(int initCacheSize, int cacheSizeMul, int cacheLimit, long worldSeed) {
        this.initCacheSize = Math.min(cacheLimit, initCacheSize);
        this.cacheSizeMul = cacheSizeMul;
        this.cacheLimit = cacheLimit;
        this.worldSeed = worldSeed;
    }

    /**
     * Returns the initial cache size for generator regions.
     */
    public int getInitialCacheSize() {
        return initCacheSize;
    }

    /**
     * Returns the cache size multiplier for transformer regions. The cache size of a transformer region is computed by
     * multiplying the cache size of the underlying region with this value.
     */
    public int getCacheSizeMultiplier() {
        return cacheSizeMul;
    }

    /**
     * Returns the maximum cache size. Computed cache sizes can't be more than this value.
     */
    public int getCacheSizeLimit() {
        return cacheLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegion create(IRegion generator) {
        return new CachingRegion(generator, initCacheSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegion create(IRegion generator, CachingRegion region) {
        return new CachingRegion(generator, computeCacheSize(region.getMaxCacheSize()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegion create(IRegion generator, CachingRegion regionA, CachingRegion regionB) {
        int max = Math.max(regionA.getMaxCacheSize(), regionB.getMaxCacheSize());
        return new CachingRegion(generator, computeCacheSize(max));
    }

    private int computeCacheSize(int size) {
        return Math.min(cacheLimit, size * cacheSizeMul);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long worldSeed() {
        return worldSeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRegionRNG getRNG(long seed) {
        return new RegionRNG(worldSeed, seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder extend(IRegionFactory<CachingRegion> factory, long seed) {
        return new CachingRegionBuilder(this, factory, seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder extend(IRegionFactory<CachingRegion> factory) {
        return extend(factory, worldSeed & 0xFFFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder generate(IGeneratorLayer layer, long seed) {
        return new CachingRegionBuilder(this, layer.factory(this, seed), seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder generate(IGeneratorLayer layer) {
        return generate(layer, worldSeed & 0xFFFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder transform(ITransformerLayer layer, IRegionFactory<CachingRegion> factory, long seed) {
        return new CachingRegionBuilder(this, layer.factory(this, seed, factory), seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder transform(ITransformerLayer layer, IRegionFactory<CachingRegion> factory) {
        return transform(layer, factory, worldSeed & 0xFFFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder merge(IMergerLayer layer, IRegionFactory<CachingRegion> factoryA, IRegionFactory<CachingRegion> factoryB, long seed) {
        return new CachingRegionBuilder(this, layer.factory(this, seed, factoryA, factoryB), seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachingRegionBuilder merge(IMergerLayer layer, IRegionFactory<CachingRegion> factoryA, IRegionFactory<CachingRegion> factoryB) {
        return merge(layer, factoryA, factoryB, worldSeed & 0xFFFF);
    }
}
