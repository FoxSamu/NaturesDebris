/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.colors.deserializer;

// TODO Re-evaluate
//public class HSVColorProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonArray() ) {
//            JsonArray array = element.getAsJsonArray();
//            return parseArray( array, ctx, 0 );
//        } else if( element.isJsonPrimitive() ) {
//            throw ctx.exception( "HSV cannot be a primitive type" );
//        } else if( element.isJsonNull() ) {
//            throw ctx.exception( "HSV cannot be nulll" );
//        } else if( element.isJsonObject() ) {
//            JsonObject object = new JsonObject();
//            if( object.has( "hue" ) && object.has( "saturation" ) && object.has( "value" ) ) {
//                return parseObject( object, "hue", "saturation", "value", ctx );
//            } else if( object.has( "h" ) && object.has( "s" ) && object.has( "v" ) ) {
//                return parseObject( object, "h", "s", "v", ctx );
//            } else {
//                throw ctx.exception( "Object must have either the keys 'h', 's', 'v' or 'hue', 'saturation', 'value'" );
//            }
//        } else {
//            throw ctx.exception( "Invalid json type" );
//        }
//    }
//
//    private static double element( JsonElement element, ColorDeserializeContext ctx, String name, int limit ) throws ColorDeserializeException {
//        if( ! element.isJsonPrimitive() ) throw ctx.exception( name + " must be a number" );
//        if( ! element.getAsJsonPrimitive().isNumber() ) throw ctx.exception( name + " must be a number" );
//        double d = element.getAsJsonPrimitive().getAsDouble();
//        if( d > limit || d < 0 ) throw ctx.exception( name + " not in range 0-" + limit );
//        return d;
//    }
//
//    public static IColorProvider parseArray( JsonArray array, ColorDeserializeContext ctx, int offset ) throws ColorDeserializeException {
//        if( array.size() < 3 + offset )
//            throw ctx.exception( "HSV array must have at least " + ( 3 + offset ) + " elements" );
//        double h = element( array.get( offset ), ctx, "Hue element", 360 );
//        double s = element( array.get( 1 + offset ), ctx, "Saturation element", 100 );
//        double v = element( array.get( 2 + offset ), ctx, "Value element", 100 );
//        return new SolidColorProvider( ColorUtil.hsv( h, s / 100, v / 100 ) );
//    }
//
//    public static IColorProvider parseObject( JsonObject object, String hue, String sat, String val, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        double h = element( object.get( hue ), ctx, "'" + hue + "'", 1 );
//        double s = element( object.get( sat ), ctx, "'" + sat + "'", 1 );
//        double v = element( object.get( val ), ctx, "'" + val + "'", 1 );
//        return new SolidColorProvider( ColorUtil.hsv( h, s, v ) );
//    }
//}
