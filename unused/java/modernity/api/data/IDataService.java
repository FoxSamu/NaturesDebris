/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.generic.data;

import modernity.generic.IModernityAPI;
import modernity.common.block.loot.IBlockDrops;
import net.minecraft.block.Block;

public interface IDataService extends IModernityAPI {
    void addBlockDrops( Block block, IBlockDrops drops );
    void addRecipe( IRecipeData recipe );

    IDataService NONE = new IDataService() {
        @Override
        public void addBlockDrops( Block block, IBlockDrops drops ) {
        }

        @Override
        public void addRecipe( IRecipeData recipe ) {
        }

        @Override
        public boolean available() {
            return false;
        }
    };
}
