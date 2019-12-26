/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.test;

import modul.core.ModulCore;

public final class ModulTest {
    private ModulTest() {
    }

    public static void main( String[] args ) {
        ModulCore.start( new TestRoot( "Server" ) );
    }
}
