package modernity.common.world.gen.decorate.feature;

import modernity.common.block.MDBlocks;

public class MDFeatures {
    public static final BushFeature BUSH = new BushFeature();
    public static final DarkwoodTreeFeature DARKWOOD_TREE = new DarkwoodTreeFeature(
            MDBlocks.DARKWOOD_LEAVES.getDefaultState(),
            MDBlocks.DARKWOOD_LOG.getDefaultState(),
            MDBlocks.DARKWOOD_BRANCH.getDefaultState()
    );
}
