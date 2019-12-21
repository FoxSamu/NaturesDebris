/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.tree;

import modernity.common.block.MDBlocks;
import modernity.common.block.base.AxisBlock;
import net.minecraft.util.Direction;

public final class MDTrees {

    public static final HangTree BLACKWOOD = new HangTree(
        MDBlocks.BLACKWOOD_LEAVES.getDefaultState(),
        MDBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.X ),
        MDBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Y ),
        MDBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Z )
    );

    public static final SphericalTree INVER = new SphericalTree(
        MDBlocks.INVER_LEAVES.getDefaultState(),
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.X ),
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Y ),
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Z )
    );

    private MDTrees() {
    }
}
