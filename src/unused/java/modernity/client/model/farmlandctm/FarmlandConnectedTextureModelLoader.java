/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.client.model.farmlandctm;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

public class FarmlandConnectedTextureModelLoader implements ICustomModelLoader {
    @Override
    public void onResourceManagerReload( IResourceManager resourceManager ) {

    }

    @Override
    public boolean accepts( ResourceLocation modelLocation ) {
        return modelLocation.getNamespace().equals( "modernity" ) &&
                   modelLocation.getPath().equals( "models/block/farmland_ctm" );
    }

    @Override
    public IUnbakedModel loadModel( ResourceLocation modelLocation ) {
        return new FarmlandConnectedTextureModel();
    }
}
