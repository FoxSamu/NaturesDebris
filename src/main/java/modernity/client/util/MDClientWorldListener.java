/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.util;

import modernity.common.util.MDEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
public class MDClientWorldListener implements IWorldEventListener {

    private final World world;

    public MDClientWorldListener( World world ) {
        this.world = world;
    }


    @Override
    public void notifyBlockUpdate( IBlockReader worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags ) {

    }

    @Override
    public void notifyLightSet( BlockPos pos ) {

    }

    @Override
    public void markBlockRangeForRenderUpdate( int x1, int y1, int z1, int x2, int y2, int z2 ) {

    }

    @Override
    public void playSoundToAllNearExcept( @Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch ) {

    }

    @Override
    public void playRecord( SoundEvent soundIn, BlockPos pos ) {

    }

    @Override
    public void addParticle( IParticleData particleData, boolean alwaysRender, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed ) {

    }

    @Override
    public void addParticle( IParticleData particleData, boolean ignoreRange, boolean minimizeLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed ) {

    }

    @Override
    public void onEntityAdded( Entity entityIn ) {

    }

    @Override
    public void onEntityRemoved( Entity entityIn ) {

    }

    @Override
    public void broadcastSound( int soundID, BlockPos pos, int data ) {

    }

    @Override
    public void playEvent( EntityPlayer player, int type, BlockPos pos, int data ) {
        MDEvents.playEvent( player, world, type, pos, data );
    }

    @Override
    public void sendBlockBreakProgress( int breakerId, BlockPos pos, int progress ) {

    }
}
