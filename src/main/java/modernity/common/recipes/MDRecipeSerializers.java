/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.recipes;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.item.crafting.IRecipeSerializer;

public final class MDRecipeSerializers {
    private static final RegistryHandler<IRecipeSerializer<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    private static <T extends IRecipeSerializer<?>> T register( String id, T serializer ) {
        return ENTRIES.register( id, serializer );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<IRecipeSerializer<?>> token = new TypeToken<IRecipeSerializer<?>>() {
        };
        handler.addHandler( (Class<IRecipeSerializer<?>>) token.getRawType(), ENTRIES );
    }

    private MDRecipeSerializers() {
    }
}
