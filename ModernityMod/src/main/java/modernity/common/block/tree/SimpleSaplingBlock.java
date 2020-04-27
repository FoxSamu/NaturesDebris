/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.tree;

import modernity.common.generator.tree.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.function.Supplier;

/**
 * Describes a sapling.
 */
public class SimpleSaplingBlock extends AbstractSaplingBlock {
    private final Supplier<Tree> tree;

    /**
     * Creates a sapling block.
     *
     * @param tree The tree feature to generate when this sapling is full grown.
     */
    public SimpleSaplingBlock( Supplier<Tree> tree, Properties properties ) {
        super( properties );
        this.tree = tree;
    }

    @Override
    protected int findGrowState( IWorld world, BlockPos pos ) {
        return 0;
    }

    @Override
    protected BlockPos getGrowPos( IWorld world, BlockPos pos, int growState ) {
        return pos;
    }

    @Override
    protected Tree getTree( IWorld world, BlockPos pos, int growState ) {
        return tree.get();
    }

    @Override
    protected void removeSaplings( IWorld world, BlockPos pos, int growState ) {
        world.removeBlock( pos, false );
    }
}
