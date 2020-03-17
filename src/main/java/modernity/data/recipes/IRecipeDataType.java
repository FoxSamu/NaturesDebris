/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
 * Author: rgsw
 */

package modernity.data.recipes;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

@FunctionalInterface
public interface IRecipeDataType {
    void build( Consumer<IFinishedRecipe> consumer );
}
