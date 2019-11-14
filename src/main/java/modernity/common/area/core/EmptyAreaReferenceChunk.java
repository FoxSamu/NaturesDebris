/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.LongStream;

@OnlyIn( Dist.CLIENT )
public class EmptyAreaReferenceChunk implements IAreaReferenceChunk {
    public final int x;
    public final int z;

    public EmptyAreaReferenceChunk( int x, int z ) {
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean hasReference( long ref ) {
        return false;
    }

    @Override
    public boolean hasReferences() {
        return false;
    }

    @Override
    public LongStream referenceStream() {
        return LongStream.empty();
    }

    @Override
    public ChunkPos getPos() {
        return new ChunkPos( x, z );
    }
}
