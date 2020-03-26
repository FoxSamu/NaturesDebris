/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 17 - 2020
 * Author: rgsw
 */

package modernity.client.colors.deserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import modernity.client.colors.ColorDeserializeContext;
import modernity.client.colors.ColorDeserializeException;
import modernity.client.colors.IColorProvider;
import modernity.client.colors.IColorProviderDeserializer;
import modernity.client.colors.provider.HeightmapColorProvider;

// TODO Re-evaluate
//public class HeightmapProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonObject() ) {
//            JsonObject obj = element.getAsJsonObject();
//            if( ! obj.has( "above" ) ) throw ctx.exception( "Missing required 'above'" );
//            if( ! obj.has( "below" ) ) throw ctx.exception( "Missing required 'below'" );
//            if( ! obj.has( "type" ) ) throw ctx.exception( "Missing required 'type'" );
//
//            IColorProvider above = ctx.silentDeserialize( obj.get( "above" ), "above" );
//            IColorProvider below = ctx.silentDeserialize( obj.get( "below" ), "below" );
//
//            String type = obj.get( "type" ).getAsString();
//
//            HeightmapColorProvider.HeightProvider provider;
//            try {
//                provider = HeightmapColorProvider.HeightProvider.valueOf( type.toUpperCase() );
//            } catch( IllegalArgumentException exc ) {
//                throw ctx.exception( "'" + type + "' is not a valid height provider" );
//            }
//
//            int lo = 1, hi = 1;
//
//            if( obj.has( "range" ) && obj.get( "range" ).isJsonPrimitive() ) {
//                int s = obj.get( "range" ).getAsInt();
//                lo = - s / 2;
//                hi = s / 2;
//            }
//
//            if( obj.has( "offset" ) && obj.get( "offset" ).isJsonPrimitive() ) {
//                int s = obj.get( "offset" ).getAsInt();
//                lo += s;
//                hi += s;
//            }
//
//            if( obj.has( "low" ) && obj.get( "low" ).isJsonPrimitive() ) {
//                lo = obj.get( "low" ).getAsInt();
//            }
//
//            if( obj.has( "high" ) && obj.get( "high" ).isJsonPrimitive() ) {
//                hi = obj.get( "high" ).getAsInt();
//            }
//
//            return new HeightmapColorProvider( lo, hi, above, below, provider );
//        } else {
//            throw ctx.exception( "Expected an object" );
//        }
//    }
//}
