/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.api.event;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

import modernity.common.util.ProxyCommon;

public class ModernityProxyReadyEvent extends Event {
    public final LogicalSide side;
    public final ProxyCommon proxy;

    public ModernityProxyReadyEvent( LogicalSide side, ProxyCommon proxy ) {
        this.side = side;
        this.proxy = proxy;
    }
}
