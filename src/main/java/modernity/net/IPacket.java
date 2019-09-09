/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 11 - 2019
 */

package modernity.net;

import net.minecraft.network.PacketBuffer;

/**
 * Basic packet interface.
 */
public interface IPacket {
    /**
     * Writes additional data to the specified packet buffer.
     * @param buf The packet buffer to write to.
     */
    void write( PacketBuffer buf );

    /**
     * Reads additional data from the specified packet buffer.
     * @param buf The packet buffer to read from.
     */
    void read( PacketBuffer buf );

    /**
     * Processes the packet on the specific context.
     * @param ctx The processing context.
     */
    void process( ProcessContext ctx );

    /**
     * Returns the name of this packet.
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
