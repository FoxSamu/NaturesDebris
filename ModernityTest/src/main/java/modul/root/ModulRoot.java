/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
