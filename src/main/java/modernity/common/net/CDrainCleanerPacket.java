/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.net;

import modernity.common.container.CleanerContainer;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CDrainCleanerPacket implements Packet {
    private static final Logger LOGGER = LogManager.getLogger();

    private int windowID;

    public CDrainCleanerPacket() {
    }

    public CDrainCleanerPacket( int id ) {
        windowID = id;
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeInt( windowID );
    }

    @Override
    public void read( PacketBuffer buf ) {
        windowID = buf.readInt();
    }

    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        Container container = ctx.player().openContainer;
        if( container.windowId == windowID ) {
            if( ! ( container instanceof CleanerContainer ) ) {
                LOGGER.warn( "Received cleaner drain packet for window that is not a cleaner" );
            } else {
                ( (CleanerContainer) container ).drain();
            }
        }
    }
}
