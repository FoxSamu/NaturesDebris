/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.generic.event;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class OverworldStructureEvent extends Event {
    private final Type type;

    public OverworldStructureEvent( Type type ) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        MINESHAFT,
        VILLAGE,
        STRONGHOLD,
        SWAMP_HUT,
        DESERT_PYRAMID,
        JUNGLE_TEMPLE,
        IGLOO,
        SHIPWRECK,
        OCEAN_MONUMENT,
        WOODLAND_MANSION,
        OCEAN_RUIN,
        BURIED_TREASURE
    }
}
