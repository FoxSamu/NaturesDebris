/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 26 - 2020
 * Author: rgsw
 */

package modernity.client.environment.particles;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IEnvironmentParticleEffect {
    void addParticleEffect( World world, BlockPos pos, Random rand );
}
