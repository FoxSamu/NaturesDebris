/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.event;

import com.google.common.reflect.TypeToken;
import modernity.common.event.impl.*;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;

/**
 * Holder class for Modernity block events.
 */
public final class MDBlockEvents {
    private static final RegistryHandler<BlockEvent<?>> ENTRIES = new RegistryHandler<>( "modernity", true );

    public static final LeavesDecayEvent LEAVES_DECAY = register( "leaves_decay", new LeavesDecayEvent() );
    public static final PlaceEyeEvent PLACE_EYE = register( "place_eye", new PlaceEyeEvent() );
    public static final BreakEyeEvent BREAK_EYE = register( "break_eye", new BreakEyeEvent() );
    public static final ShadeBlueTeleportEvent SHADE_BLUE_TELEPORT = register( "shade_blue_teleport", new ShadeBlueTeleportEvent() );
    public static final TorchExtinguishEvent TORCH_EXTINGUISH = register( "torch_extinguish", new TorchExtinguishEvent() );
    public static final PlantGrowEvent PLANT_GROW = register( "plant_grow", new PlantGrowEvent() );

    private static <T extends BlockEvent<?>> T register( String id, T event ) {
        return ENTRIES.register( id, event );
    }

    /**
     * Adds the registry handler to the {@link RegistryEventHandler}. Must be called internally only.
     */
    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BlockEvent<?>> token = new TypeToken<BlockEvent<?>>() {
        };
        handler.addHandler( (Class<BlockEvent<?>>) token.getRawType(), ENTRIES );
    }

    private MDBlockEvents() {
    }
}
