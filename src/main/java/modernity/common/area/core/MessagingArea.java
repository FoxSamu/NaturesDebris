package modernity.common.area.core;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public abstract class MessagingArea<T extends MessagingArea<T>> extends Area {

    private final Int2ObjectOpenHashMap<Supplier<? extends IAreaMessage<T>>> messageConstructors = new Int2ObjectOpenHashMap<>();
    private final Object2IntOpenHashMap<Class<? extends IAreaMessage>> messageIDs = new Object2IntOpenHashMap<>();


    public MessagingArea( AreaType type, World world, AreaBox box ) {
        super( type, world, box );
    }

    protected <D extends IAreaMessage<T>> void registerMessage( int id, Class<D> cls, Supplier<? extends D> constr ) {
        messageConstructors.put( id, constr );
        messageIDs.put( cls, id );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public Object readMessage( PacketBuffer buf ) {
        if( buf.readableBytes() < 4 ) return null;
        int id = buf.readInt();
        Supplier<? extends IAreaMessage<T>> supplier = messageConstructors.get( id );
        if( supplier == null ) return null;
        IAreaMessage<T> msg = supplier.get();
        if( msg == null ) return null;
        msg.read( buf );
        return msg;
    }

    @Override
    public void writeMessage( Object msg, PacketBuffer buf ) {
        if( msg instanceof IAreaMessage ) {
            IAreaMessage message = (IAreaMessage) msg;
            if( messageIDs.containsKey( message.getClass() ) ) {
                buf.writeInt( messageIDs.getInt( message.getClass() ) );
                message.write( buf );
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    @OnlyIn( Dist.CLIENT )
    @Override
    public void receiveMessage( Object msg ) {
        if( msg instanceof IAreaMessage ) {
            ( (IAreaMessage<T>) msg ).receive( (T) this );
        }
    }
}
