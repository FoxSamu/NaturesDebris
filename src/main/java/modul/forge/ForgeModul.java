/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.forge;

import modul.module.ModuleContext;

public final class ForgeModul {
    public static final ModuleContext CLIENT = new ModuleContext( "forge_client" );
    public static final ModuleContext SERVER = new ModuleContext( "forge_server" );
    public static final ModuleContext WORLD_CLIENT = new ModuleContext( "forge_world_client" );
    public static final ModuleContext WORLD_SERVER = new ModuleContext( "forge_world_server" );

    static void load() {
        CLIENT.load();
        SERVER.load();
        WORLD_CLIENT.load();
        WORLD_SERVER.load();
    }

    private ForgeModul() {
    }
}
