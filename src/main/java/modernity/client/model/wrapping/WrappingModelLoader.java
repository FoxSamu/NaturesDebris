/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.client.model.wrapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import modernity.client.model.ModernityJSONModelLoader;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WrappingModelLoader extends ModernityJSONModelLoader {
    public WrappingModelLoader() {
        super( "wrapped" );
    }

    @Override
    public IUnbakedModel loadModel( JsonObject json ) throws Exception {
        if( ! json.has( "model" ) ) {
            throw new Exception( "Missing key 'model'" );
        }

        JsonElement modelJSON = json.get( "model" );

        if( ! modelJSON.isJsonPrimitive() || ! modelJSON.getAsJsonPrimitive().isString() )
            throw new Exception( "'model' must be a string" );

        ResourceLocation model = ResourceLocation.tryCreate( modelJSON.getAsString() );


        HashMap<String, String> custom = new HashMap<>();
        if( json.has( "custom" ) ) {

            JsonElement customJSON = json.get( "custom" );

            if( ! customJSON.isJsonObject() )
                throw new Exception( "'custom' must be an object" );

            JsonObject customObj = customJSON.getAsJsonObject();

            for( Map.Entry<String, JsonElement> entry : customObj.entrySet() ) {
                custom.put( entry.getKey(), entry.getValue().toString() );
            }
        }


        HashMap<String, String> textures = new HashMap<>();
        if( json.has( "textures" ) ) {

            JsonElement texturesJSON = json.get( "textures" );

            if( ! texturesJSON.isJsonObject() )
                throw new Exception( "'custom' must be an object" );

            JsonObject texturesObj = texturesJSON.getAsJsonObject();

            for( Map.Entry<String, JsonElement> entry : texturesObj.entrySet() ) {
                textures.put( entry.getKey(), entry.getValue().toString() );
            }
        }


        Boolean gui3d = null;
        if( json.has( "gui3d" ) ) {

            JsonElement gui3dJSON = json.get( "gui3d" );

            if( ! gui3dJSON.isJsonPrimitive() || ! gui3dJSON.getAsJsonPrimitive().isBoolean() )
                throw new Exception( "'gui3d' must be a boolean" );

            gui3d = gui3dJSON.getAsBoolean();
        }


        Boolean smooth = null;
        if( json.has( "smooth_lighting" ) ) {

            JsonElement smoothJSON = json.get( "smooth_lighting" );

            if( ! smoothJSON.isJsonPrimitive() || ! smoothJSON.getAsJsonPrimitive().isBoolean() )
                throw new Exception( "'smooth_lighting' must be a boolean" );

            smooth = smoothJSON.getAsBoolean();
        }
        return new WrappingModel( model, custom, textures, gui3d, smooth );
    }
}
