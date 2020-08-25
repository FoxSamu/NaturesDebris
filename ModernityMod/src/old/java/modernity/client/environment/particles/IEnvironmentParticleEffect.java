/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.environment.particles;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IEnvironmentParticleEffect {
    void addParticleEffect(World world, BlockPos pos, Random rand);
}
