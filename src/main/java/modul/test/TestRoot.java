/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.test;

import modul.core.MListFile;
import modul.root.ModulRoot;

public class TestRoot implements ModulRoot {
    private final String dist;

    public TestRoot( String dist ) {
        this.dist = dist;
    }

    public String getDist() {
        return dist;
    }

    @Override
    public MListFile.Context buildCoreContext( MListFile.Context ctx ) {
        return ctx.withCondition( MListFile.prefix( "DIST", str -> str.equalsIgnoreCase( dist ) ) );
    }

    @Override
    public Object instantiate( Class<?> cls ) {
        try {
            return cls.newInstance();
        } catch( InstantiationException | IllegalAccessException e ) {
            throw new IllegalStateException( "Unable to instantiate " + cls, e );
        }
    }
}
