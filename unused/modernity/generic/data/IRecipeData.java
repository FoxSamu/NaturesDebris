/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.generic.data;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

@FunctionalInterface
public interface IRecipeData {
    void build( Consumer<IFinishedRecipe> consumer );
}
