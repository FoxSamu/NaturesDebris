/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 12 - 2020
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
        if( world.rand.nextInt( 3 ) != 0 ) return;
        int height = getHeight( world, pos.getX(), pos.getZ() );

        if( doesPuddleGenerate( world, pos ) ) {
            BlockState state = world.getBlockState( pos );
            if( state.isAir( world, pos ) && pos.getY() == height ) {
                world.setBlockState( pos, MDBlocks.PUDDLE.getDefaultState().with( PuddleBlock.DISTANCE, 0 ), 7 );
            } else {
                MDBlocks.PUDDLE.rainTick( world, pos, state, 0.25 );
            }
        }
    }

    private boolean doesPuddleGenerate( World world, BlockPos pos ) {
        if( ! world.isAreaLoaded( pos, 1 ) ) return false;
        if( pos.getY() >= 0 && pos.getY() < 256 ) {
            BlockState state = world.getBlockState( pos );
            return ( state.isAir( world, pos ) || state.getBlock() == MDBlocks.PUDDLE ) && MDBlocks.PUDDLE.getDefaultState().isValidPosition( world, pos );
        }

        return false;
    }

    @Override
    public int getHeight( World world, int x, int z ) {
        return world.getHeight( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z );
    }
}
