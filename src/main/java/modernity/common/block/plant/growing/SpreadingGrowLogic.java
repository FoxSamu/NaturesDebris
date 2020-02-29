/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.PlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public abstract class SpreadingGrowLogic implements IGrowLogic {
    protected final PlantBlock plant;

    protected SpreadingGrowLogic( PlantBlock plant ) {
        this.plant = plant;
    }

    @Override
    public void grow( World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland ) {
        if( farmland == null ) return;

        GrowType grow = checkResources( farmland, rand );
        if( grow == GrowType.GROW ) {
            if( grow( world, pos, rand, false ) ) {
                consumeResources( farmland, rand );
            }
        } else if( grow == GrowType.DIE ) {
            killPlant( world, pos, rand );
        }
    }

    @Override
    public boolean grow( World world, BlockPos pos, BlockState state, Random rand, ItemStack item ) {
        if( isFertilizer( item ) ) {
            return grow( world, pos, rand, true );
        }

        return false;
    }

    protected boolean grow( World world, BlockPos pos, Random rand, boolean isItem ) {
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

    protected abstract GrowType checkResources( IFarmland logic, Random rand );
    protected abstract void consumeResources( IFarmland logic, Random rand );

    protected boolean canPlacePlant( World world, BlockPos pos, Random rand ) {
        return plant.canGenerateAt( world, pos, world.getBlockState( pos ) );
    }

    protected void placePlant( World world, BlockPos pos, Random rand ) {
        plant.growAt( world, pos );
    }

    protected void killPlant( World world, BlockPos pos, Random rand ) {
        plant.kill( world, pos, world.getBlockState( pos ) );
    }

    public enum GrowType {
        NONE,
        GROW,
        DIE
    }
}
