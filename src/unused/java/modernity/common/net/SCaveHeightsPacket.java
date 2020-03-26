/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.net;

import modernity.common.generator.structure.CaveStructure;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Packet that sends cave heights to the client.
 */
public class SCaveHeightsPacket implements Packet {
    private int x;
    private int z;
    private long[] heights;

    private boolean invalid;

    public SCaveHeightsPacket() {
    }

    public SCaveHeightsPacket( IChunk chunk ) {
        x = chunk.getPos().x;
        z = chunk.getPos().z;

        StructureStart start = chunk.getStructureStart( CaveStructure.NAME );
        if( start == null ) {
            invalid = true;
            return;
        }
        CaveStructure.Piece piece = (CaveStructure.Piece) start.getComponents().get( 0 );
        heights = piece.getLimitsAsLongMap();
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeInt( x );
        buf.writeInt( z );
        buf.writeLongArray( heights );
    }

    @Override
    public void read( PacketBuffer buf ) {
        x = buf.readInt();
        z = buf.readInt();
        heights = buf.readLongArray( null );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();

        System.out.println( "Received cave packet" );

        ClientWorld world = Minecraft.getInstance().world;
        StructureStart start = new CaveStructure.Start( x, z, world.getBiome( new BlockPos( x * 16, 0, z * 16 ) ) );

        IChunk chunk = world.getChunk( x, z );
        chunk.putStructureStart( CaveStructure.NAME, start );

        chunk.addStructureReference( CaveStructure.NAME, ChunkPos.asLong( x, z ) );
    }

    @Override
    public boolean refuseSending() {
        return invalid;
    }
}
