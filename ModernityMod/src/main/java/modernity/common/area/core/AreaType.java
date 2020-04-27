/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.area.core;

import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.Validate;

import java.util.function.BiFunction;

public class AreaType<T extends Area> extends ForgeRegistryEntry<AreaType<?>> {
    private final BiFunction<World, AreaBox, T> factory;

    public final int updateInterval;

    public AreaType( BiFunction<World, AreaBox, T> factory, int updateInterval ) {
        Validate.isTrue( updateInterval >= 0, "updateInterval < 0" );
        this.factory = factory;
        this.updateInterval = updateInterval;
    }

    public Area create( World world, AreaBox box ) {
        return factory.apply( world, box );
    }
}
