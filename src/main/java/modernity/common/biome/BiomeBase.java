/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.biome;

import modernity.api.biome.IColoringBiome;
import modernity.api.util.ColorUtil;
import modernity.api.util.EcoBlockPos;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.ModernityGenSettings;
import modernity.common.world.gen.surface.ISurfaceGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;

public class BiomeBase extends Biome implements IColoringBiome {
    protected static final FractalPerlin3D GRASS_COLOR_VARIATION = new FractalPerlin3D( 58291250, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D FOLIAGE_COLOR_VARIATION = new FractalPerlin3D( 81725122, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D WATER_COLOR_VARIATION = new FractalPerlin3D( 21516663, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D FOG_COLOR_VARIATION = new FractalPerlin3D( 62278189, 26.29517772, 26.29517772, 26.29517772, 2 );


    protected final float baseHeight;  // Main height of the biome
    protected final float heightDiff;  // Difference between minimum height and maximum height
    protected final float heightVar;   // Factor of noise added to the main height
    protected final float blendWeight; // Weight when blending biome heights
    protected final float waterFogDensity;
    protected final float fogDensity;
    protected final int waterColor;
    protected final int grassColor;
    protected final int foliageColor;
    protected final int fogColor;

    protected final ISurfaceGenerator surfaceGen;

    protected BiomeBase( String id, Builder builder ) {
        super( builder );
        setRegistryName( "modernity:" + id );
        if( builder.heightBase != null && builder.heightVar != null && builder.heightDiff != null && builder.waterFogDensity != null && builder.fogDensity != null && builder.waterColor != null && builder.grassColor != null && builder.foliageColor != null && builder.fogColor != null && builder.surfaceGen != null ) {
            this.waterColor = builder.waterColor;
            this.grassColor = builder.grassColor;
            this.foliageColor = builder.foliageColor;
            this.fogColor = builder.fogColor;

            this.baseHeight = builder.heightBase;
            this.heightVar = builder.heightVar;
            this.heightDiff = builder.heightDiff;
            this.blendWeight = builder.blendWeight;

            this.waterFogDensity = builder.waterFogDensity;
            this.fogDensity = builder.fogDensity;

            this.surfaceGen = builder.surfaceGen;
        } else {
            throw new IllegalStateException( "You are missing parameters to build a proper biome for " + this.getClass().getSimpleName() + "\n" + builder );
        }
    }

    public float getBaseHeight() {
        return baseHeight;
    }

    public float getHeightVar() {
        return heightVar;
    }

    public float getHeightDiff() {
        return heightDiff;
    }

    public float getBlendWeight() {
        return blendWeight;
    }

    public float getWaterFogDensity() {
        return waterFogDensity;
    }

    public ISurfaceGenerator getSurfaceGen() {
        return surfaceGen;
    }

    public int getMDWaterColor( BlockPos pos ) {
        return waterColor; // ColorUtil.darken( waterColor, WATER_COLOR_VARIATION.generate( pos.getX(), pos.getY(), pos.getZ() ) * 0.14 );
    }

    public int getGrassColor( BlockPos pos ) {
        return ColorUtil.darken( grassColor, GRASS_COLOR_VARIATION.generate( pos.getX(), pos.getY(), pos.getZ() ) * 0.14 );
    }

    public int getFoliageColor( BlockPos pos ) {
        return ColorUtil.darken( foliageColor, FOLIAGE_COLOR_VARIATION.generate( pos.getX(), pos.getY(), pos.getZ() ) * 0.14 );
    }

    public int getFogColor( BlockPos pos ) {
        return ColorUtil.darken( fogColor, FOG_COLOR_VARIATION.generate( pos.getX(), pos.getY(), pos.getZ() ) * 0.14 );
    }

    public float getFogDensity() {
        return fogDensity;
    }

    @Override
    public boolean doesSnowGenerate( IWorldReaderBase worldIn, BlockPos pos ) {
        return false;
    }

    @Override
    public RainType getPrecipitation() {
        return RainType.NONE;
    }

    public static class Builder extends BiomeBuilder {
        private Float heightBase;
        private Float heightVar;
        private Float heightDiff;
        private Float blendWeight = 1F;
        private Integer grassColor;
        private Integer foliageColor;
        private Integer waterColor;
        private Float waterFogDensity;
        private Float fogDensity;
        private Integer fogColor;
        private ISurfaceGenerator surfaceGen;

        @SuppressWarnings( "ConstantConditions" )
        public Builder() {
            surfaceBuilder( new CompositeSurfaceBuilder<>( new ModernitySurfaceBuilder(), new SurfaceBuilderConfig(
                    MDBlocks.DARK_GRASS.getDefaultState(),
                    MDBlocks.DARK_DIRT.getDefaultState(),
                    MDBlocks.DARK_DIRT.getDefaultState()
            ) ) );
            precipitation( RainType.NONE );
            category( Category.NONE );
            depth( 0 );
            scale( 0 );
            temperature( 0.8f );
            downfall( 0 );
            super.waterColor( 0x3F76E4 );
            waterFogColor( 0x050533 );
        }

        @Override
        public BiomeBase.Builder depth( float value ) {
            super.depth( value );
            return this;
        }

        @Override
        public BiomeBase.Builder scale( float value ) {
            super.scale( value );
            return this;
        }

        @Override
        public BiomeBase.Builder waterColor( int value ) {
            waterColor = value;
            return this;
        }

        public BiomeBase.Builder waterFogDensity( float value ) {
            waterFogDensity = value;
            return this;
        }

        public BiomeBase.Builder baseHeight( float value ) {
            heightBase = value;
            return this;
        }

        public BiomeBase.Builder heightVariation( float value ) {
            heightVar = value;
            return this;
        }

        public BiomeBase.Builder heightDifference( float value ) {
            heightDiff = value;
            return this;
        }

        public BiomeBase.Builder blendWeight( float value ) {
            blendWeight = value;
            return this;
        }

        public BiomeBase.Builder grassColor( int value ) {
            grassColor = value;
            return this;
        }

        public BiomeBase.Builder foliageColor( int value ) {
            foliageColor = value;
            return this;
        }

        public BiomeBase.Builder fogDensity( float value ) {
            fogDensity = value;
            return this;
        }

        public BiomeBase.Builder fogColor( int value ) {
            fogColor = value;
            return this;
        }

        public BiomeBase.Builder surfaceGenerator( ISurfaceGenerator value ) {
            surfaceGen = value;
            return this;
        }

        @Override
        public String toString() {
            String old = super.toString();
            old = old.substring( 0, old.length() - 2 );

            old += ",\nbaseHeigt=" + heightBase;
            old += ",\nheightVariation=" + heightVar;
            old += ",\nheightDifference=" + heightDiff;
            old += ",\nfogDensity=" + fogDensity;
            old += ",\nwaterFogDensity=" + waterFogDensity;
            old += ",\ngrassColor=#" + Integer.toString( grassColor, 16 );
            old += ",\nfoliageColor=#" + Integer.toString( foliageColor, 16 );
            old += ",\nwaterColor=#" + Integer.toString( waterColor, 16 );
            old += ",\nfogColor=#" + Integer.toString( fogColor, 16 );
            old += ",\nsurfaceGenerator=" + surfaceGen;
            old += "\n}";
            return old;
        }
    }

    static class ModernitySurfaceBuilder implements ISurfaceBuilder<SurfaceBuilderConfig> {
        @Override
        public void buildSurface( Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, IBlockState defaultBlock, IBlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config ) {
            MovingBlockPos mpos = new MovingBlockPos();
            for( int y = 0; y < 256; y++ ) {
                mpos.setPos( x, y, z );
                if( chunk.getBlockState( mpos ).isSolid() ) {
                    chunk.setBlockState( mpos, MDBlocks.ROCK.getDefaultState(), false );
                }
            }

            if( biome instanceof BiomeBase ) {
                EcoBlockPos rpos = EcoBlockPos.retain();
                ( (BiomeBase) biome ).getSurfaceGen().generateSurface( chunk, chunk.getPos().x, chunk.getPos().z, x, z, random, (BiomeBase) biome, new FractalOpenSimplex3D( (int) seed, 2.512, 4 ), rpos, new ModernityGenSettings( seaLevel ) );
                rpos.release();
            }
        }
    }
}
