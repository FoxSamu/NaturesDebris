/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.MushroomGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class CavePlantBlock extends SimplePlantBlock {
    public static final VoxelShape SEEDLE_SHAPE = makePlantShape( 6, 6 );
    public static final VoxelShape DOTTED_MUSHROOM_SHAPE = makePlantShape( 6, 6 );
    public static final VoxelShape BLACK_MUSHROOM_SHAPE = makePlantShape( 6, 6 );
    public static final VoxelShape CAVE_GRASS_SHAPE = makePlantShape( 14, 5 );
    public static final VoxelShape DEAD_GRASS_SHAPE = makePlantShape( 14, 5 );
    public static final VoxelShape PEBBLES_SHAPE = makePlantShape( 13, 2 );

    public static final Type SEEDLE = plant -> {
        plant.setGrowLogic( new MushroomGrowLogic( plant ) );
        return SEEDLE_SHAPE;
    };
    public static final Type DOTTED_MUSHROOM = plant -> {
        plant.setGrowLogic( new MushroomGrowLogic( plant ) );
        return DOTTED_MUSHROOM_SHAPE;
    };
    public static final Type BLACK_MUSHROOM = plant -> {
        plant.setGrowLogic( new MushroomGrowLogic( plant ) );
        return BLACK_MUSHROOM_SHAPE;
    };
    public static final Type CAVE_GRASS = plant -> CAVE_GRASS_SHAPE;
    public static final Type DEAD_GRASS = plant -> DEAD_GRASS_SHAPE;
    public static final Type PEBBLES = plant -> PEBBLES_SHAPE;

    protected CavePlantBlock( Properties properties, VoxelShape shape ) {
        super( properties, shape );
    }

    public CavePlantBlock( Properties properties, Type type ) {
        super( properties, type );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return isBlockSideSustainable( state, world, pos, Direction.UP );
    }
}
