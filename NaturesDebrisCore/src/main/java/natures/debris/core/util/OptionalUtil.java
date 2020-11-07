/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.core.util;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.shadew.util.contract.Validate;

public final class OptionalUtil {
    private OptionalUtil() {
    }

    public static <T> Optional<T> cast(Object o, Class<T> cls) {
        Validate.notNull(cls, "cls");
        return cls.isInstance(o)
               ? Optional.of(cls.cast(o))
               : Optional.empty();
    }

    public static <K, V> Optional<V> mapGet(Map<K, V> map, K key) {
        Validate.notNull(map, "map");
        return Optional.ofNullable(map.get(key));
    }

    public static <E extends Enum<E>> Optional<E> enumByName(Class<E> cls, String name) {
        Validate.notNull(cls, "cls");
        if (name == null) return Optional.empty();
        E[] consts = cls.getEnumConstants();
        for (E e : consts) {
            if (e.name().equals(name)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<T> onlyIf(T obj, boolean condition) {
        if (condition) Optional.ofNullable(obj);
        return Optional.empty();
    }

    public static <T> T orNull(Optional<T> opt) {
        Validate.notNull(opt, "opt");
        return opt.orElse(null);
    }

    public static <T> T orElse(T t, T def) {
        return t == null ? def : t;
    }

    public static <T> T orElseGet(T t, Supplier<T> def) {
        Validate.notNull(def, "def");
        return t == null ? def.get() : t;
    }

    public static <T, E extends Throwable> T orElseThrow(T t, Supplier<E> err) throws E {
        Validate.notNull(err, "err");
        if (t == null)
            throw err.get();
        return t;
    }

    public static <T, U> U map(T t, Function<? super T, ? extends U> func) {
        Validate.notNull(func, "func");
        return t == null ? null : func.apply(t);
    }

    public static <T> T filter(T t, Predicate<? super T> func) {
        Validate.notNull(func, "func");
        return t == null ? null : func.test(t) ? t : null;
    }

    public static <T> void ifPresent(T t, Consumer<? super T> func) {
        Validate.notNull(func, "func");
        if (t != null)
            func.accept(t);
    }

    public static <T> T notNull(T t) {
        if (t == null)
            throw new NoSuchElementException();
        return t;
    }
}
