/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.plant.FacingPlantBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class MossGrowLogic implements IGrowLogic {
    private final FacingPlantBlock plant;

    public MossGrowLogic( FacingPlantBlock plant ) {
        this.plant = plant;
    }

    @Override
    public void grow( World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmlandLogic farmland ) {
        for( Direction dir : Direction.values() ) {
            if( rand.nextInt( 16 ) == 0 ) {
                addMoss( world, pos.offset( dir ), rand );
            }
        }
    }

    @Override
    public boolean grow( World world, BlockPos pos, BlockState state, Random rand, ItemStack item ) {
        if( ! item.getItem().isIn( MDItemTags.FERTILIZER ) ) return false;
        boolean grown = false;
        for( Direction dir : Direction.values() ) {
            if( rand.nextInt( 16 ) == 0 ) {
                grown |= addMoss( world, pos.offset( dir ), rand );
            }
        }
        return grown;
    }

    private boolean addMoss( World world, BlockPos pos, Random rand ) {
        if( world.isAirBlock( pos ) ) return false;

        ArrayList<Direction> possibleDirections = new ArrayList<>();
        for( Direction dir : Direction.values() ) {
            BlockPos off = pos.offset( dir, - 1 );

            if( plant.isBlockSideSustainable( world.getBlockState( off ), world, pos, dir ) ) {
                possibleDirections.add( dir );
            }
        }

        if( possibleDirections.isEmpty() ) return false;

        return world.setBlockState( pos, plant.computeStateForPos( world, pos ).with(
            FacingPlantBlock.FACING,
            possibleDirections.get( rand.nextInt( possibleDirections.size() ) )
        ) );
    }
}
