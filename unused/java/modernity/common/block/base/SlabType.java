/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.generic.util.MDVoxelShapes;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * The possible states of a vertical-placable slab block.
 */
public enum SlabType implements IStringSerializable {
    DOWN( "down", MDVoxelShapes.create16( 0, 0, 0, 16, 8, 16 ), Direction.DOWN ),
    UP( "up", MDVoxelShapes.create16( 0, 8, 0, 16, 16, 16 ), Direction.UP ),
    NORTH( "north", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 8 ), Direction.NORTH ),
    SOUTH( "south", MDVoxelShapes.create16( 0, 0, 8, 16, 16, 16 ), Direction.SOUTH ),
    WEST( "west", MDVoxelShapes.create16( 0, 0, 0, 8, 16, 16 ), Direction.WEST ),
    EAST( "east", MDVoxelShapes.create16( 8, 0, 0, 16, 16, 16 ), Direction.EAST ),
    DOUBLE( "double", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 16 ), null );

    private final String name;
    private final VoxelShape shape;
    private final Direction facing;

    SlabType( String name, VoxelShape shape, Direction facing ) {
        this.name = name;
        this.shape = shape;
        this.facing = facing;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the shape of such slabs.
     */
    public VoxelShape getShape() {
        return shape;
    }

    /**
     * Returns the facing of this slab, or null when {@link #DOUBLE}.
     */
    public Direction getFacing() {
        return facing;
    }

    /**
     * Returns the type that belongs to the specified facing. Returns {@link #DOUBLE} when facing is null.
     */
    public static SlabType forFacing( Direction facing ) {
        if( facing == null ) {
            return DOUBLE;
        }
        switch( facing ) {
            case UP: return UP;
            case DOWN: return DOWN;
            case EAST: return EAST;
            case WEST: return WEST;
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            default: return DOUBLE;
        }
    }
}
