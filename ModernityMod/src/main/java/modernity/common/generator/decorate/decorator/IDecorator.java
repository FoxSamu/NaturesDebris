/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.decorator;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

@FunctionalInterface
public interface IDecorator {
    void decorate( IWorld world, int cx, int cz, Biome biome, Random rand, ChunkGenerator<?> chunkGenerator );
}
