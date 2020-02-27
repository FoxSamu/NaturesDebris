/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 27 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.plant.FacingPlantBlock;
import net.minecraft.block.BlockState;
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

    private void addMoss( World world, BlockPos pos, Random rand ) {
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        for( Direction dir : Direction.values() ) {
            BlockPos off = pos.offset( dir );

            if( plant.isBlockSideSustainable( world.getBlockState( off ), world, pos, dir.getOpposite() ) ) {
                possibleDirections.add( dir );
            }
        }

        world.setBlockState( pos, plant.computeStateForPos( world, pos ).with(
            FacingPlantBlock.FACING,
            possibleDirections.get( rand.nextInt( possibleDirections.size() ) )
        ) );
    }
}
