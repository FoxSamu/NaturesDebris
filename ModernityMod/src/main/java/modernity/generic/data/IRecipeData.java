/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.data;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

@FunctionalInterface
public interface IRecipeData {
    void build( Consumer<IFinishedRecipe> consumer );
}
