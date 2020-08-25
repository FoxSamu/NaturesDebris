/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.tree;

import natures.debris.common.blockold.MDTreeBlocks;
import natures.debris.common.blockold.base.AxisBlock;
import net.minecraft.util.Direction;

public final class MDTrees {

    public static final TinyBlackwoodTree BLACKWOOD_TINY = new TinyBlackwoodTree();

    public static final BlackwoodTree BLACKWOOD = new BlackwoodTree();

    public static final TallBlackwoodTree BLACKWOOD_TALL = new TallBlackwoodTree();

    public static final SphereTree INVER = new SphereTree(
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.X),
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Y),
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Z),
        MDTreeBlocks.INVER_LEAVES.getDefaultState()
    );

    public static final SphereTree RED_INVER = new SphereTree(
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.X),
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Y),
        MDTreeBlocks.INVER_LOG.getDefaultState().with(AxisBlock.AXIS, Direction.Axis.Z),
        MDTreeBlocks.RED_INVER_LEAVES.getDefaultState()
    );

    private MDTrees() {
    }
}
