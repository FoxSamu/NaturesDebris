/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.net;

import modernity.client.ModernityClientOld;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Packet that sends the world seed to clients.
 */
public class SSeedPacket implements Packet {
    private long seed;

    public SSeedPacket( long seed ) {
        this.seed = seed;
    }

    public SSeedPacket() {

    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeLong( seed );
    }

    @Override
    public void read( PacketBuffer buf ) {
        seed = buf.readLong();
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        ModernityClientOld.get().setLastWorldSeed( seed );
    }
}
