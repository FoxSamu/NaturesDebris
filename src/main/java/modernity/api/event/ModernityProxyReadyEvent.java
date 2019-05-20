package modernity.api.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.Event;

import modernity.common.util.ProxyCommon;

public class ModernityProxyReadyEvent extends Event {
    public final Dist dist;
    public final ProxyCommon proxy;

    public ModernityProxyReadyEvent( Dist dist, ProxyCommon proxy ) {
        this.dist = dist;
        this.proxy = proxy;
    }
}
