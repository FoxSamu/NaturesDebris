/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

import net.minecraft.util.math.ChunkPos;

import java.util.stream.LongStream;

public interface IAreaReferenceChunk {
    boolean hasReference( long ref );
    boolean hasReferences();

    LongStream referenceStream();

    ChunkPos getPos();
}
