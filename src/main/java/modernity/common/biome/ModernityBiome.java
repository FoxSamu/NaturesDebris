/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.blocks.IBlockGenerator;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.decorator.IDecorator;
import modernity.common.generator.surface.ISurfaceGenerator;
import modernity.common.generator.util.BiomeMetrics;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.ArrayList;

/**
 * Base of all Modernity biomes. It holds additional values for generating the the Modernity.
 */
public abstract class ModernityBiome extends Biome {
    private final float waterFogDensity;
    private final ISurfaceGenerator surfaceGen;
    private final IPrecipitationFunction precipitationFunction;

    private final BiomeMetrics metrics;

    private final ArrayList<IDecorator> decorators = new ArrayList<>();

    private IBlockGenerator growingPlants = MDBlockGenerators.MURK_GRASS_1;

    protected ModernityBiome( Builder builder ) {
        super( builder.vanilla() );
        metrics = builder.buildMetrics();
        waterFogDensity = builder.waterFogDensity;
        surfaceGen = builder.surfaceGen;
        precipitationFunction = builder.precipitationFunction;
        if( precipitationFunction == null )
            throw new NullPointerException( "Null precipitation function" );
    }

    /**
     * Returns the {@link BiomeMetrics} of this biome.
     */
    public BiomeMetrics metrics() {
        return metrics;
    }

    /**
     * Returns the additional fog density for water.
     */
    public float getWaterFogDensity() {
        return waterFogDensity;
    }

    /**
     * Returns the {@link IPrecipitationFunction} that determines the precipitation in this biome.
     */
    public IPrecipitationFunction getPrecipitationFunction() {
        return precipitationFunction;
    }

    /**
     * Returns the surface generator of this biome
     */
    public ISurfaceGenerator getSurfaceGen() {
        return surfaceGen;
    }

    public void addDecorator( IDecorator decorator ) {
        decorators.add( decorator );
    }

    public void removeDecorator( IDecorator decorator ) {
        decorators.remove( decorator );
    }


    @Override
    public void decorate( GenerationStage.Decoration stage, ChunkGenerator<?> chunkGenerator, IWorld world, long seed, SharedSeedRandom rand, BlockPos pos ) {
        if( stage == GenerationStage.Decoration.RAW_GENERATION ) {
            for( IDecorator decorator : decorators ) {
                decorator.decorate( world, pos.getX() >> 4, pos.getZ() >> 4, this, rand, chunkGenerator );
            }
        }
    }

    public IBlockGenerator getRandomPlant( BlockState soil ) {
        return growingPlants;
    }

    protected void setGrowingPlants( IBlockGenerator growingPlants ) {
        this.growingPlants = growingPlants;
    }

    public static ModernityBiome get( Biome biome, ModernityBiome def ) {
        if( biome instanceof ModernityBiome ) {
            return (ModernityBiome) biome;
        }
        return def;
    }

    public static ModernityBiome get( Biome biome ) {
        return get( biome, MDBiomes.DEFAULT );
    }

    /**
     * Builder for Modernity biomes, used instead of vanilla biome builder.
     */
    public static class Builder {
        private final Biome.Builder vanillaBuilder = new Biome.Builder();

        private float depth;
        private float variation;
        private float scale;
        private float blendWeight = 1;
        private float waterFogDensity;

        private IPrecipitationFunction precipitationFunction;

        private ISurfaceGenerator surfaceGen;

        public Builder() {
            vanillaBuilder.category( Category.NONE );
            vanillaBuilder.temperature( 0.02F );
            vanillaBuilder.waterColor( 0x4E76C2 );
            vanillaBuilder.downfall( 0 );
            vanillaBuilder.precipitation( RainType.NONE );
            vanillaBuilder.waterFogColor( 0x1A2A5C );
        }

        public Builder depth( float value ) {
            this.depth = value;
            return this;
        }

        public Builder variation( float value ) {
            this.variation = value;
            return this;
        }

        public Builder scale( float value ) {
            this.scale = value;
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

        public Builder surfaceGen( ISurfaceGenerator value ) {
            this.surfaceGen = value;
            return this;
        }

        public Builder precipitation( IPrecipitationFunction value ) {
            this.precipitationFunction = value;
            return this;
        }

        private BiomeMetrics buildMetrics() {
            return new BiomeMetrics( depth, scale, variation, blendWeight );
        }

        private Biome.Builder vanilla() {
            vanillaBuilder.depth( depth / 8 );
            vanillaBuilder.scale( variation / 8 );
            vanillaBuilder.surfaceBuilder( SurfaceBuilder.NOPE, surfaceGen.createBuilderConfig() );
            return vanillaBuilder;
        }
    }
}
