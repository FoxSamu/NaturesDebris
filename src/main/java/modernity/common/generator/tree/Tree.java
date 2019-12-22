/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.base.DirtBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Tree implements IFeatureConfig {
    public abstract boolean canGenerate( IWorldReader world, Random rand, BlockPos pos );
    protected abstract void generateTree( Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos );

    public void generate( IWorld world, Random rand, BlockPos pos ) {
        Set<BlockPos> changedBlocks = Sets.newHashSet();

        generateTree( changedBlocks::add, world, rand, pos );

        // Changed blocks at each leaf distance (logs = 0)
        List<Set<BlockPos>> distLayers = Lists.newArrayList();

        Property<Integer> distance = getLeafDistanceProperty();
        int maxDist = getLeafDistanceMax();

        for( int layer = 0; layer < maxDist; layer++ ) {
            distLayers.add( Sets.newHashSet() );
        }

        // Update the leaf distances
        MovingBlockPos mpos = new MovingBlockPos();
        {

            // Update the leaves around logs to distance 1
            if( ! changedBlocks.isEmpty() ) {
                for( BlockPos change : Lists.newArrayList( changedBlocks ) ) {
                    for( Direction facing : Direction.values() ) {
                        mpos.setPos( change ).move( facing );
                        if( ! changedBlocks.contains( mpos ) ) {
                            BlockState state = world.getBlockState( mpos );
                            if( isDecayableLeaf( state ) ) {
                                // Add the leaves to the next list of changes
                                distLayers.get( 0 ).add( mpos.toImmutable() );
                                setBlockState( world, mpos, state.with( distance, 1 ) );
                            }
                        }
                    }
                }
            }

            // Update the leaves around leaves with changed distance: they probably need to be updated too
            for( int dist = 1; dist < maxDist; dist++ ) {
                Set<BlockPos> changedLeaves = distLayers.get( dist - 1 );
                Set<BlockPos> nextChanges = distLayers.get( dist );

                for( BlockPos change : changedLeaves ) {
                    for( Direction facing : Direction.values() ) {
                        mpos.setPos( change ).move( facing );
                        if( ! changedLeaves.contains( mpos ) && ! nextChanges.contains( mpos ) ) {
                            BlockState state = world.getBlockState( mpos );
                            if( isDecayableLeaf( state ) ) {
                                int currDist = state.get( distance );
                                if( currDist > dist + 1 ) {
                                    BlockState newState = state.with( distance, dist + 1 );
                                    setBlockState( world, mpos, newState );
                                    nextChanges.add( mpos.toImmutable() );
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    protected Property<Integer> getLeafDistanceProperty() {
        return BlockStateProperties.DISTANCE_1_7;
    }

    protected int getLeafDistanceMax() {
        return 6;
    }

    protected boolean isDecayableLeaf( BlockState state ) {
        return state.has( getLeafDistanceProperty() );
    }

    protected boolean hasDecayableLeaves() {
        return true;
    }

    protected void setBlockState( IWorld world, BlockPos pos, BlockState state ) {
        world.setBlockState( pos, state, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
    }

    public boolean isSustainable( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.getBlock() instanceof DirtBlock;
    }

    @Override
    public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
        return new Dynamic<>( ops );
    }
}
