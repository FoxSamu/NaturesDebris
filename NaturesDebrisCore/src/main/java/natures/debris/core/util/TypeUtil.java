/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.core.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for casting objects to other objects.
 */
public final class TypeUtil {

    private TypeUtil() {
    }

    public static <T> T cast(Object o, Class<T> cls) {
        return cls.cast(o);
    }

    public static <T> T castOrNull(Object o, Class<T> cls) {
        return cls.isInstance(o) ? cls.cast(o) : null;
    }

    public static <T> Optional<T> castOptional(Object o, Class<T> cls) {
        return cls.isInstance(o) ? Optional.of(cls.cast(o)) : Optional.empty();
    }

    public static <T> T castOrElse(Object o, Class<T> cls, Supplier<T> orElse) {
        return cls.isInstance(o) ? cls.cast(o) : orElse.get();
    }

    public static <T> T castOrElse(Object o, Class<T> cls, T orElse) {
        return cls.isInstance(o) ? cls.cast(o) : orElse;
    }

    public static <T> void invokeAs(Object o, Class<T> cls, Consumer<T> fn) {
        if (cls.isInstance(o))
            fn.accept(cls.cast(o));
    }

    public static <T, U> U invokeOrNull(Object o, Class<T> cls, Function<T, U> fn) {
        return cls.isInstance(o)
               ? fn.apply(cls.cast(o))
               : null;
    }

    public static <T, U> U invokeOrElse(Object o, Class<T> cls, Function<T, U> fn, Supplier<U> orElse) {
        return cls.isInstance(o) ? fn.apply(cls.cast(o)) : orElse.get();
    }

    public static <T, U> U invokeOrElse(Object o, Class<T> cls, Function<T, U> fn, U orElse) {
        return cls.isInstance(o) ? fn.apply(cls.cast(o)) : orElse;
    }

    public static <T, U> Optional<U> invokeOptional(Object o, Class<T> cls, Function<T, U> fn) {
        return cls.isInstance(o) ? Optional.of(fn.apply(cls.cast(o))) : Optional.empty();
    }
}
