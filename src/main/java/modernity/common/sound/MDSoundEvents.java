/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.sound;

import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public final class MDSoundEvents {
    private static final RegistryHandler<SoundEvent> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final SoundEvent BLOCK_SHADE_BLUE_BREAK = register( "block.shade_blue.break" );
    public static final SoundEvent BLOCK_SHADE_BLUE_PLACE = register( "block.shade_blue.place" );
    public static final SoundEvent BLOCK_SHADE_BLUE_TELEPORT = register( "block.shade_blue.teleport" );

    private static SoundEvent register( String name ) {
        SoundEvent event = new SoundEvent( new ResourceLocation( "modernity", name ) );
        return ENTRIES.register( name, event );
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( SoundEvent.class, ENTRIES );
    }

    private MDSoundEvents() {
    }
}
