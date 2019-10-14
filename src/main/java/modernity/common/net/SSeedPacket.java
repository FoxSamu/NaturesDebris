/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.net;

import modernity.client.ModernityClient;
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
        ModernityClient.get().setLastWorldSeed( seed );
    }
}
