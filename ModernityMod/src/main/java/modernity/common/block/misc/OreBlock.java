/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class OreBlock extends Block {
    private final int minXP;
    private final int maxXP;

    public OreBlock( int minXP, int maxXP, Properties properties ) {
        super( properties );
        this.minXP = minXP;
        this.maxXP = maxXP;
    }

    @Override
    public int getExpDrop( BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch ) {
        return silktouch == 0 ? MathHelper.nextInt( RANDOM, minXP, maxXP ) : 0;
    }
}
