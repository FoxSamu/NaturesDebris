/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.util.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wraps a {@link Method} and suppresses any exceptions using {@link RuntimeException}s upon usage.
 *
 * @author RGSW
 */
@SuppressWarnings( "unchecked" )
public class MethodAccessor<T, R> {
    private final Method method;

    /**
     * Wraps a {@link Method} instance
     */
    public MethodAccessor( Method method ) {
        this.method = method;
        method.setAccessible( true );
    }

    /**
     * Loads a method from a class
     *
     * @param cls    The class to load from
     * @param name   The field name. Use SRG mapping for Minecraft classes.
     * @param params The parameter classes.
     */
    public MethodAccessor( Class<? super T> cls, String name, Class<?>... params ) {
        this.method = ObfuscationReflectionHelper.findMethod( cls, name, params );
        method.setAccessible( true );
    }

    /**
     * Call the method on the specified instance
     *
     * @param t      The instance to call on, or null in case of a static method
     * @param values The arguments
     * @return The returned value
     */
    public R call( T t, Object... values ) {
        try {
            return (R) method.invoke( t, values );
        } catch( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns the wrapped {@link Method} instance.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns a manager for calling the method on a specific instance multiple times.
     *
     * @param inst The object instance to call the method on.
     */
    public Manager forInstance( T inst ) {
        return new Manager( inst );
    }

    /**
     * Returns a manager for calling the method statically multiple times.
     */
    public Manager forStatic() {
        return new Manager( null );
    }

    public class Manager {
        private final T instance;

        private Manager( T instance ) {
            this.instance = instance;
        }

        /**
         * Call the method.
         *
         * @param values The arguments.
         * @return The returned value.
         */
        public R call( Object... values ) {
            return MethodAccessor.this.call( instance, values );
        }
    }
}
