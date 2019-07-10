/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.IThreadListener;

import modernity.common.util.ProxyCommon;

public class ProcessContext {
    private final ESide fromSide;
    private final EntityPlayerMP player;
    private final PacketChannel channel;
    private final IPacket packet;

    public ProcessContext( ESide fromSide, EntityPlayerMP player, PacketChannel channel, IPacket packet ) {
        this.fromSide = fromSide;
        this.player = player;
        this.channel = channel;
        this.packet = packet;
    }

    public ESide senderSide() {
        return fromSide;
    }

    public ESide receiverSide() {
        return fromSide.opposite();
    }

    public EntityPlayerMP player() {
        return player;
    }

    public PacketChannel channel() {
        return channel;
    }

    public void sendResponse( IPacket packet ) {
        if( receiverSide().isServer() ) {
            channel().sendToPlayer( packet, player() );
        } else {
            channel().sendToServer( packet );
        }
    }

    public void scheduleOnMainThread() {
        IThreadListener threadListener = ProxyCommon.get().getThreadListener( receiverSide().toLogical() );
        if( ! threadListener.isCallingFromMinecraftThread() ) {
            threadListener.addScheduledTask( () -> packet.process( this ) );
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}
