/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 30 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class MushroomBlock extends SimplePlantBlock {
    public static final VoxelShape SEEDLE_SHAPE = makePlantShape( 6, 6 );
    public static final VoxelShape DOTTED_MUSHROOM_SHAPE = makePlantShape( 6, 6 );
    public static final VoxelShape RITTLEPIT_SHAPE = makePlantShape( 6, 6 );

    public MushroomBlock( Properties properties, VoxelShape shape ) {
        super( properties, shape );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return isBlockSideSustainable( state, world, pos, Direction.UP );
    }
}
