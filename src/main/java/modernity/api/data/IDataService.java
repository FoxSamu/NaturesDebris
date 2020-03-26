/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.api.data;

import modernity.api.IModernityAPI;

public interface IDataService extends IModernityAPI {
    //    void addBlockDrops( Block block, IBlockDrops drops ); // TODO Re-evaluate
    void addRecipe( IRecipeData recipe );

    IDataService NONE = new IDataService() {
//        @Override
//        public void addBlockDrops( Block block, IBlockDrops drops ) {
//        }

        @Override
        public void addRecipe( IRecipeData recipe ) {
        }

        @Override
        public boolean available() {
            return false;
        }
    };
}
