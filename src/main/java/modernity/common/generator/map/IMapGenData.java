/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map;

import net.minecraft.world.gen.ChunkGenerator;

public interface IMapGenData {
    default void init( ChunkGenerator<?> chunkGen ) {
    }
}
