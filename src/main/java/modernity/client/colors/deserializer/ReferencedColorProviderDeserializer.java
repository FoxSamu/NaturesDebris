/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.deserializer;

import com.google.gson.JsonElement;
import modernity.client.colors.ColorDeserializeContext;
import modernity.client.colors.ColorDeserializeException;
import modernity.client.colors.IColorProvider;
import modernity.client.colors.IColorProviderDeserializer;
import modernity.client.colors.provider.ReferencedColorProvider;
import net.minecraft.util.ResourceLocation;

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
