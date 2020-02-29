/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeathBlock extends SimplePlantBlock {
    public HeathBlock( Properties properties ) {
        super( properties, HEATH );
    }


    @Override
    public void growAt( World world, BlockPos pos ) {
        if( world.rand.nextInt( 5 ) == 0 ) {
            world.setBlockState( pos, MDBlocks.FLOWERED_HEATH.computeStateForPos( world, pos ), 3 );
        } else {
            world.setBlockState( pos, MDBlocks.HEATH.computeStateForPos( world, pos ), 3 );
        }
    }
}
