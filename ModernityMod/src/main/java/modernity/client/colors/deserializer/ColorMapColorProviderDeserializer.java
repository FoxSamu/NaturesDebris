/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.deserializer;

// TODO Re-evaluate
//public class ColorMapColorProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonObject() ) {
//            JsonObject obj = element.getAsJsonObject();
//            if( ! obj.has( "texture" ) ) throw ctx.exception( "Missing required 'texture'" );
//            if( ! obj.get( "texture" ).isJsonPrimitive() ) throw ctx.exception( "'texture' must be a string" );
//            if( ! obj.getAsJsonPrimitive( "texture" ).isString() ) throw ctx.exception( "'texture' must be a string" );
//
//            if( ! obj.has( "x" ) ) throw ctx.exception( "Missing required 'x'" );
//            if( ! obj.get( "x" ).isJsonPrimitive() ) throw ctx.exception( "'x' must be a number" );
//            if( ! obj.getAsJsonPrimitive( "x" ).isNumber() ) throw ctx.exception( "'x' must be a number" );
//
//            if( ! obj.has( "y" ) ) throw ctx.exception( "Missing required 'y'" );
//            if( ! obj.get( "y" ).isJsonPrimitive() ) throw ctx.exception( "'y' must be a number" );
//            if( ! obj.getAsJsonPrimitive( "y" ).isNumber() ) throw ctx.exception( "'y' must be a number" );
//
//            ResourceLocation loc = ctx.resLoc( obj.get( "texture" ).getAsString() );
//            float x = obj.get( "x" ).getAsFloat();
//            float y = obj.get( "y" ).getAsFloat();
//
//            IColorProvider fallback = ErrorColorProvider.INSTANCE;
//
//            if( obj.has( "fallback" ) ) {
//                fallback = ctx.deserialize( obj.get( "fallback" ) );
//            }
//
//            return new ColormapColorProvider( loc, x, y, fallback );
//        } else {
//            throw ctx.exception( "Expected an object" );
//        }
//    }
//}
