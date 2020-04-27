/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.farmland;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.client.model.IModelLoader;

public class FarmlandModelLoader implements IModelLoader<FarmlandModelGeometry> {
    @Override
    public void onResourceManagerReload( IResourceManager resourceManager ) {
    }

    @Override
    public FarmlandModelGeometry read( JsonDeserializationContext ctx, JsonObject obj ) {
        float y = JSONUtils.getFloat( obj, "y", 16 );
        int tint = JSONUtils.getInt( obj, "tintindex", - 1 );
        int uvAngle = JSONUtils.getInt( obj, "uvrotation", 0 );
        return new FarmlandModelGeometry( y, tint, uvAngle );
    }
}
