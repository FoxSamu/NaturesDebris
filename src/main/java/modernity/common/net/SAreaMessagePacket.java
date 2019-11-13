package modernity.common.net;

import modernity.common.area.core.Area;
import modernity.common.area.core.ClientWorldAreaManager;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.network.PacketBuffer;

public class SAreaMessagePacket implements Packet {
    private Area area;
    private long refID;
    private Object object;

    public SAreaMessagePacket( Area area, Object object ) {
        this.area = area;
        this.refID = area.getReferenceID();
        this.object = object;
    }

    public SAreaMessagePacket() {
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeLong( refID );
        area.writeMessage( object, buf );
    }

    @Override
    public void read( PacketBuffer buf ) {
        refID = buf.readLong();
        ClientWorldAreaManager
            .get()
            .ifPresent( manager -> {
                area = manager.getLoadedArea( refID );
                object = area.readMessage( buf );
            } );
    }

    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        if( area != null && object != null ) {
            area.receiveMessage( object );
        }
    }
}
