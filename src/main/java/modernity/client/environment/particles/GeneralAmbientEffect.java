/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 26 - 2020
 * Author: rgsw
 */

package modernity.client.environment.particles;

import modernity.common.particle.MDParticleTypes;
import modernity.common.particle.RgbParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class GeneralAmbientEffect implements IEnvironmentParticleEffect {
    @Override
    public void addParticleEffect( World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 500 ) == 0 ) {
            world.addParticle(
                new RgbParticleData( MDParticleTypes.AMBIENT, 0xffffff ),
                rand.nextDouble() + pos.getX(),
                rand.nextDouble() + pos.getY(),
                rand.nextDouble() + pos.getZ(),
                0, 0, 0
            );
        }
    }
}
