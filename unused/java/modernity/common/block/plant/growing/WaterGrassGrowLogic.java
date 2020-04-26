/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.TallDirectionalPlantBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WaterGrassGrowLogic extends TallPlantGrowLogic {
    public WaterGrassGrowLogic( TallDirectionalPlantBlock plant ) {
        super( plant );
    }

    @Override
    protected GrowDirection decideGrowDir( World world, BlockPos pos, int height, Random rand, boolean isItem ) {
        if( isItem ) {
            if( height == 8 ) return GrowDirection.NONE;
            return GrowDirection.UP;
        } else {
            boolean up = rand.nextInt( 3 ) == 0;
            if( up ) {
                if( height >= 4 ) return GrowDirection.NONE;
                if( height == 3 ) return rand.nextInt( 9 ) == 0 ? GrowDirection.UP : GrowDirection.NONE;
                if( height == 2 ) return rand.nextInt( 4 ) == 0 ? GrowDirection.UP : GrowDirection.NONE;
                return GrowDirection.UP;
            } else {
                return GrowDirection.SIDEWARDS;
            }
        }
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return item.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        return GrowType.NONE;
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }
}
