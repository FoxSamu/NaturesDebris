/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import net.minecraft.util.IStringSerializable;

public enum Fertility implements IStringSerializable {
    NOT_FERTILE( "not_fertile" ),
    FERTILE( "fertile" ),
    SALINE( "saline" ),
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
