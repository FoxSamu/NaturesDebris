/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modul.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModuleType<T, M extends Module> {
    private static final ArrayList<ModuleType<?, ?>> MODULE_TYPES = new ArrayList<>();
    private static final HashMap<String, ModuleType<?, ?>> MODULE_TYPE_MAP = new HashMap<>();

    private final Function<T, M> constructor;
    private final String name;
    private final int id;

    public ModuleType( Function<T, M> constructor, String name ) {
        if( MODULE_TYPE_MAP.containsKey( name ) ) {
            throw new IllegalStateException( "Module type with name '" + name + "' already exists!" );
        }

        this.constructor = constructor;
        this.name = name;

        id = MODULE_TYPES.size();
        MODULE_TYPES.add( this );
        MODULE_TYPE_MAP.put( name, this );
    }

    public ModuleType( Supplier<M> constructor, String name ) {
        this( t -> constructor.get(), name );
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public M construct( T instance ) {
        return constructor.apply( instance );
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) return true;
        if( ! ( o instanceof ModuleType ) ) return false;
        ModuleType<?, ?> that = (ModuleType<?, ?>) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public static ModuleType<?, ?> getByName( String name ) {
        return MODULE_TYPE_MAP.get( name );
    }

    public static ModuleType<?, ?> getByID( int id ) {
        if( id < 0 || id >= MODULE_TYPES.size() ) return null;
        return MODULE_TYPES.get( id );
    }
}
