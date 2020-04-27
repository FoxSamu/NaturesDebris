/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.precipitation;

import modernity.client.render.environment.SurfaceWeatherRenderer;
import modernity.common.block.MDNatureBlocks;
import modernity.common.block.misc.PuddleBlock;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ShowersPrecipitation implements IPrecipitation {
    @OnlyIn( Dist.CLIENT )
    @Override
    public ResourceLocation getTexture() {
        return SurfaceWeatherRenderer.SHOWERS_TEXTURES;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasParticles( Random rand ) {
        return rand.nextBoolean();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean hasSound( Random rand ) {
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public IParticleData getParticleType( Random rand ) {
        return MDParticleTypes.RAIN;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void playSound( double x, double y, double z, boolean above, World world, float strength ) {
        if( above ) {
            world.playSound( x, y, z, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.04F * strength, 0.5F, false );
        } else {
            world.playSound( x, y, z, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.08F * strength, 1, false );
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
        return 0; // TODO Colors
//        return ModernityClient.get().getWaterColors().getColor( world, new BlockPos( x, 255, z ) );
    }

    @Override
    public void blockUpdate( World world, BlockPos pos ) {
        if( world.rand.nextInt( 4 ) != 0 ) return;
        int height = getHeight( world, pos.getX(), pos.getZ() );

        if( pos.getY() == height && doesPuddleGenerate( world, pos ) ) {
            BlockState state = world.getBlockState( pos );
            if( state.isAir( world, pos ) ) {
                world.setBlockState( pos, MDNatureBlocks.PUDDLE.getDefaultState().with( PuddleBlock.DISTANCE, 0 ), 7 );
            } else {
                MDNatureBlocks.PUDDLE.rainTick( world, pos, state, 0.25 );
            }
        }
    }

    private boolean doesPuddleGenerate( World world, BlockPos pos ) {
        if( pos.getY() >= 0 && pos.getY() < 256 ) {
            BlockState state = world.getBlockState( pos );
            return ( state.isAir( world, pos ) || state.getBlock() == MDNatureBlocks.PUDDLE ) && MDNatureBlocks.PUDDLE.getDefaultState().isValidPosition( world, pos );
        }

        return false;
    }

    @Override
    public Biome.RainType type() {
        return Biome.RainType.RAIN;
    }
}
