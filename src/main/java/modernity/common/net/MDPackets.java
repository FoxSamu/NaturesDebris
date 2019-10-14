package modernity.common.net;

import modernity.network.ESide;
import modernity.network.PacketChannel;

public final class MDPackets {
    private MDPackets() {
    }

    public static void register( PacketChannel channel ) {
        channel.register( ESide.SERVER, SSpawnEntityPacket.class );
        channel.register( ESide.SERVER, SEventPacket.class );
        channel.register( ESide.SERVER, SSeedPacket.class );
        channel.register( ESide.SERVER, SCaveHeightsPacket.class );
    }
}
