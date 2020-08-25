/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.recipes;

import com.google.common.reflect.TypeToken;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.item.crafting.IRecipeSerializer;

public final class MDRecipeSerializers {
    private static final RegistryHandler<IRecipeSerializer<?>> ENTRIES = new RegistryHandler<>("natures/debris");

    private MDRecipeSerializers() {
    }

    private static <T extends IRecipeSerializer<?>> T register(String id, T serializer) {
        return ENTRIES.register(id, serializer);
    }

    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<IRecipeSerializer<?>> token = new TypeToken<IRecipeSerializer<?>>() {
        };
        handler.addHandler((Class<IRecipeSerializer<?>>) token.getRawType(), ENTRIES);
    }
}
