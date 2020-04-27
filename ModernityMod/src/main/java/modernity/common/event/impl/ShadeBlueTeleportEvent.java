/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.event.impl;

import modernity.common.event.SimpleBlockEvent;
import modernity.common.particle.MDParticleTypes;
import modernity.common.sound.MDSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShadeBlueTeleportEvent extends SimpleBlockEvent {
    @Override
    @OnlyIn( Dist.CLIENT )
    public void playEvent( World world, BlockPos pos, Void data ) {
        for( int i = 0; i < 20; i++ ) {
            world.addParticle(
                MDParticleTypes.SHADE,
                pos.getX() + world.rand.nextDouble(),
                pos.getY() + world.rand.nextDouble(),
                pos.getZ() + world.rand.nextDouble(),
                ( world.rand.nextDouble() - world.rand.nextDouble() ) * 0.005,
                ( world.rand.nextDouble() - world.rand.nextDouble() ) * 0.005,
                ( world.rand.nextDouble() - world.rand.nextDouble() ) * 0.005
            );
        }

        for( int i = 0; i < 60; i++ ) {
            double rad = Math.toRadians( i * 6 );
            double sin = Math.sin( rad ) * 0.05 + world.rand.nextDouble() * 0.002;
            double cos = Math.cos( rad ) * 0.05 + world.rand.nextDouble() * 0.002;

            world.addParticle(
                MDParticleTypes.SHADE,
                pos.getX() + 0.5 + sin * 24,
                pos.getY() + 0.45 + world.rand.nextDouble() * 0.1,
                pos.getZ() + 0.5 + cos * 24,
                sin, 0, cos
            );
        }

        world.playSound( Minecraft.getInstance().player, pos, MDSoundEvents.BLOCK_SHADE_BLUE_TELEPORT, SoundCategory.BLOCKS, 1, world.rand.nextFloat() * 0.3F - 0.15F + 1 );
    }
}
