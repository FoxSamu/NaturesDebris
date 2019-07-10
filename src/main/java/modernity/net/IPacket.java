/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import net.minecraft.network.PacketBuffer;

public interface IPacket {
    void write( PacketBuffer buf );
    void read( PacketBuffer buf );
    void process( ProcessContext ctx );

    default String getName() {
        return getClass().getSimpleName();
    }
}
