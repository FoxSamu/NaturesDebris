/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
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
    public void grow( World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland ) {
        if( rand.nextInt( 3 ) != 0 ) return;

        ArrayList<BlockPos> locs = new ArrayList<>();
        for( int x = - 1; x <= 1; x++ ) {
            for( int z = - 1; z <= 1; z++ ) {
                for( int y = - 1; y <= 1; y++ ) {
                    if( x == 0 && y == 0 && z == 0 ) continue;

                    locs.add( pos.add( x, y, z ) );
                }
            }
        }

        while( ! locs.isEmpty() ) {
            BlockPos loc = locs.remove( rand.nextInt( locs.size() ) );
            if( tryGrow( world, loc, rand ) ) {
                return;
            }
        }
    }

    @Override
    public boolean grow( World world, BlockPos pos, BlockState state, Random rand, ItemStack item ) {
        if( ! item.getItem().isIn( MDItemTags.FERTILIZER ) ) return false;

        ArrayList<BlockPos> locs = new ArrayList<>();
        for( int x = - 1; x <= 1; x++ ) {
            for( int z = - 1; z <= 1; z++ ) {
                for( int y = - 1; y <= 1; y++ ) {
                    if( x == 0 && y == 0 && z == 0 ) continue;

                    locs.add( pos.add( x, y, z ) );
                }
            }
        }

        while( ! locs.isEmpty() ) {
            BlockPos loc = locs.remove( rand.nextInt( locs.size() ) );
            if( tryGrow( world, loc, rand ) ) {
                return true;
            }
        }

        return false;
    }

    private boolean tryGrow( World world, BlockPos pos, Random rand ) {
        if( ! world.isAirBlock( pos ) ) return false;

        ArrayList<Direction> dirs = new ArrayList<>();

        for( Direction dir : Direction.values() ) {
            BlockPos adj = pos.offset( dir, - 1 );
            BlockState state = world.getBlockState( adj );

            if( plant.isBlockSideSustainable( state, world, adj, dir ) ) {
                dirs.add( dir );
            }
        }

        if( dirs.isEmpty() ) return false;

        Direction dir = dirs.get( rand.nextInt( dirs.size() ) );

        world.setBlockState( pos, plant.computeStateForPos( world, pos ).with( FacingPlantBlock.FACING, dir ) );
        return true;
    }
}
