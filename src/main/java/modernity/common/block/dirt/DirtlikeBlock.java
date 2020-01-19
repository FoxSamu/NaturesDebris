/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.common.block.base.DigableBlock;
import modernity.common.block.dirt.logic.DirtLogic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class DirtlikeBlock extends DigableBlock {
    protected final DirtLogic logic;

    public DirtlikeBlock( DirtLogic logic, Properties properties ) {
        super( properties );
        this.logic = logic;
    }

    @Override
    public boolean ticksRandomly( BlockState state ) {
        return logic.randomTicks();
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void randomTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( ! world.isRemote ) {
            logic.blockUpdate( world, pos, state, rand );
        }
    }

    public DirtLogic getLogic() {
        return logic;
    }
}
