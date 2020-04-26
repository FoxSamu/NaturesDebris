/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul;

import modul.root.ModulRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Modul {
    private final ModulRoot root;
    private final List<Class<?>> rootClasses;
    private final List<Object> rootObjects = new ArrayList<>();

    public Modul( ModulRoot root, List<Class<?>> rootClasses ) {
        this.root = root;
        this.rootClasses = rootClasses;

        root.onInstantiate( this );
    }

    public void startUp() {
        for( Class<?> cls : rootClasses ) {
            rootObjects.add( root.instantiate( cls ) );
        }
        root.onStart( this );
    }

    public List<Object> objects() {
        return Collections.unmodifiableList( rootObjects );
    }

    public List<Class<?>> classes() {
        return Collections.unmodifiableList( rootClasses );
    }

    public ModulRoot getRoot() {
        return root;
    }
}
