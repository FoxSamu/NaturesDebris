/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface IColorProviderDeserializer {
    IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException;
}
