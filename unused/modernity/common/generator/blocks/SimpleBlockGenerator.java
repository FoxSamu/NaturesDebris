/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class SimpleBlockGenerator implements IBlockGenerator {
    private final BlockState state;

    public SimpleBlockGenerator( BlockState state ) {
        this.state = state;
    }

    public SimpleBlockGenerator( Block block ) {
        this( block.getDefaultState() );
    }

    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        if( state.isValidPosition( world, pos ) ) {
            return world.setBlockState( pos, state, 2 );
        }
        return false;
    }
}
