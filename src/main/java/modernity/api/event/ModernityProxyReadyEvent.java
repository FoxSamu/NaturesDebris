/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.api.event;

import modernity.common.Modernity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

public class ModernityProxyReadyEvent extends Event {
    public final LogicalSide side;
    public final Modernity proxy;

    public ModernityProxyReadyEvent( LogicalSide side, Modernity proxy ) {
        this.side = side;
        this.proxy = proxy;
    }
}
