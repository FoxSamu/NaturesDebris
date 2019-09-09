/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.net.pkt;

import modernity.client.util.ProxyClient;
import modernity.net.IPacket;
import modernity.net.ProcessContext;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SPacketSeed implements IPacket {
    private long seed;

    public SPacketSeed( long seed ) {
        this.seed = seed;
    }

    public SPacketSeed() {

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
        ProxyClient.get().setLastWorldSeed( seed );
    }
}
