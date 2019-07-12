/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.util.ColorUtil;
import modernity.common.biome.BiomeBase;

@OnlyIn( Dist.CLIENT )
public class BiomeValues {
    private static final int CACHE_SIZE = 32;

    public static final ColorResolver WATER_COLOR = BiomeBase::getMDWaterColor;
    public static final ColorResolver FOG_COLOR = BiomeBase::getFogColor;
    public static final ColorResolver GRASS_COLOR = BiomeBase::getGrassColor;
    public static final ColorResolver FOLIAGE_COLOR = BiomeBase::getFoliageColor;

    public static final ValueResolver FOG_DENSITY = BiomeBase::getFogDensity;
    public static final ValueResolver WATER_FOG_DENSITY = BiomeBase::getWaterFogDensity;

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

                if( biome instanceof BiomeBase ) {
                    int col = resolver.getColor( (BiomeBase) biome, rpos );

                    r += col >>> 16 & 0xff;
                    g += col >>> 8 & 0xff;
                    b += col & 0xff;

                    tot++;
                }
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

                if( biome instanceof BiomeBase ) {
                    float val = resolver.getValue( (BiomeBase) biome );

                    value += val;

                    tot++;
                }
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
        int getColor( BiomeBase biome, BlockPos pos );
    }

    @OnlyIn( Dist.CLIENT )
    private interface ValueResolver {
        float getValue( BiomeBase biome );
    }
}
