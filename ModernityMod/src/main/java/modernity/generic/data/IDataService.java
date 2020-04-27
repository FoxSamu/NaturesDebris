/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.data;

import modernity.common.block.loot.IBlockDrops;
import modernity.generic.IModernityAPI;
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
