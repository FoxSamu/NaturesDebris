/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.deserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import modernity.client.colors.ColorDeserializeContext;
import modernity.client.colors.ColorDeserializeException;
import modernity.client.colors.IColorProvider;
import modernity.client.colors.IColorProviderDeserializer;
import modernity.client.colors.provider.OpenSimplex2DColorProvider;

// TODO Re-evaluate
//public class OpenSimplex2DColorProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonObject() ) {
//            JsonObject obj = element.getAsJsonObject();
//            if( ! obj.has( "a" ) ) throw ctx.exception( "Missing required 'a'" );
//            if( ! obj.has( "b" ) ) throw ctx.exception( "Missing required 'b'" );
//
//            IColorProvider a = ctx.silentDeserialize( obj.get( "a" ), "a" );
//            IColorProvider b = ctx.silentDeserialize( obj.get( "b" ), "b" );
//
//            int octaves = 1;
//
//            double sizeX = 1, sizeZ = 1;
//
//            if( obj.has( "size" ) && obj.get( "size" ).isJsonPrimitive() ) {
//                double s = obj.get( "size" ).getAsDouble();
//                sizeX = s;
//                sizeZ = s;
//            }
//
//            if( obj.has( "sizeX" ) && obj.get( "sizeX" ).isJsonPrimitive() ) {
//                sizeX = obj.get( "sizeX" ).getAsDouble();
//            }
//
//            if( obj.has( "sizeZ" ) && obj.get( "sizeZ" ).isJsonPrimitive() ) {
//                sizeZ = obj.get( "sizeZ" ).getAsDouble();
//            }
//
//            if( obj.has( "octaves" ) && obj.get( "octaves" ).isJsonPrimitive() ) {
//                octaves = obj.get( "octaves" ).getAsInt();
//            }
//
//            return new OpenSimplex2DColorProvider( a, b, octaves, sizeX, sizeZ );
//        } else {
//            throw ctx.exception( "Expected an object" );
//        }
//    }
//}
