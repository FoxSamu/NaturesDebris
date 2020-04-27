/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.data;

import modernity.common.block.loot.IBlockDrops;
import modernity.data.loot.BlockLootData;
import modernity.data.recipes.RecipeData;
import modernity.generic.data.IDataService;
import modernity.generic.data.IRecipeData;
import net.minecraft.block.Block;

public class DataService implements IDataService {
    @Override
    public void addBlockDrops( Block block, IBlockDrops drops ) {
        BlockLootData.addBlock( block, drops );
    }

    @Override
    public void addRecipe( IRecipeData recipe ) {
        RecipeData.addRecipe( recipe );
    }

    @Override
    public boolean available() {
        return true;
    }
}
