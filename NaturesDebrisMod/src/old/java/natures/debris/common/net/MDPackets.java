/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.net;

import modernity.network.NetworkSide;
import modernity.network.PacketChannel;

/**
 * Class that handles registry of Modernity packets.
 */
public final class MDPackets {
    private MDPackets() {
    }

    public static void register(PacketChannel channel) {
        channel.register(NetworkSide.SERVER, SSpawnEntityPacket.class);
        channel.register(NetworkSide.SERVER, SEventPacket.class);
        channel.register(NetworkSide.SERVER, SSeedPacket.class);
        channel.register(NetworkSide.SERVER, SSatellitePacket.class);
        channel.register(NetworkSide.SERVER, SEnvironmentPacket.class);
        channel.register(NetworkSide.SERVER, SAreaUntrackPacket.class);
        channel.register(NetworkSide.SERVER, SAreaUpdatePacket.class);
        channel.register(NetworkSide.SERVER, SAreaMessagePacket.class);
    }
}
