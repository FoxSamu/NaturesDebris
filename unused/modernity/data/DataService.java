/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.data;

import modernity.generic.data.IDataService;
import modernity.generic.data.IRecipeData;
import modernity.common.block.loot.IBlockDrops;
import modernity.data.loot.BlockLootData;
import modernity.data.recipes.RecipeData;
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
