/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package net.redgalaxy.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private T value;
    private Supplier<? extends T> supplier;

    private Lazy( Supplier<? extends T> supplier ) {
        set( supplier );
    }

    private Lazy( T value ) {
        set( value );
    }

    public void set( Supplier<? extends T> supplier ) {
        if( supplier == null ) throw new NullPointerException( "Supplier is null" );
        this.supplier = supplier;
        this.value = null;
    }

    public void set( T value ) {
        if( value == null ) throw new NullPointerException( "Value is null" );
        this.supplier = () -> value;
        this.value = value;
    }

    public void unload() {
        value = null;
    }

    @Override
    public T get() {
        if( value == null ) {
            value = supplier.get();
            if( value == null ) {
                throw new NullPointerException( "Supplier returned null" );
            }
        }
        return value;
    }

    public Optional<T> getIfLoaded() {
        return Optional.ofNullable( value );
    }

    public boolean isLoaded() {
        return value != null;
    }

    public void ifLoaded( Consumer<T> consumer ) {
        if( value != null ) consumer.accept( value );
    }

    public void ifNotLoaded( Runnable runnable ) {
        if( value == null ) runnable.run();
    }

    public <D> Lazy<D> map( Function<T, D> fn ) {
        return new Lazy<>( () -> fn.apply( get() ) );
    }

    public static <T> Lazy<T> ofLoaded( T value ) {
        return new Lazy<>( value );
    }

    public static <T> Lazy<T> of( Supplier<? extends T> supplier ) {
        return new Lazy<>( supplier );
    }
}
