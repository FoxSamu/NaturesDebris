/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.plant.SimplePlantBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MushroomGrowLogic extends AbstractSpreadingGrowLogic {
    private final SimplePlantBlock plant;

    public MushroomGrowLogic( SimplePlantBlock plant ) {
        this.plant = plant;
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return false;
    }

    @Override
    protected boolean checkResources( IFarmlandLogic logic, Random rand ) {
        if( logic == null ) return false;
        if( ! logic.isWet() ) return false;
        double chance = 1 / 24D;
        if( logic.isDecayed() ) {
            double decay = (double) logic.getDecay() / logic.getMaxDecay();
            chance += 1 / 6D * decay;
        }

        return rand.nextDouble() < chance;
    }

    @Override
    protected void consumeResources( IFarmlandLogic logic, Random rand ) {
        if( logic == null ) return;
        if( logic.isDecayed() && rand.nextInt( logic.getMaxDecay() - logic.getDecay() + 1 ) == 0 ) {
            logic.undecay( rand.nextInt( 2 ) + 1 );
        }
    }

    @Override
    protected boolean canPlacePlant( World world, BlockPos pos, Random rand ) {
        return plant.canGenerateAt( world, pos, world.getBlockState( pos ) );
    }

    @Override
    protected void placePlant( World world, BlockPos pos, Random rand ) {
        plant.growAt( world, pos );
    }
}
