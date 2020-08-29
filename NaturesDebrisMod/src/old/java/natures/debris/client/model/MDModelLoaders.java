/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.model;

import natures.debris.client.model.bush.BushModelLoader;
import natures.debris.client.model.empty.EmptyModelLoader;
import natures.debris.client.model.farmland.FarmlandModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public final class MDModelLoaders {
    private MDModelLoaders() {
    }

    public static void register() {
        ModelLoaderRegistry.registerLoader(new ResourceLocation("modernity:empty"), new EmptyModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation("modernity:bush"), new BushModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation("modernity:farmland"), new FarmlandModelLoader());
    }
}