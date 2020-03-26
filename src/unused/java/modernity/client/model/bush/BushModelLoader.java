/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model.bush;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

public class BushModelLoader implements ICustomModelLoader {
    @Override
    public void onResourceManagerReload( IResourceManager resourceManager ) {

    }

    @Override
    public boolean accepts( ResourceLocation loc ) {
        return loc.getNamespace().equals( "modernity" ) && loc.getPath().endsWith( "block/dynamic_bush" );
    }

    @Override
    public IUnbakedModel loadModel( ResourceLocation loc ) {
        return new BushModel( null );
    }
}
