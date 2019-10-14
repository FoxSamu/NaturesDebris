package modernity.common.net;

import modernity.common.event.BlockEvent;
import modernity.common.registry.MDRegistries;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Packet that sends a block event.
 */
@SuppressWarnings( "unchecked" )
public class SEventPacket implements Packet {
    private BlockPos pos;
    private BlockEvent type;
    private Object data;

    public <T> SEventPacket( BlockPos pos, BlockEvent<T> type, T data ) {
        this.pos = pos;
        this.type = type;
        this.data = data;
    }

    public SEventPacket() {
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeInt( pos.getX() );
        buf.writeInt( pos.getY() );
        buf.writeInt( pos.getZ() );
        buf.writeInt( MDRegistries.BLOCK_EVENTS.getID( type ) );
        type.writeData( data, buf );
    }

    @Override
    public void read( PacketBuffer buf ) {
        pos = new BlockPos( buf.readInt(), buf.readInt(), buf.readInt() );
        type = MDRegistries.BLOCK_EVENTS.getValue( buf.readInt() );
        data = type.readData( buf );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        type.playEvent( Minecraft.getInstance().world, pos, data );
    }
}
