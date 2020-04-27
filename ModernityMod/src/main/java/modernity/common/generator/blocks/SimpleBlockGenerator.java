/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
