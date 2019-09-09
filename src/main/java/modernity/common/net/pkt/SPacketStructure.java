/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.net.pkt;

import it.unimi.dsi.fastutil.longs.LongSet;
import modernity.net.IPacket;
import modernity.net.ProcessContext;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureIO;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SPacketStructure implements IPacket {
    private String structure;
    private int x;
    private int z;
    private StructureStart start;
    private long[] refs;
    private NBTTagCompound startNBT;

    public SPacketStructure() {
    }

    public SPacketStructure( String structureName, IChunk chunk ) {
        this.structure = structureName;
        x = chunk.getPos().x;
        z = chunk.getPos().z;

        LongSet refs = chunk.getStructureReferences( structureName );
        StructureStart start = chunk.getStructureStart( structureName );
        if( refs != null ) {
            this.refs = new long[ refs.size() ];
            int i = 0;
            for( long l : refs ) {
                this.refs[ i ] = l;
                i++;
            }
        } else {
            this.refs = new long[ 0 ];
        }
        if( start != null ) {
            this.start = start;
            this.startNBT = start.write( x, z );
        }
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeString( structure, 256 );
        buf.writeInt( x );
        buf.writeInt( z );
        buf.writeBoolean( startNBT != null );
        if( startNBT != null ) {
            buf.writeCompoundTag( startNBT );
        }
        buf.writeLongArray( refs );
    }

    @Override
    public void read( PacketBuffer buf ) {
        structure = buf.readString( 256 );
        x = buf.readInt();
        z = buf.readInt();
        startNBT = null;
        if( buf.readBoolean() ) {
            startNBT = buf.readCompoundTag();
        }
        refs = buf.readLongArray( null );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();

        IWorld world = Minecraft.getInstance().world;
        start = null;
        if( startNBT != null ) {
            start = StructureIO.func_202602_a( startNBT, world );
        }

        IChunk chunk = world.getChunk( x, z );

        if( start != null ) {
            chunk.putStructureStart( structure, start );
        }

        LongSet refs = chunk.getStructureReferences( structure );
        if( refs != null ) {
            refs.clear();
            for( long l : this.refs ) {
                refs.add( l );
            }
        }
    }
}
