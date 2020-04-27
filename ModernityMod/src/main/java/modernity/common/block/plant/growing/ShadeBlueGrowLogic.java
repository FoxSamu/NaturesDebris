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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ShadeBlueGrowLogic extends SpreadingGrowLogic {
    public ShadeBlueGrowLogic( PlantBlock plant ) {
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
            return rand.nextInt( 12 ) == 0 ? GrowType.GROW : GrowType.NONE;
        } else {
            return GrowType.NONE;
        }
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }

    @Override
    protected boolean grow( World world, BlockPos pos, Random rand, boolean isItem ) {
        for( int i = 0; i < 30; i++ ) {
            int x = rand.nextInt( 8 ) - rand.nextInt( 8 );
            int y = rand.nextInt( 8 ) - rand.nextInt( 8 );
            int z = rand.nextInt( 8 ) - rand.nextInt( 8 );
            if( x == 0 && y == 0 && z == 0 ) continue;

            BlockPos p = pos.add( x, y, z );

            if( canPlacePlant( world, p, rand ) ) {
                placePlant( world, p, rand );
                break;
            }
        }
        return true;
    }
}
