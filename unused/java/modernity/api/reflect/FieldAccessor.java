/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.generic.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

/**
 * Wraps a {@link Field} and suppresses any exceptions using {@link RuntimeException}s upon usage.
 *
 * @author RGSW
 */
@SuppressWarnings( "unchecked" )
public class FieldAccessor<T, R> {
    private final Field field;

    /**
     * Wraps a {@link Field} instance
     */
    public FieldAccessor( Field field ) {
        this.field = field;
        try {
            Field modifiers = field.getClass().getDeclaredField( "modifiers" );
            modifiers.setAccessible( true );
            int value = field.getModifiers();
            value &= ~ Modifier.FINAL;
            modifiers.set( field, value );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
        field.setAccessible( true );
    }

    /**
     * Loads a field from a class
     *
     * @param cls  The class to load from
     * @param name The field name. Use SRG mapping for Minecraft classes.
     */
    public FieldAccessor( Class<? super T> cls, String name ) {
        this.field = ObfuscationReflectionHelper.findField( cls, name );
        try {
            Field modifiers = field.getClass().getDeclaredField( "modifiers" );
            modifiers.setAccessible( true );
            int value = field.getModifiers();
            value &= ~ Modifier.FINAL;
            modifiers.set( field, value );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
        field.setAccessible( true );
    }

    /**
     * Get the field's value.
     *
     * @param t In instance to get the field from, or null in case of a static field.
     */
    public R get( T t ) {
        try {
            return (R) field.get( t );
        } catch( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Set the field's value.
     *
     * @param t     In instance to get the field on, or null in case of a static field.
     * @param value The new value
     */
    public void set( T t, R value ) {
        try {
            field.set( t, value );
        } catch( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns a manager for setting the field on a specific instance multiple times.
     *
     * @param inst The object instance to set the field on.
     */
    public Manager forInstance( T inst ) {
        return new Manager( inst );
    }

    /**
     * Returns a manager for setting the field statically multiple times.
     */
    public Manager forStatic() {
        return new Manager( null );
    }

    /**
     * Returns the wrapped {@link Field} instance.
     */
    public Field getField() {
        return field;
    }

    public class Manager implements Supplier<R> {
        private final T instance;

        private Manager( T instance ) {
            this.instance = instance;
        }

        /**
         * Get the field's value
         */
        @Override
        public R get() {
            return FieldAccessor.this.get( instance );
        }

        /**
         * Set the field's value
         *
         * @param value The new value
         * @return This instance for chaining
         */
        public Manager set( R value ) {
            FieldAccessor.this.set( instance, value );
            return this;
        }
    }
}
