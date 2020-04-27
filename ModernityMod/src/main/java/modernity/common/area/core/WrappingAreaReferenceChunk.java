/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.area.core;

import net.minecraft.util.math.ChunkPos;

import java.util.stream.LongStream;

public class WrappingAreaReferenceChunk implements IAreaReferenceChunk {
    private final IAreaReferenceChunk chunk;

    public WrappingAreaReferenceChunk( IAreaReferenceChunk chunk ) {
        this.chunk = chunk;
    }

    @Override
    public boolean hasReference( long ref ) {
        return chunk.hasReference( ref );
    }

    @Override
    public boolean hasReferences() {
        return chunk.hasReferences();
    }

    @Override
    public LongStream referenceStream() {
        return chunk.referenceStream();
    }

    @Override
    public ChunkPos getPos() {
        return chunk.getPos();
    }
}
