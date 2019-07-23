/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;

import modernity.common.util.ProxyCommon;

/**
 * A process context is used during packet processing to provide extra information about the packet, usually used to
 * send responses or to schedule processing on a main thread. It is passed as argument to {@link IPacket#process} during
 * processing.
 */
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

    /**
     * Returns the side the packet originates from. This is the side where the packet was sent from.
     * @return The origin side.
     */
    public ESide senderSide() {
        return fromSide;
    }

    /**
     * Returns the side that received the current packet. This is the side that processes the packet.
     * @return The receiving side.
     */
    public ESide receiverSide() {
        return fromSide.opposite();
    }

    /**
     * Returns the player that sent the current packet. This method returns {@code null} when the server sent the
     * packet.
     * @return The player that sent the current packet, or {@code null} if this is the server.
     */
    public EntityPlayerMP player() {
        return player;
    }

    /**
     * Returns the channel used to send the current packet. Response packets and other reactions can be sent over this
     * channel.
     * @return The channel used to send the current packet.
     */
    public PacketChannel channel() {
        return channel;
    }

    /**
     * Sends a response packet to the sender of this packet. This is sending a packet to the player client that sent the
     * packet, or to the server if the server sent this packet.
     * @param packet The response packet to send. This packet must be registered in the used {@linkplain PacketChannel
     *               channel}.
     */
    public void sendResponse( IPacket packet ) {
        if( receiverSide().isServer() ) {
            channel().sendToPlayer( packet, player() );
        } else {
            channel().sendToServer( packet );
        }
    }

    /**
     * Ensures this packet does the processing on the main thread listener of the receiving side. That is the {@link
     * MinecraftServer} instance when on the {@linkplain ESide#SERVER server} side and the {@link Minecraft} instance
     * when on the {@linkplain ESide#CLIENT client} side. When called from another thread than the side's main thread,
     * it will schedule the task on the thread listener and throws an exception to stop the current method from running.
     * When called from the main thread, this method does nothing and returns. It's recommended to call this method as
     * first statement in the {@link IPacket#process} method. Every statement before a call to this method may
     * potentially be executed twice.
     * @throws ThreadQuickExitException Thrown after scheduling the task, to stop the current thread from processing
     *                                  this packet. <i>Prevent catching this exception!!</i>
     */
    public void ensureMainThread() {
        IThreadListener threadListener = ProxyCommon.get().getThreadListener( receiverSide().toLogical() );
        if( ! threadListener.isCallingFromMinecraftThread() ) {
            threadListener.addScheduledTask( () -> packet.process( this ) );
            throw ThreadQuickExitException.INSTANCE;
        }
    }

    /**
     * Schedules the specified task on the main thread listener of the receiving side. That is the {@link
     * MinecraftServer} instance when on the {@linkplain ESide#SERVER server} side and the {@link Minecraft} instance
     * when on the {@linkplain ESide#CLIENT client} side. When the curren thread is already the main thread of the
     * receiving side, the specified task will be executed immediately.
     * @param task The task to run on the main thread.
     */
    public void scheduleOnMainThread( Runnable task ) {
        IThreadListener threadListener = ProxyCommon.get().getThreadListener( receiverSide().toLogical() );
        if( ! threadListener.isCallingFromMinecraftThread() ) {
            threadListener.addScheduledTask( task );
        } else {
            task.run();
        }
    }
}