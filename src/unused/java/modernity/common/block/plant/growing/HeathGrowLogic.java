/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.PlantBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class HeathGrowLogic extends SpreadingGrowLogic {
    public HeathGrowLogic( PlantBlock plant ) {
        super( plant );
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return item.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        if( logic.isDecayed() ) {
            return GrowType.NONE;
        }
        int chance = 2;
        if( logic.isWet() ) {
            chance = 6;
        }
        return rand.nextInt( 16 ) < chance ? GrowType.GROW : GrowType.NONE;
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }
}
