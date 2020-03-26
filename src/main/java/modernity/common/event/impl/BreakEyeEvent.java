/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.event.impl;

import modernity.common.event.SimpleBlockEvent;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BreakEyeEvent extends SimpleBlockEvent {
    @Override
    public void playEvent( World world, BlockPos pos, Void data ) {
        world.playSound(
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
            SoundEvents.BLOCK_FIRE_EXTINGUISH,
            SoundCategory.BLOCKS,
            1, 1, true
        );

        for( int i = 0; i < 32; ++ i ) {
            double x = pos.getX() + ( 5 + world.rand.nextDouble() * 6 ) / 16;
            double y = pos.getY() + 0.625;
            double z = pos.getZ() + ( 5 + world.rand.nextDouble() * 6 ) / 16;
            world.addParticle( ParticleTypes.POOF, x, y, z, 0, 0, 0 );
        }
    }
}
