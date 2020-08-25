/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import modernity.api.util.math.MathUtil;

// TODO Re-evaluate
public final class LightUtil {
    private LightUtil() {
    }

    public static float lightBrightnessFunction(float l, float factor, float min, float max) {
        float inv = 1 - l;
        return l / (inv * factor + 1) * (max - min) + min;
    }

    public static void genLightBrightnessTable(float[] lbt, float factor, float min, float max) {
        for(int i = 0; i < lbt.length; i++) {
            lbt[i] = lightBrightnessFunction(i / (float) lbt.length, factor, min, max);
        }
    }

    public static void updateLightColors(Vector3f colors, float partialTicks, float sunBrightness, float skyLight, float blockLight) {
        World world = Minecraft.getInstance().world;
//        if( world.dimension instanceof IEnvironmentDimension ) { // TODO
//            ( (IEnvironmentDimension) world.dimension ).updateLight( EnvironmentRenderingManager.LIGHT );
//        }

        float[] ambient = EnvironmentRenderingManager.LIGHT.ambient;
        float[] block = EnvironmentRenderingManager.LIGHT.block;
        float[] sky = EnvironmentRenderingManager.LIGHT.sky;

        float nvb = 0;
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isPotionActive(Effects.NIGHT_VISION)) {
            nvb = GameRenderer.getNightVisionBrightness(Minecraft.getInstance().player, partialTicks);
        }

        if(nvb > 0) {
            ambient[0] = MathUtil.lerp(ambient[0], ambient[0] * 0.2F + 0.8F, nvb);
            ambient[1] = MathUtil.lerp(ambient[1], ambient[1] * 0.2F + 0.8F, nvb);
            ambient[2] = MathUtil.lerp(ambient[2], ambient[2] * 0.2F + 0.8F, nvb);
        }

        colors.setX(interpolate(sky[0], block[0], ambient[0], skyLight, blockLight));
        colors.setY(interpolate(sky[1], block[1], ambient[1], skyLight, blockLight));
        colors.setZ(interpolate(sky[2], block[2], ambient[2], skyLight, blockLight));
    }

    private static float interpolate(float sky, float block, float ambient, float skyLight, float blockLight) {
        return MathUtil.lerp(MathUtil.lerp(ambient, block, blockLight), sky, skyLight);
    }
}
