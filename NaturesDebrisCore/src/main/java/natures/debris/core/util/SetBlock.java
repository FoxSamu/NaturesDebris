package natures.debris.core.util;

public class SetBlock {
    public static final int UPDATE = 1;
    public static final int SEND_CLIENTS = 2;
    public static final int NO_RENDER = 4;
    public static final int IMMEDIATE_RENDER = 8;
    public static final int NO_NEIGHBORS = 16;
    public static final int NO_NEIGHBOR_DROPS = 32;
    public static final int MOVING = 64;
    public static final int NO_LIGHT = 128;

    public static final int BASE = UPDATE | SEND_CLIENTS;
}
