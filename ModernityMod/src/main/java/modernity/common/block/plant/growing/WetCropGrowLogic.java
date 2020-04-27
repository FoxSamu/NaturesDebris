/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.CropBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class WetCropGrowLogic extends CropGrowLogic {
    public WetCropGrowLogic( CropBlock crop ) {
        super( crop );
    }

    @Override
    protected boolean isFertilizer( ItemStack stack, Random rand ) {
        return stack.getItem().isIn( MDItemTags.FERTILIZER ) || stack.getItem().isIn( MDItemTags.LITTLE_FERTILIZER );
    }

    @Override
    protected int getItemGrow( ItemStack stack, Random rand ) {
        if( stack.getItem().isIn( MDItemTags.FERTILIZER ) ) {
            return 1 + rand.nextInt( 3 );
        }
        if( stack.getItem().isIn( MDItemTags.LITTLE_FERTILIZER ) ) {
            return rand.nextInt( 2 );
        }
        return 0;
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        if( logic.isDecayed() ) {
            return rand.nextInt( 2 ) == 0 ? GrowType.DIE : GrowType.NONE;
        } else if( logic.isWet() ) {
            return GrowType.GROW;
        } else {
            return GrowType.NONE;
        }
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }
}
