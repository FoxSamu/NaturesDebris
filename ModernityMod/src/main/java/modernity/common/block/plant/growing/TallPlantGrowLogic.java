/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant.growing;

import modernity.common.block.plant.TallDirectionalPlantBlock;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public abstract class TallPlantGrowLogic extends SpreadingGrowLogic {
    protected final TallDirectionalPlantBlock plant;

    protected TallPlantGrowLogic( TallDirectionalPlantBlock plant ) {
        super( plant );
        this.plant = plant;
    }

    @Override
    protected boolean grow( World world, BlockPos pos, Random rand, boolean isItem ) {
        MovingBlockPos mpos = new MovingBlockPos( pos );
        int height = 0;
        while( plant.isSelfState( world, mpos, world.getBlockState( mpos ) ) ) {
            height++;
            mpos.move( plant.getGrowDirection() );
        }

        GrowDirection dir = decideGrowDir( world, pos, height, rand, isItem );

        if( dir == GrowDirection.UP ) {
            return growUp( world, pos, pos.offset( plant.getGrowDirection(), height ), height, rand );
        } else if( dir == GrowDirection.SIDEWARDS ) {
            return growSidewards( world, pos, rand );
        } else {
            return false;
        }
    }

    protected boolean growUp( World world, BlockPos pos, BlockPos end, int height, Random rand ) {
        if( canPlacePlant( world, end, rand ) ) {
            placePlant( world, end, rand );
            return true;
        }
        return false;
    }

    protected boolean growSidewards( World world, BlockPos pos, Random rand ) {
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

    protected abstract GrowDirection decideGrowDir( World world, BlockPos pos, int height, Random rand, boolean isItem );

    public enum GrowDirection {
        UP,
        SIDEWARDS,
        NONE
    }
}
