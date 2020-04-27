/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.farmland;

import net.minecraft.util.IStringSerializable;

public enum Fertility implements IStringSerializable {
    NONE( "none" ),
    SALTY( "salty" ),
    WET( "wet" ),
    FERTILE( "fertile" ),
    DECAYED( "decayed" );

    private final String name;

    Fertility( String name ) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
