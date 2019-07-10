/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import java.util.EnumMap;

public class PacketProfile {
    private final EnumMap<ESide, PacketRegistry> packetRegistries = new EnumMap<>( ESide.class );

    public void registerPacket( ESide side, Class<? extends IPacket> packet ) {
        packetRegistries.computeIfAbsent( side, s -> new PacketRegistry() ).register( packet );
    }

    public int getID( ESide side, IPacket pkt ) {
        return packetRegistries.get( side ).getID( pkt );
    }

    public IPacket newPacket( ESide side, int id ) {
        return packetRegistries.get( side ).getPacket( id );
    }
}
