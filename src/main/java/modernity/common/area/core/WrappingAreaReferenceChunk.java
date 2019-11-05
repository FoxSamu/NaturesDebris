package modernity.common.area.core;

import net.minecraft.util.math.ChunkPos;

import java.util.stream.LongStream;

class WrappingAreaReferenceChunk implements IAreaReferenceChunk {
    private final IAreaReferenceChunk chunk;

    WrappingAreaReferenceChunk( IAreaReferenceChunk chunk ) {
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
