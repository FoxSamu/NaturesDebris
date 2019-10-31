package modernity.common.area;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import modernity.api.util.StreamUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;

import java.util.stream.LongStream;

public class AreaReferenceChunk {
    public final int x;
    public final int z;
    private final LongSet references = new LongOpenHashSet();

    public AreaReferenceChunk( int x, int z ) {
        this.x = x;
        this.z = z;
    }

    public LongStream referenceStream() {
        return StreamUtil.streamLongs( references, true );
    }

    public boolean hasReference( long ref ) {
        return references.contains( ref );
    }

    public void addReference( long ref ) {
        references.add( ref );
    }

    public void removeReference( long ref ) {
        references.remove( ref );
    }

    public void removeAllReferences() {
        references.clear();
    }

    public void write( CompoundNBT nbt ) {
        nbt.put( "references", new LongArrayNBT( references ) );
    }

    public void read( CompoundNBT nbt ) {
        references.clear();
        long[] ls = nbt.getLongArray( "references" );
        for( long l : ls ) references.add( l );
    }
}
