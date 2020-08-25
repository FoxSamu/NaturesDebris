/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.portal;

import net.minecraft.util.IStringSerializable;

public enum PortalCornerState implements IStringSerializable {
    INACTIVE("inactive", 0),
    EYE("eye", 0),
    ACTIVE("active", 8),
    EXHAUSTED("exhausted", 0);

    private final String name;
    private final int lightValue;

    PortalCornerState(String name, int lightValue) {
        this.name = name;
        this.lightValue = lightValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLightValue() {
        return lightValue;
    }
}
