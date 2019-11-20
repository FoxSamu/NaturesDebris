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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class SnowPrecipitation implements IPrecipitation {
    @OnlyIn( Dist.CLIENT )
    @Override
    public ResourceLocation getTexture() {
        return SurfaceWeatherRenderer.SNOW_TEXTURES;
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
        double vOffset = rand.nextDouble() + renderTick * rand.nextGaussian() * 0.001;

        uv[ 0 ] = (float) uOffset;
        uv[ 1 ] = (float) ( yOffset + vOffset );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public int getColor( World world, int x, int z ) {
        return 0xffffff;
    }

    @Override
    public void blockUpdate( World world, BlockPos pos ) {
        if( world.rand.nextInt( 2 ) != 0 ) return;
        BlockPos heightPos = new BlockPos( pos.getX(), getHeight( world, pos.getX(), pos.getZ() ), pos.getZ() );

        if( doesSnowGenerate( world, heightPos ) ) {
            world.setBlockState( heightPos, Blocks.SNOW.getDefaultState() );
        }
    }

    private boolean doesSnowGenerate( World world, BlockPos pos ) {
        if( ! world.isAreaLoaded( pos, 1 ) ) return false;
        if( pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor( LightType.BLOCK, pos ) < 10 ) {
            BlockState state = world.getBlockState( pos );
            return state.isAir( world, pos ) && Blocks.SNOW.getDefaultState().isValidPosition( world, pos );
        }

        return false;
    }
}
