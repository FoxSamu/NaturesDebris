/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modernity;

import modul.module.ModuleContext;

public final class MDModules {
    public static final ModuleContext MODERNITY = new ModuleContext( "modernity" );

    static void load() {
        MODERNITY.load();
    }

    private MDModules() {
    }
}
