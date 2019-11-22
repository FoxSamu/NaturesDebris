/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 22 - 2019
 * Author: rgsw
 */

package modernity.client.environment;

import modernity.api.dimension.IEnvironmentDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.rgsw.MathUtil;

public final class LightUtil {
    private LightUtil() {
    }

    public static float lightBrightnessFunction( float l, float factor, float min, float max ) {
        float inv = 1 - l;
        return l / ( inv * factor + 1 ) * ( max - min ) + min;
    }

    public static void genLightBrightnessTable( float[] lbt, float factor, float min, float max ) {
        for( int i = 0; i < lbt.length; i++ ) {
            lbt[ i ] = lightBrightnessFunction( i / (float) lbt.length, factor, min, max );
        }
    }

    public static void updateLightColors( float[] colors, float partialTicks, float sunBrightness, float skyLight, float blockLight ) {
        World world = Minecraft.getInstance().world;
        if( world.dimension instanceof IEnvironmentDimension ) {
            ( (IEnvironmentDimension) world.dimension ).updateLight( EnvironmentRenderingManager.LIGHT );
        }

        float[] ambient = EnvironmentRenderingManager.LIGHT.ambient;
        float[] block = EnvironmentRenderingManager.LIGHT.block;
        float[] sky = EnvironmentRenderingManager.LIGHT.sky;

        colors[ 0 ] = interpolate( sky[ 0 ], block[ 0 ], ambient[ 0 ], skyLight, blockLight );
        colors[ 1 ] = interpolate( sky[ 1 ], block[ 1 ], ambient[ 1 ], skyLight, blockLight );
        colors[ 2 ] = interpolate( sky[ 2 ], block[ 2 ], ambient[ 2 ], skyLight, blockLight );
    }

    private static float interpolate( float sky, float block, float ambient, float skyLight, float blockLight ) {
        return MathUtil.lerp( MathUtil.lerp( ambient, block, blockLight ), sky, skyLight );
    }
}
