package modernity.common.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.rgsw.noise.FractalPerlin3D;

import modernity.api.util.ColorUtil;

import java.util.Random;

public class BiomeBase extends Biome {
    protected static final FractalPerlin3D GRASS_COLOR_VARIATION = new FractalPerlin3D( 58291250, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D FOLIAGE_COLOR_VARIATION = new FractalPerlin3D( 81725122, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D WATER_COLOR_VARIATION = new FractalPerlin3D( 21516663, 9.16292137, 9.16292137, 9.16292137, 2 );
    protected static final FractalPerlin3D FOG_COLOR_VARIATION = new FractalPerlin3D( 62278189, 26.29517772, 26.29517772, 26.29517772, 2 );


    protected final float baseHeight;  // Main height of the biome
    protected final float heightDiff;  // Difference between minimum height and maximum height
    protected final float heightVar;   // Factor of noise added to the main height
    protected final float waterFogDensity;
    protected final float fogDensity;
    protected final int waterColor;
    protected final int grassColor;
    protected final int foliageColor;
    protected final int fogColor;

    protected BiomeBase( String id, Builder builder ) {
        super( builder );
        setRegistryName( "modernity:" + id );
        if( builder.heightBase != null && builder.heightVar != null && builder.waterFogDensity != null && builder.fogDensity != null && builder.waterColor != null && builder.grassColor != null && builder.foliageColor != null && builder.fogColor != null ) {
            this.waterColor = builder.waterColor;
            this.grassColor = builder.grassColor;
            this.foliageColor = builder.foliageColor;
            this.fogColor = builder.fogColor;

            this.baseHeight = builder.heightBase;
            this.heightVar = builder.heightVar;
            this.heightDiff = builder.heightDiff;
            this.waterFogDensity = builder.waterFogDensity;
            this.fogDensity = builder.fogDensity;
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

    public float getWaterFogDensity() {
        return waterFogDensity;
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

    public static class Builder extends BiomeBuilder {
        private Float heightBase;
        private Float heightVar;
        private Float heightDiff;
        private Integer grassColor;
        private Integer foliageColor;
        private Integer waterColor;
        private Float waterFogDensity;
        private Float fogDensity;
        private Integer fogColor;

        @SuppressWarnings( "ConstantConditions" )
        public Builder() {
            surfaceBuilder( new CompositeSurfaceBuilder<>( new NoSurfaceBuilder(), null ) );
            precipitation( RainType.NONE );
            category( Category.NONE );
            depth( 0 );
            scale( 0 );
            temperature( 0 );
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

        @Override
        public String toString() {
            String old = super.toString();
            old = old.substring( 0, old.length() - 2 );

            old += ",\nbaseHeigt=" + heightBase;
            old += ",\nheightVariation=" + heightVar;
            old += ",\nfogDensity=" + fogDensity;
            old += ",\nwaterFogDensity=" + waterFogDensity;
            old += ",\ngrassColor=#" + Integer.toString( grassColor, 16 );
            old += ",\nfoliageColor=#" + Integer.toString( foliageColor, 16 );
            old += ",\nwaterColor=#" + Integer.toString( waterColor, 16 );
            old += ",\nfogColor=#" + Integer.toString( fogColor, 16 );
            old += "\n}";
            return old;
        }
    }

    static class NoSurfaceBuilder implements ISurfaceBuilder<SurfaceBuilderConfig> {
        @Override
        public void buildSurface( Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, IBlockState defaultBlock, IBlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config ) {}
    }
}
