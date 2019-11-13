package modernity.common.area.core;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IAreaMessage<T extends MessagingArea<T>> {
    void write( PacketBuffer buf );

    @OnlyIn( Dist.CLIENT )
    void read( PacketBuffer buf );

    @OnlyIn( Dist.CLIENT )
    void receive( T area );
}
