/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.generator.tree;

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

    public static final SphereTree INVER = new SphereTree(
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.X ),
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Y ),
        MDBlocks.INVER_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Z ),
        MDBlocks.INVER_LEAVES.getDefaultState()
    );

    private MDTrees() {
    }
}
