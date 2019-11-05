package modernity.common.area.core;

import net.minecraft.util.math.ChunkPos;

import java.util.stream.LongStream;

public interface IAreaReferenceChunk {
    boolean hasReference( long ref );
    boolean hasReferences();

    LongStream referenceStream();

    ChunkPos getPos();
}
