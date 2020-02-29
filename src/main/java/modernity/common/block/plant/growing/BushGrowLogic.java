/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.PlantBlock;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public class BushGrowLogic extends SpreadingGrowLogic {
    public BushGrowLogic( PlantBlock plant ) {
        super( plant );
    }

    @Override
    protected boolean grow( World world, BlockPos pos, Random rand, boolean isItem ) {
        MovingBlockPos mpos = new MovingBlockPos( pos );
        int height = 0;
        while( world.getBlockState( mpos ).getBlock() == plant ) {
            height++;
            mpos.moveUp();
        }

        GrowDirection dir = decideGrowDir( world, pos, height, rand );

        if( dir == GrowDirection.UP ) {
            return growUp( world, pos, pos.up( height ), height, rand );
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

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return item.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        if( logic.isDecayed() ) {
            return rand.nextInt( 2 ) == 0 ? GrowType.DIE : GrowType.NONE;
        } else if( logic.isFertile() ) {
            return rand.nextInt( 16 ) < 6 ? GrowType.GROW : GrowType.NONE;
        } else {
            return GrowType.NONE;
        }
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
        logic.decreaseLevel();
    }

    protected GrowDirection decideGrowDir( World world, BlockPos pos, int height, Random rand ) {
        if( rand.nextInt( 3 ) == 0 ) {
            if( height >= 3 ) return GrowDirection.NONE;
            if( height == 2 ) return rand.nextInt( 9 ) == 0 ? GrowDirection.UP : GrowDirection.NONE;
            return GrowDirection.UP;
        } else {
            return GrowDirection.SIDEWARDS;
        }
    }

    public enum GrowDirection {
        UP,
        SIDEWARDS,
        NONE
    }
}
