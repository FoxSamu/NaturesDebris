/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import modernity.client.colors.deserializer.HSVColorProviderDeserializer;
import modernity.client.colors.deserializer.RGBColorProviderDeserializer;
import modernity.client.colors.provider.ErrorColorProvider;
import modernity.client.colors.provider.ReferencedColorProvider;
import modernity.client.colors.provider.SolidColorProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;

public class ColorDeserializeContext {
    private final Stack<String> path = new Stack<>();

    private final List<ColorDeserializeException> excs = new ArrayList<>();

    public IColorProvider silentDeserialize( JsonElement element, String subLevel ) {
        pushID( subLevel );
        IColorProvider provider;
        try {
            provider = deserialize( element );
        } catch( ColorDeserializeException exc ) {
            excs.add( exc );
            provider = ErrorColorProvider.INSTANCE;
        }
        popID();
        return provider;
    }

    public IColorProvider deserialize( JsonElement element ) throws ColorDeserializeException {
        if( element.isJsonPrimitive() ) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if( primitive.isString() && ! primitive.getAsString().startsWith( "#" ) ) {
                return new ReferencedColorProvider( resLoc( primitive.getAsString() ) );
            } else {
                if( primitive.isString() )
                    return RGBColorProviderDeserializer.parseHex( primitive.getAsString(), this );
                if( primitive.isNumber() )
                    return RGBColorProviderDeserializer.parseNumber( primitive.getAsDouble(), this );
                if( primitive.isBoolean() )
                    return RGBColorProviderDeserializer.parseBoolean( primitive.getAsBoolean() );
                throw exception( "Invalid json type" );
            }
        } else if( element.isJsonArray() ) {
            JsonArray array = element.getAsJsonArray();
            if( array.size() < 3 ) throw exception( "Color array must have at least 3 elements" );
            if( array.size() == 3 ) return RGBColorProviderDeserializer.parseArray( array, this, 0 );
            JsonElement first = array.get( 0 );
            if( first.isJsonPrimitive() && first.getAsJsonPrimitive().isString() ) {
                String type = first.getAsString();
                if( ! type.equals( "rgb" ) && ! type.equals( "hsv" ) ) {
                    throw exception( "Color array type must be 'rgb' or 'hsv'" );
                } else if( type.equals( "rgb" ) ) {
                    return RGBColorProviderDeserializer.parseArray( array, this, 1 );
                } else {
                    return HSVColorProviderDeserializer.parseArray( array, this, 1 );
                }
            } else {
                return RGBColorProviderDeserializer.parseArray( array, this, 0 );
            }
        } else if( element.isJsonObject() ) {
            JsonObject obj = element.getAsJsonObject();
            if( obj.has( "red" ) && obj.has( "green" ) && obj.has( "blue" ) ) {
                return RGBColorProviderDeserializer.parseObject( obj, "red", "green", "blue", this );
            }
            if( obj.has( "r" ) && obj.has( "g" ) && obj.has( "b" ) ) {
                return RGBColorProviderDeserializer.parseObject( obj, "r", "g", "b", this );
            }
            if( obj.has( "hue" ) && obj.has( "saturation" ) && obj.has( "value" ) ) {
                return HSVColorProviderDeserializer.parseObject( obj, "hue", "saturation", "value", this );
            }
            if( obj.has( "h" ) && obj.has( "s" ) && obj.has( "v" ) ) {
                return HSVColorProviderDeserializer.parseObject( obj, "hue", "saturation", "value", this );
            }

            Iterator<Map.Entry<String, JsonElement>> iterator = obj.entrySet().iterator();
            if( ! iterator.hasNext() ) {
                throw exception( "Object must at least contain one element" );
            }
            Map.Entry<String, JsonElement> elm = iterator.next();
            return deserialize( elm.getValue(), elm.getKey() );
        } else if( element.isJsonNull() ) {
            return new SolidColorProvider( 0 );
        } else {
            throw exception( "Invalid json type" );
        }
    }

    public IColorProvider deserialize( JsonElement element, String id ) {
        ResourceLocation res = silentResLoc( id );
        if( res == null ) return ErrorColorProvider.INSTANCE;
        IColorProviderDeserializer deserializer = ColorProviderRegistry.deserializer( res );
        if( deserializer == null ) {
            excs.add( exception( "No such color provider '" + id + "'" ) );
            return ErrorColorProvider.INSTANCE;
        }
        path.push( id );
        IColorProvider provider;
        try {
            provider = deserializer.deserialize( element, this );
        } catch( ColorDeserializeException exc ) {
            excs.add( exc );
            provider = ErrorColorProvider.INSTANCE;
        }
        path.pop();
        return provider;
    }

    public void pushID( String id ) {
        path.push( id );
    }

    public void popID() {
        path.pop();
    }

    private ResourceLocation silentResLoc( String id ) {
        try {
            return new ResourceLocation( id );
        } catch( ResourceLocationException exc ) {
            excs.add( exception( "Malformed resource location '" + id + "': " + exc.getMessage(), exc ) );
            return null;
        }
    }

    public ResourceLocation resLoc( String id ) throws ColorDeserializeException {
        try {
            return new ResourceLocation( id );
        } catch( ResourceLocationException exc ) {
            throw exception( "Malformed resource location '" + id + "': " + exc.getMessage(), exc );
        }
    }

    public ColorDeserializeException exception( String message ) {
        return exception( message, null );
    }

    public ColorDeserializeException exception( Throwable cause ) {
        return exception( cause.getMessage(), cause );
    }

    public ColorDeserializeException exception( String message, Throwable cause ) {
        StringBuilder msg = new StringBuilder();
        boolean arrow = false;
        msg.append( "[" );
        for( String s : path ) {
            if( ! arrow ) arrow = true;
            else msg.append( " > " );
            msg.append( s );
        }

        msg.append( "] " );
        msg.append( message );
        return new ColorDeserializeException( msg.toString(), cause );
    }

    public List<ColorDeserializeException> getExcs() {
        return excs;
    }
}
