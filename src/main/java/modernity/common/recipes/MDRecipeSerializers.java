/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.recipes;

import com.google.common.reflect.TypeToken;
import modernity.common.fluid.MDFluids;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.item.crafting.IRecipeSerializer;

public final class MDRecipeSerializers {
    private static final RegistryHandler<IRecipeSerializer<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final CleaningRecipeSerializer CLEANING = register( "cleaning", new CleaningRecipeSerializer( 200, MDFluids.MURKY_WATER, 0.25F ) );

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
