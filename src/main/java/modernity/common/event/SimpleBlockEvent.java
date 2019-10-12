package modernity.common.event;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class SimpleBlockEvent extends BlockEvent<Void> {

    protected SimpleBlockEvent( int range ) {
        super( range );
    }

    protected SimpleBlockEvent() {
        super();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public Void readData( PacketBuffer buffer ) {
        return null;
    }

    @Override
    public void writeData( Void data, PacketBuffer buffer ) {

    }
}
