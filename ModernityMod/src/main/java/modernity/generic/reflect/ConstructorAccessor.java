/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.generic.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Wraps a {@link Constructor} and suppresses any exceptions using {@link RuntimeException}s upon usage.
 *
 * @author RGSW
 */
public class ConstructorAccessor<R> {
    private final Constructor<? extends R> constructor;

    /**
     * Wraps a {@link Constructor} instance
     */
    public ConstructorAccessor( Constructor<? extends R> constructor ) {
        this.constructor = constructor;
        constructor.setAccessible( true );
    }

    /**
     * Loads a constructor from a class
     *
     * @param cls    The class to load from
     * @param params The parameter classes
     */
    public ConstructorAccessor( Class<? extends R> cls, Class<?>... params ) {
        this.constructor = ObfuscationReflectionHelper.findConstructor( cls, params );
        constructor.setAccessible( true );
    }

    /**
     * Creates a new instance of the constructor's class
     *
     * @param values The parameters to pass to the constructor
     */
    public R create( Object... values ) {
        try {
            return constructor.newInstance( values );
        } catch( IllegalAccessException | InvocationTargetException | InstantiationException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns the wrapping {@link Constructor}.
     */
    public Constructor<? extends R> getConstructor() {
        return constructor;
    }
}
