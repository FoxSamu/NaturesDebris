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

/**
 * Fired after {@link Modernity} is initialized
 *
 * @author RGSW
 */
public class ModernityReadyEvent extends Event {
    public final LogicalSide side;
    public final Modernity modernity;

    public ModernityReadyEvent( LogicalSide side, Modernity modernity ) {
        this.side = side;
        this.modernity = modernity;
    }
}
