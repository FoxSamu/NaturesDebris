/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.area.core;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import modernity.api.util.StreamUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.math.ChunkPos;

import java.util.stream.LongStream;

public class SimpleAreaReferenceChunk implements IAreaReferenceChunk {
    public final int x;
    public final int z;
    private final LongSet references = new LongOpenHashSet();

    private boolean dirty;

    public final WrappingAreaReferenceChunk unmodifiable = new WrappingAreaReferenceChunk( this );

    public SimpleAreaReferenceChunk( int x, int z ) {
        this.x = x;
        this.z = z;
    }

    @Override
    public LongStream referenceStream() {
        return StreamUtil.streamLongs( references, true );
    }

    @Override
    public boolean hasReference( long ref ) {
        return references.contains( ref );
    }

    public void addReference( long ref ) {
        references.add( ref );
        dirty = true;
    }

    public void removeReference( long ref ) {
        references.remove( ref );
        dirty = true;
    }

    @Override
    public boolean hasReferences() {
        return ! references.isEmpty();
    }

    public void write( CompoundNBT nbt ) {
        nbt.put( "references", new LongArrayNBT( references ) );
    }

    public void read( CompoundNBT nbt ) {
        references.clear();
        long[] ls = nbt.getLongArray( "references" );
        for( long l : ls ) addReference( l );
        dirty = false;
    }


    @Override
    public ChunkPos getPos() {
        return new ChunkPos( x, z );
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty( boolean dirty ) {
        this.dirty = dirty;
    }
}
