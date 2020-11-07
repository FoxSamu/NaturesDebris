package natures.debris.common.block.plant;

import net.minecraft.state.Property;

@FunctionalInterface
public interface IStateBuilder {
    <T extends Comparable<T>> void addProperty(Property<T> property, T def);

    default void addProperty(Property<?> property) {
        addProperty(property, null);
    }
}
