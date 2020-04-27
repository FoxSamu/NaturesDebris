/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.event;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Holds a type of environment event, to be registered to the {@linkplain IForgeRegistry registry} and to create event
 * instances.
 */
public final class EnvironmentEventType extends ForgeRegistryEntry<EnvironmentEventType> {

    private final Function<EnvironmentEventManager, EnvironmentEvent> factory;
    private final BiConsumer<ArrayList<ArgumentBuilder<CommandSource, ?>>, EnvironmentEventType> commandFactory;

    @Nullable
    private String translationKey;

    /**
     * Creates an environment event type.
     *
     * @param factory        Function for creating new event instances.
     * @param commandFactory Function for setting up the command for this event.
     */
    public EnvironmentEventType( Function<EnvironmentEventManager, EnvironmentEvent> factory, BiConsumer<ArrayList<ArgumentBuilder<CommandSource, ?>>, EnvironmentEventType> commandFactory ) {
        this.factory = factory;
        this.commandFactory = commandFactory;
    }

    /**
     * Creates a new environment event instance for the specified event manager.
     */
    public EnvironmentEvent createEvent( EnvironmentEventManager manager ) {
        return factory.apply( manager );
    }

    public void buildCommand( ArrayList<ArgumentBuilder<CommandSource, ?>> list ) {
        commandFactory.accept( list, this );
    }

    public String getTranslationKey() {
        if( translationKey == null ) {
            translationKey = Util.makeTranslationKey( "envevent", getRegistryName() );
        }
        return translationKey;
    }
}
