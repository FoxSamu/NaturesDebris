/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.biome.IColoringBiome;
import modernity.api.util.ColorUtil;

@OnlyIn( Dist.CLIENT )
public class BiomeValues {
    public static final ColorResolver WATER_COLOR = new ColorResolver() {
        @Override
        public int getColor( IColoringBiome iColoringBiome, BlockPos pos ) {
            return iColoringBiome.getMDWaterColor( pos );
        }

        @Override
        public int getDefaultColor( Biome biome, BlockPos pos ) {
            return ColorUtil.darken( biome.getWaterColor(), 0.6 );
        }
    };

    public static final ColorResolver FOG_COLOR = IColoringBiome::getFogColor;

    public static final ColorResolver GRASS_COLOR = new ColorResolver() {
        @Override
        public int getColor( IColoringBiome iColoringBiome, BlockPos pos ) {
            return iColoringBiome.getGrassColor( pos );
        }

        @Override
        public int getDefaultColor( Biome biome, BlockPos pos ) {
            return ColorUtil.darken( biome.getGrassColor( pos ), 0.6 );
        }
    };

    public static final ColorResolver FOLIAGE_COLOR = new ColorResolver() {
        @Override
        public int getColor( IColoringBiome iColoringBiome, BlockPos pos ) {
            return iColoringBiome.getFoliageColor( pos );
        }

        @Override
        public int getDefaultColor( Biome biome, BlockPos pos ) {
            return ColorUtil.darken( biome.getFoliageColor( pos ), 0.6 );
        }
    };

    public static final ValueResolver FOG_DENSITY = IColoringBiome::getFogDensity;

    public static final ValueResolver WATER_FOG_DENSITY = new ValueResolver() {
        @Override
        public float getValue( IColoringBiome iColoringBiome ) {
            return iColoringBiome.getWaterFogDensity();
        }

        @Override
        public float getDefaultValue( Biome biome ) {
            return 0.01F;
        }
    };

    private static int getColor( IWorldReaderBase world, BlockPos pos, ColorResolver resolver, int radius ) {
        BlockPos.MutableBlockPos rpos = new BlockPos.MutableBlockPos();
        int tot = 0;

        int r = 0;
        int g = 0;
        int b = 0;

        for( int x = - radius; x <= radius; x++ ) {
            for( int z = - radius; z <= radius; z++ ) {
                rpos.setPos( pos.getX() + x, pos.getY(), pos.getZ() + z );

                Biome biome = world.getBiome( rpos );

                int col;
                if( biome instanceof IColoringBiome ) {
                    col = resolver.getColor( (IColoringBiome) biome, rpos );
                } else {
                    col = resolver.getDefaultColor( biome, pos );
                }

                r += col >>> 16 & 0xff;
                g += col >>> 8 & 0xff;
                b += col & 0xff;

                tot++;
            }
        }

        if( tot == 0 ) tot = 1;
        r /= tot;
        g /= tot;
        b /= tot;

        return ColorUtil.rgb( r, g, b );
    }

    public static float get( IWorldReaderBase world, BlockPos pos, ValueResolver resolver ) {
        return getValue( world, pos, resolver, Minecraft.getInstance().gameSettings.biomeBlendRadius );
    }

    private static float getValue( IWorldReaderBase world, BlockPos pos, ValueResolver resolver, int radius ) {
        BlockPos.MutableBlockPos rpos = new BlockPos.MutableBlockPos();
        int tot = 0;

        float value = 0;

        for( int x = - radius; x <= radius; x++ ) {
            for( int z = - radius; z <= radius; z++ ) {
                rpos.setPos( pos.getX() + x, pos.getY(), pos.getZ() + z );

                Biome biome = world.getBiome( rpos );

                float val;
                if( biome instanceof IColoringBiome ) {
                    val = resolver.getValue( (IColoringBiome) biome );
                } else {
                    val = resolver.getDefaultValue( biome );
                }

                value += val;

                tot++;
            }
        }

        if( tot == 0 ) tot = 1;
        value /= tot;

        return value;
    }

    public static int get( IWorldReaderBase world, BlockPos pos, ColorResolver resolver ) {
        return getColor( world, pos, resolver, Minecraft.getInstance().gameSettings.biomeBlendRadius );
    }

    @OnlyIn( Dist.CLIENT )
    private interface ColorResolver {
        int getColor( IColoringBiome biome, BlockPos pos );
        default int getDefaultColor( Biome biome, BlockPos pos ) {
            return 0x000000;
        }
    }

    @OnlyIn( Dist.CLIENT )
    private interface ValueResolver {
        float getValue( IColoringBiome biome );
        default float getDefaultValue( Biome biome ) {
            return 0;
        }
    }
}
