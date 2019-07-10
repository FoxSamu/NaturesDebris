/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import net.minecraftforge.fml.LogicalSide;

public enum ESide {
    CLIENT,
    SERVER;

    public ESide opposite() {
        return this == CLIENT ? SERVER : CLIENT;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isServer() {
        return this == SERVER;
    }

    public LogicalSide toLogical() {
        return this == CLIENT ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    public static ESide fromLogical( LogicalSide side ) {
        return side == LogicalSide.SERVER ? SERVER : CLIENT;
    }
}
