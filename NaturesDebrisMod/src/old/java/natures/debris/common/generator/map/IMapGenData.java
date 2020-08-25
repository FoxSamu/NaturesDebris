/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.map;

import net.minecraft.world.gen.ChunkGenerator;

public interface IMapGenData {
    default void init(ChunkGenerator<?> chunkGen) {
    }
}
