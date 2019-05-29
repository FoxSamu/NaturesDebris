package modernity.common.world.gen.decorate.feature;

import modernity.common.block.MDBlocks;

public class MDFeatures {
    public static final BushFeature BUSH = new BushFeature();
    public static final DarkwoodTreeFeature DARKWOOD_TREE = new DarkwoodTreeFeature(
            MDBlocks.BLACKWOOD_LEAVES.getDefaultState(),
            MDBlocks.BLACKWOOD_LOG.getDefaultState(),
            MDBlocks.BLACKWOOD_BRANCH.getDefaultState()
    );
}
