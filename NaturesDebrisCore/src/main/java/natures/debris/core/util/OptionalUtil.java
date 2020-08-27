/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.core.util;

import java.util.Map;
import java.util.Optional;

public final class OptionalUtil {
    private OptionalUtil() {
    }

    public static <T> Optional<T> cast(Object o, Class<T> cls) {
        if (cls.isInstance(o))
            return Optional.of(cls.cast(o));
        return Optional.empty();
    }

    public static <K, V> Optional<V> mapGet(Map<K, V> map, K key) {
        return Optional.ofNullable(map.get(key));
    }

    public static <E extends Enum<E>> Optional<E> enumByName(Class<E> cls, String name) {
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
        return opt.orElse(null);
    }
}
