/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
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
