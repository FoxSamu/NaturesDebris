/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model.wrapping;

import com.google.common.collect.Maps;
import modernity.client.model.ModernityJSONModelLoader;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ForgeBlockStateV1;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WrappingModelLoader extends ModernityJSONModelLoader {
    public static final WrappingModelLoader INSTANCE = new WrappingModelLoader();

    private static final Gson GSON = new GsonBuilder()
                                         .registerTypeAdapter( TRSRTransformation.class, ForgeBlockStateV1.TRSRDeserializer.INSTANCE )
                                         .create();

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

        Optional<IModelState> modelState = Optional.empty();

        if( json.has( "transform" ) ) {
            if( json.get( "transform" ).isJsonPrimitive() && json.get( "transform" ).getAsJsonPrimitive().isString() ) {
                String transform = json.get( "transform" ).getAsString();
                modelState = ForgeBlockStateV1.Transforms.get( transform );
                if( ! modelState.isPresent() ) {
                    throw new JsonParseException( "'transform': unknown default string: " + transform );
                }
            } else if( ! json.get( "transform" ).isJsonObject() ) {
                try {
                    TRSRTransformation base = GSON.fromJson( json.get( "transform" ), TRSRTransformation.class );
                    modelState = Optional.of( TRSRTransformation.blockCenterToCorner( base ) );
                } catch( JsonParseException e ) {
                    throw new JsonParseException( "transform: expected a string, object or valid base transformation, got: " + json.get( "transform" ) );
                }
            } else {
                JsonObject transform = json.get( "transform" ).getAsJsonObject();
                EnumMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = Maps.newEnumMap( ItemCameraTransforms.TransformType.class );
                if( transform.has( "thirdperson" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "thirdperson" ), TRSRTransformation.class );
                    transform.remove( "thirdperson" );
                    transforms.put( ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "thirdperson_righthand" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "thirdperson_righthand" ), TRSRTransformation.class );
                    transform.remove( "thirdperson_righthand" );
                    transforms.put( ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "thirdperson_lefthand" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "thirdperson_lefthand" ), TRSRTransformation.class );
                    transform.remove( "thirdperson_lefthand" );
                    transforms.put( ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "firstperson" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "firstperson" ), TRSRTransformation.class );
                    transform.remove( "firstperson" );
                    transforms.put( ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "firstperson_righthand" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "firstperson_righthand" ), TRSRTransformation.class );
                    transform.remove( "firstperson_righthand" );
                    transforms.put( ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "firstperson_lefthand" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "firstperson_lefthand" ), TRSRTransformation.class );
                    transform.remove( "firstperson_lefthand" );
                    transforms.put( ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "head" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "head" ), TRSRTransformation.class );
                    transform.remove( "head" );
                    transforms.put( ItemCameraTransforms.TransformType.HEAD, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "gui" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "gui" ), TRSRTransformation.class );
                    transform.remove( "gui" );
                    transforms.put( ItemCameraTransforms.TransformType.GUI, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "ground" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "ground" ), TRSRTransformation.class );
                    transform.remove( "ground" );
                    transforms.put( ItemCameraTransforms.TransformType.GROUND, TRSRTransformation.blockCenterToCorner( t ) );
                }
                if( transform.has( "fixed" ) ) {
                    TRSRTransformation t = GSON.fromJson( transform.get( "fixed" ), TRSRTransformation.class );
                    transform.remove( "fixed" );
                    transforms.put( ItemCameraTransforms.TransformType.FIXED, TRSRTransformation.blockCenterToCorner( t ) );
                }
                int k = transform.entrySet().size();
                if( transform.has( "matrix" ) ) k--;
                if( transform.has( "translation" ) ) k--;
                if( transform.has( "rotation" ) ) k--;
                if( transform.has( "scale" ) ) k--;
                if( transform.has( "post-rotation" ) ) k--;
                if( k > 0 ) {
                    throw new JsonParseException( "'transform': allowed keys: 'thirdperson', 'firstperson', 'gui', 'head', 'matrix', 'translation', 'rotation', 'scale', 'post-rotation'" );
                }
                TRSRTransformation base = TRSRTransformation.identity();
                if( ! transform.entrySet().isEmpty() ) {
                    base = GSON.fromJson( transform, TRSRTransformation.class );
                    base = TRSRTransformation.blockCenterToCorner( base );
                }
                IModelState state;
                if( transforms.isEmpty() ) {
                    state = base;
                } else {
                    state = new SimpleModelState( Maps.immutableEnumMap( transforms ), Optional.of( base ) );
                }
                modelState = Optional.of( state );
            }
        }



        return new WrappingModel( model, custom, textures, gui3d, smooth, modelState );
    }
}
