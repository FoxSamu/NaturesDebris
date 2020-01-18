/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import net.minecraft.util.IStringSerializable;

public enum Wetness implements IStringSerializable {
    DRY( "dry" ),
    WET( "wet" ),
    FLOODED( "flooded" );

    private final String name;

    Wetness( String name ) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
