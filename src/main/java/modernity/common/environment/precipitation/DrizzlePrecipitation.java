/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.precipitation;

import modernity.client.render.environment.SurfaceWeatherRenderer;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class DrizzlePrecipitation implements IPrecipitation {
    @OnlyIn( Dist.CLIENT )
    @Override
    public ResourceLocation getTexture() {
        return SurfaceWeatherRenderer.DRIZZLE_TEXTURES;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasParticles( Random rand ) {
        return false;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasSound( Random rand ) {
        return false;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public IParticleData getParticleType( Random rand ) {
        return MDParticleTypes.RAIN;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void playSound( double x, double y, double z, boolean above, World world, float strength ) {

    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public boolean shouldRender() {
        return true;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void computeUVOffset( float[] uv, int tick, float partialTicks, Random rand, int x, int z ) {
        float renderTick = tick + partialTicks;
        double yOffset = - ( ( tick & 511 ) + partialTicks ) / 512;
        double uOffset = rand.nextDouble() + renderTick * 0.01 * rand.nextGaussian();

        uv[ 0 ] = (float) uOffset;
        uv[ 1 ] = (float) yOffset;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public int getColor( World world, int x, int z ) {
        return 0x8d9c83;
    }

    @Override
    public void blockUpdate( World world, BlockPos pos ) {

    }

    @Override
    public int getHeight( World world, int x, int z ) {
        return world.getHeight( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z );
    }
}
