/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.util;

/**
 * Holds the generation metrics of a biome, such as the height and roughness.
 */
public class BiomeMetrics {
    /**
     * The depth of the biome. This is the upwards offset from the main surface height (water level) and defines the
     * base height of the biome.
     */
    public final double depth;

    /**
     * The density interpolation scale of the biome. This is the offset from the height of this biome (mix of {@link
     * #depth} and {@link #variation}) to the density interpolation limits and defines the roughness and complexity of
     * the terrain in this biome.
     */
    public final double scale;

    /**
     * The scale of the height variation noise, which is added to the base height before interpolating. This defines the
     * height separation between higher, rougher areas and lower, flatter areas.
     */
    public final double variation;

    /**
     * The blending weight multiplier of the biome. When blending this biome's metrics, the blending weight is
     * multiplied by this value to make this biome blend in stronger or weaker.
     */
    public final double blendWeight;

    /**
     * Creates a biome metrics instance.
     *
     * @param depth       The depth value (main height offset)
     * @param scale       The scale value (interpolation offset)
     * @param variation   The variation value (height separation)
     * @param blendWeight The blending weight multiplier
     */
    public BiomeMetrics( double depth, double scale, double variation, double blendWeight ) {
        this.depth = depth;
        this.scale = scale;
        this.variation = variation;
        this.blendWeight = blendWeight;
    }
}
