/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modul.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@SuppressWarnings( "unchecked" )
public class ModuleManager<T> {
    private final T instance;

    private final HashMap<ModuleType<? extends T, ?>, Module> modules = new HashMap<>();

    public ModuleManager( T instance, ModuleContext moduleContext ) {
        this.instance = instance;

        Iterable<ModuleType<T, ?>> types = moduleContext.getModuleTypes();
        for( ModuleType<T, ?> type : types ) {
            Module module = type.construct( instance );
            if( module == null ) {
                throw new NullPointerException( "Constructor constructed null module" );
            }

            modules.put( type, module );
        }
    }

    public void init() {
        for( Module module : modules.values() ) {
            module.onInit();
        }
    }

    public <M extends Module> Optional<M> getModule( ModuleType<? extends T, M> type ) {
        return Optional.ofNullable( (M) modules.get( type ) );
    }

    public T getInstance() {
        return instance;
    }

    public Collection<Module> getModules() {
        return modules.values();
    }
}
