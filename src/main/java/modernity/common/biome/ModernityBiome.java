package modernity.common.biome;

import modernity.common.world.gen.surface.ISurfaceGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public abstract class ModernityBiome extends Biome {
    private final float baseHeight;
    private final float heightVariation;
    private final float heightDifference;
    private final float blendWeight;
    private final ISurfaceGenerator<?> surfaceGen;

    protected ModernityBiome( Builder builder ) {
        super( builder.vanilla() );
        baseHeight = builder.baseHeight;
        heightVariation = builder.heightVariation;
        heightDifference = builder.heightDifference;
        blendWeight = builder.blendWeight;
        surfaceGen = builder.surfaceGen;
    }

    public float getBaseHeight() {
        return baseHeight;
    }

    public float getHeightVariation() {
        return heightVariation;
    }

    public float getHeightDifference() {
        return heightDifference;
    }

    public float getBlendWeight() {
        return blendWeight;
    }

    @SuppressWarnings( "unchecked" )
    public <T extends GenerationSettings> ISurfaceGenerator<T> getSurfaceGen() {
        return (ISurfaceGenerator<T>) surfaceGen;
    }

    public static class Builder {
        private final Biome.Builder vanillaBuilder = new Biome.Builder();

        private float baseHeight;
        private float heightVariation;
        private float heightDifference;
        private float blendWeight = 1;

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
