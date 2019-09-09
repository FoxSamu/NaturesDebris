/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.util;

public final class BlockUpdates {
    public static final int CAUSE_UPDATE = 1;
    public static final int NOTIFY_CLIENTS = 2;
    public static final int NO_RENDER = 4;
    public static final int FORCE_RENDER = 8;
    public static final int NO_NEIGHBOR_REACTIONS = 16;
    public static final int NO_NEIGHBOR_DROPS = 32;
    public static final int MOVING = 64;

    private BlockUpdates() {
        throw new UnsupportedOperationException( "No BlockUpdates instances for you!" );
    }
}
