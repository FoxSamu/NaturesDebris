/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate;

import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;

@FunctionalInterface
public interface IDecorationHandler {
    void decorate( WorldGenRegion region, ChunkGenerator<?> chunkGen );
}
