/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package natures.debris.common.block.soil;

import net.minecraft.util.IStringSerializable;

public enum Fertility implements IStringSerializable {
    NONE("none"),
    SALTY("salty"),
    WET("wet"),
    FERTILE("fertile"),
    DECAYED("decayed");

    private final String name;

    Fertility(String name) {
        this.name = name;
    }

    @Override
    public String getString() {
        return name;
    }
}
