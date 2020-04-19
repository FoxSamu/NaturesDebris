/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model;

import modernity.client.model.bushold.BushModelLoader;
import modernity.client.model.empty.EmptyModelLoader;
import modernity.client.model.farmland.FarmlandConnectedTextureModelLoader;
import modernity.client.model.merged.MergedModelLoader;
import modernity.client.model.wrapping.WrappingModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public final class MDModelLoaders {
    private MDModelLoaders() {
    }

    public static void register() {
        ModelLoaderRegistry.registerLoader( new FarmlandConnectedTextureModelLoader() );
        ModelLoaderRegistry.registerLoader( new BushModelLoader() );
        ModelLoaderRegistry.registerLoader( new WrappingModelLoader() );
        ModelLoaderRegistry.registerLoader( new MergedModelLoader() );
        ModelLoaderRegistry.registerLoader( new EmptyModelLoader() );
    }
}
