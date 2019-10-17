package modernity.common.net;

import modernity.api.dimension.ISatelliteDimension;
import modernity.common.environment.satellite.SatelliteData;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A packet that sends {@linkplain SatelliteData satellite data} to the client.
 */
public class SSatellitePacket implements Packet {

    private int tick;
    private int phase;

    public SSatellitePacket( int tick, int phase ) {
        this.tick = tick;
        this.phase = phase;
    }

    public SSatellitePacket() {

    }

    @Override
    public void write( PacketBuffer buf ) {
        int mixed = tick << 3 | phase;

        buf.writeInt( mixed );
    }

    @Override
    public void read( PacketBuffer buf ) {
        int mixed = buf.readInt();

        tick = mixed >>> 3;
        phase = mixed & 7;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();

        Dimension dimension = Minecraft.getInstance().world.dimension;
        if( dimension instanceof ISatelliteDimension ) {
            SatelliteData data = ( (ISatelliteDimension) dimension ).getSatelliteData();
            data.set( phase, tick );
        }
    }
}
