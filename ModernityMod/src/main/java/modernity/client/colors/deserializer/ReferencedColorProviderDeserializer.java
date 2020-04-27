/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.deserializer;

// TODO Re-evaluate
//public class ReferencedColorProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonPrimitive() && element.getAsJsonPrimitive().isString() ) {
//            String id = element.getAsString();
//
//            ResourceLocation loc = ctx.resLoc( id );
//
//            return new ReferencedColorProvider( loc );
//        } else {
//            throw ctx.exception( "Expected a string" );
//        }
//    }
//}
