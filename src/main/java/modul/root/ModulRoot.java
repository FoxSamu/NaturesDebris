/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.root;

import modul.Modul;
import modul.core.MListFile;

public interface ModulRoot {
    MListFile.Context buildCoreContext( MListFile.Context ctx );

    Object instantiate( Class<?> cls );

    default void onInstantiate( Modul modul ) {

    }

    default void onStart( Modul modul ) {

    }
}
