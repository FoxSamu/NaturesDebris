/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.world.gen.surface.ISurfaceGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

/**
 * Base of all Modernity biomes. It holds additional values for generating the the Modernity.
 */
public abstract class ModernityBiome extends Biome {
    private final float baseHeight;
    private final float heightVariation;
    private final float heightDifference;
    private final float blendWeight;
    private final float waterFogDensity;
    private final ISurfaceGenerator<?> surfaceGen;

    protected ModernityBiome( Builder builder ) {
        super( builder.vanilla() );
        baseHeight = builder.baseHeight;
        heightVariation = builder.heightVariation;
        heightDifference = builder.heightDifference;
        blendWeight = builder.blendWeight;
        waterFogDensity = builder.waterFogDensity;
        surfaceGen = builder.surfaceGen;
    }

    /**
     * Returns the base height, which is the upwards offset from water level.
     */
    public float getBaseHeight() {
        return baseHeight;
    }

    /**
     * Returns the noise interpolation range at base height to generate any hills.
     */
    public float getHeightVariation() {
        return heightVariation;
    }

    /**
     * Returns the range of any additional height differences.
     */
    public float getHeightDifference() {
        return heightDifference;
    }

    /**
     * Returns the weight for blending this biome with others.
     */
    public float getBlendWeight() {
        return blendWeight;
    }

    /**
     * Returns the additional fog density for water.
     */
    public float getWaterFogDensity() {
        return waterFogDensity;
    }

    /**
     * Returns the surface generator of this biome
     */
    @SuppressWarnings( "unchecked" )
    public <T extends GenerationSettings> ISurfaceGenerator<T> getSurfaceGen() {
        return (ISurfaceGenerator<T>) surfaceGen;
    }

    /**
     * Builder for Modernity biomes, used instead of vanilla biome builder.
     */
    public static class Builder {
        private final Biome.Builder vanillaBuilder = new Biome.Builder();

        private float baseHeight;
        private float heightVariation;
        private float heightDifference;
        private float blendWeight = 1;
        private float waterFogDensity;

        private ISurfaceGenerator<?> surfaceGen;

        public Builder() {
            vanillaBuilder.category( Category.NONE );
            vanillaBuilder.temperature( 0.02F );
            vanillaBuilder.waterColor( 0x4e76c2 );
            vanillaBuilder.downfall( 0 );
            vanillaBuilder.precipitation( RainType.NONE );
            vanillaBuilder.waterFogColor( 0x1a2a5c );
        }

        public Builder baseHeight( float value ) {
            this.baseHeight = value;
            return this;
        }

        public Builder heightVariation( float value ) {
            this.heightVariation = value;
            return this;
        }

        public Builder heightDifference( float value ) {
            this.heightDifference = value;
            return this;
        }

        public Builder blendWeight( float value ) {
            this.blendWeight = value;
            return this;
        }

        public Builder waterFogDensity( float value ) {
            this.waterFogDensity = value;
            return this;
        }

        public Builder surfaceGen( ISurfaceGenerator<?> value ) {
            this.surfaceGen = value;
            return this;
        }

        private Biome.Builder vanilla() {
            vanillaBuilder.depth( baseHeight / 8 );
            vanillaBuilder.scale( heightVariation / 8 );
            vanillaBuilder.surfaceBuilder( SurfaceBuilder.NOPE, new SurfaceBuilderConfig(
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
            ) );
            return vanillaBuilder;
        }
    }
}
