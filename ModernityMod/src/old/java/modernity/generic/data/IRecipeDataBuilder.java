/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.data;

public interface IRecipeDataBuilder extends IRecipeData {
    IRecipeDataBuilder group(String group);
    IRecipeDataBuilder id(String id);
}
