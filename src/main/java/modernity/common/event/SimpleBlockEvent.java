package modernity.common.event;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A block event that does not use any data.
 */
public abstract class SimpleBlockEvent extends BlockEvent<Void> {

    protected SimpleBlockEvent( int range ) {
        super( range );
    }

    protected SimpleBlockEvent() {
        super();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public final Void readData( PacketBuffer buffer ) {
        return null;
    }

    @Override
    public final void writeData( Void data, PacketBuffer buffer ) {

    }
}
