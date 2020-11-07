package natures.debris.core.util;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

public class StateUtil {
    public static <T extends Comparable<T>> Optional<T> getOptional(BlockState state, Property<T> prop) {
        return state.method_28500(prop);
    }

    public static <T extends Comparable<T>> T getOrNull(BlockState state, Property<T> prop) {
        return getOptional(state, prop).orElse(null);
    }

    public static <T extends Comparable<T>> T getOrDefault(BlockState state, Property<T> prop, T def) {
        return getOptional(state, prop).orElse(def);
    }

    public static <T extends Comparable<T>> BlockState withIfHaving(BlockState state, Property<T> prop, T val) {
        return state.contains(prop) ? state.with(prop, val) : state;
    }
}
