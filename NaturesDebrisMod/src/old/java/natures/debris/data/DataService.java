/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.data;

import natures.debris.common.blockold.loot.IBlockDrops;
import natures.debris.data.loot.BlockLootData;
import natures.debris.data.recipes.RecipeData;
import natures.debris.generic.data.IDataService;
import natures.debris.generic.data.IRecipeData;
import net.minecraft.block.Block;

public class DataService implements IDataService {
    @Override
    public void addBlockDrops(Block block, IBlockDrops drops) {
        BlockLootData.addBlock(block, drops);
    }

    @Override
    public void addRecipe(IRecipeData recipe) {
        RecipeData.addRecipe(recipe);
    }

    @Override
    public boolean available() {
        return true;
    }
}
