/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.precipitation;

import modernity.client.render.environment.SurfaceWeatherRenderer;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.PuddleBlock;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class HailPrecipitation implements IPrecipitation {
    @OnlyIn( Dist.CLIENT )
    @Override
    public ResourceLocation getTexture() {
        return SurfaceWeatherRenderer.HAIL_TEXTURES;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasParticles( Random rand ) {
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasSound( Random rand ) {
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public IParticleData getParticleType( Random rand ) {
        return MDParticleTypes.HAIL;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void playSound( double x, double y, double z, boolean above, World world, float strength ) {
        if( above ) {
            world.playSound( x, y, z, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.2F * strength, 0.5F, false );
        } else {
            world.playSound( x, y, z, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.25F * strength, 1, false );
        }
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public boolean shouldRender() {
        return true;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void computeUVOffset( float[] uv, int tick, float partialTicks, Random rand, int x, int z ) {
        double yOffset = - ( ( tick + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31 ) + partialTicks ) / 32 * ( 3 + rand.nextDouble() );
        uv[ 0 ] = 0;
        uv[ 1 ] = (float) yOffset;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public int getColor( World world, int x, int z ) {
        return 0xffffff;
    }

    @Override
    public int getHeight( World world, int x, int z ) {
        return world.getHeight( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z );
    }

    @Override
    public void blockUpdate( World world, BlockPos pos ) {
        BlockPos heightPos = new BlockPos( pos.getX(), getHeight( world, pos.getX(), pos.getZ() ), pos.getZ() );

        if( doesPuddleGenerate( world, heightPos ) ) {
            BlockState state = world.getBlockState( heightPos );
            if( state.isAir( world, heightPos ) ) {
                world.setBlockState( heightPos, MDBlocks.PUDDLE.getDefaultState().with( PuddleBlock.DISTANCE, 0 ), 7 );
            } else {
                MDBlocks.PUDDLE.rainTick( world, pos, state, 0.25 );
            }
        }
    }

    private boolean doesPuddleGenerate( World world, BlockPos pos ) {
        if( pos.getY() >= 0 && pos.getY() < 256 ) {
            BlockState state = world.getBlockState( pos );
            return ( state.isAir( world, pos ) || state.getBlock() == MDBlocks.PUDDLE ) && MDBlocks.PUDDLE.getDefaultState().isValidPosition( world, pos );
        }

        return false;
    }
}
