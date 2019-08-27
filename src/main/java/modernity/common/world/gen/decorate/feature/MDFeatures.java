/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import modernity.common.block.MDBlocks;

public class MDFeatures {
    // Unspecific features
    public static final ClusterBushFeature CLUSTER_BUSH = new ClusterBushFeature();
    public static final GroupedBushFeature GROUPED_BUSH = new GroupedBushFeature();
    public static final LakeFeature LAKE = new LakeFeature();
    public static final FluidFallFeature FLUID_FALL = new FluidFallFeature();
    public static final DepositFeature DEPOSIT = new DepositFeature();

    // Specific features
    public static final HangTreeFeature BLACKWOOD_TREE = new HangTreeFeature(
            MDBlocks.BLACKWOOD_LEAVES.getDefaultState(),
            MDBlocks.BLACKWOOD_LOG.getDefaultState(),
            MDBlocks.BLACKWOOD_BARK.getDefaultState()
    );
    public static final SphericalTreeFeature INVER_TREE = new SphericalTreeFeature(
            MDBlocks.INVER_LEAVES.getDefaultState(),
            MDBlocks.INVER_LOG.getDefaultState(),
            MDBlocks.INVER_BARK.getDefaultState()
    );



}
