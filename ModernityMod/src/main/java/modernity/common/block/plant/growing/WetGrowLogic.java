/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.PlantBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class WetGrowLogic extends SpreadingGrowLogic {
    public WetGrowLogic( PlantBlock plant ) {
        super( plant );
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return item.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        if( logic.isDecayed() ) {
            return rand.nextInt( 2 ) == 0 ? GrowType.DIE : GrowType.NONE;
        } else if( logic.isWet() ) {
            return rand.nextInt( 16 ) < 6 ? GrowType.GROW : GrowType.NONE;
        } else {
            return GrowType.NONE;
        }
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }
}
