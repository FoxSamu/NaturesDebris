/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.api.data;

public interface IRecipeDataBuilder extends IRecipeData {
    IRecipeDataBuilder group( String group );
    IRecipeDataBuilder id( String id );
}
