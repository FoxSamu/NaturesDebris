/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import modernity.client.colors.ColorDeserializeContext;
import modernity.client.colors.ColorDeserializeException;
import modernity.client.colors.IColorProvider;
import modernity.client.colors.IColorProviderDeserializer;
import modernity.client.colors.provider.RandomNoise2DColorProvider;

public class Random2DColorProviderDeserializer implements IColorProviderDeserializer {
    @Override
    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
        if( element.isJsonArray() ) {
            JsonArray array = element.getAsJsonArray();
            return new RandomNoise2DColorProvider( parseArray( array, ctx ), 1, 1 );
        } else if( element.isJsonObject() ) {
            JsonObject obj = element.getAsJsonObject();
            if( ! obj.has( "entries" ) || ! obj.get( "entries" ).isJsonArray() )
                throw ctx.exception( "Missing required 'entries' array" );

            IColorProvider[] entries = parseArray( obj.getAsJsonArray( "entries" ), ctx );

            int sizeX = 1, sizeZ = 1;

            if( obj.has( "size" ) && obj.get( "size" ).isJsonPrimitive() ) {
                int s = obj.get( "size" ).getAsInt();
                sizeX = s;
                sizeZ = s;
            }

            if( obj.has( "sizeX" ) && obj.get( "sizeX" ).isJsonPrimitive() ) {
                sizeX = obj.get( "sizeX" ).getAsInt();
            }

            if( obj.has( "sizeZ" ) && obj.get( "sizeZ" ).isJsonPrimitive() ) {
                sizeZ = obj.get( "sizeZ" ).getAsInt();
            }

            return new RandomNoise2DColorProvider( entries, sizeX, sizeZ );
        } else {
            throw ctx.exception( "Expected an object or array" );
        }
    }

    private static IColorProvider[] parseArray( JsonArray array, ColorDeserializeContext ctx ) throws ColorDeserializeException {
        if( array.size() == 0 ) {
            throw ctx.exception( "Required at least one entry to pick from" );
        }

        IColorProvider[] providers = new IColorProvider[ array.size() ];
        int i = 0;
        for( JsonElement element : array ) {
            providers[ i ] = ctx.silentDeserialize( element, "" + i );
        }
        return providers;
    }
}