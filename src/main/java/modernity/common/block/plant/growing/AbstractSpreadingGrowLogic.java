/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public abstract class AbstractSpreadingGrowLogic implements IGrowLogic {
    @Override
    public void grow( World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmlandLogic farmland ) {
        if( farmland == null ) return;

        if( farmland.isDecayed() ) {
            if( rand.nextInt( 2 ) == 0 ) {
                world.removeBlock( pos, false );
            }
        }

        if( checkResources( farmland, rand ) ) {
            if( grow( world, pos, rand ) ) {
                consumeResources( farmland, rand );
            }
        }
    }

    @Override
    public boolean grow( World world, BlockPos pos, BlockState state, Random rand, ItemStack item ) {
        if( isFertilizer( item ) ) {
            return grow( world, pos, rand );
        }

        return false;
    }

    protected boolean grow( World world, BlockPos pos, Random rand ) {
        EnumMap<Direction, Integer> sprs = new EnumMap<>( Direction.class );
        ArrayList<Direction> dirs = new ArrayList<>();
        for( Direction dir : Direction.Plane.HORIZONTAL ) {
            Integer spr = canSpread( world, pos, rand, dir );
            if( spr != null ) {
                sprs.put( dir, spr );
                dirs.add( dir );
            }
        }

        if( dirs.isEmpty() ) return false;

        Direction dir = dirs.get( rand.nextInt( dirs.size() ) );
        int spread = sprs.getOrDefault( dir, 0 );

        BlockPos plantPos = pos.offset( dir ).up( spread );
        placePlant( world, plantPos, rand );

        return true;
    }

    private Integer canSpread( World world, BlockPos pos, Random rand, Direction dir ) {
        BlockPos offPos = pos.offset( dir );
        if( canPlacePlant( world, offPos.down(), rand ) ) return - 1;
        if( canPlacePlant( world, offPos, rand ) ) return 0;
        if( canPlacePlant( world, offPos.up(), rand ) ) return 1;
        return null;
    }

    protected abstract boolean isFertilizer( ItemStack item );

    protected abstract boolean checkResources( IFarmlandLogic logic, Random rand );
    protected abstract void consumeResources( IFarmlandLogic logic, Random rand );

    protected abstract boolean canPlacePlant( World world, BlockPos pos, Random rand );
    protected abstract void placePlant( World world, BlockPos pos, Random rand );
}
