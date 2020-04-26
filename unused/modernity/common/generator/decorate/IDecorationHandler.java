/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate;

import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;

@FunctionalInterface
public interface IDecorationHandler {
    void decorate( WorldGenRegion region, ChunkGenerator<?> chunkGen );
}
