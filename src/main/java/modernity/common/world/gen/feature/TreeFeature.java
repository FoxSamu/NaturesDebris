/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.world.gen.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.prop.SignedIntegerProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A feature that generates a tree. Subclasses can generate the tree, and this feature computes the leaf distances...
 */
public abstract class TreeFeature extends Feature<NoFeatureConfig> {

    protected final BlockState leaves;
    protected final BlockState log;
    protected final BlockState bark;

    public TreeFeature( BlockState leaves, BlockState log, BlockState bark ) {
        super( dynamic -> IFeatureConfig.NO_FEATURE_CONFIG );
        this.leaves = leaves;
        this.log = log;
        this.bark = bark;
    }

    /**
     * Generates the tree and gives leaves the correct distance value. This method is called by saplings directly
     * instead of {@link #place}.
     *
     * @param world The world to generate the tree in.
     * @param rand  A random number generator.
     * @param pos   The position to generate the tree at.
     * @return True if the tree successfully generated.
     */
    public boolean generate( IWorld world, Random rand, BlockPos pos ) {
        Set<BlockPos> changedBlocks = Sets.newHashSet();

        boolean generated = generateTree( changedBlocks, world, pos, rand );

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
            if( generated && ! changedBlocks.isEmpty() ) {
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

        return generated;
    }

    /**
     * Returns an integer property, either {@linkplain SignedIntegerProperty signed} or {@link IntegerProperty
     * unsigned}, which is the distance property of the used leaves.
     */
    protected Property<Integer> getLeafDistanceProperty() {
        return BlockStateProperties.DISTANCE_1_7;
    }

    /**
     * Returns the maximum distance of the used leaves.
     */
    protected int getLeafDistanceMax() {
        return 6;
    }

    /**
     * Returns true when the passed block state is a decayable leaves block.
     */
    protected boolean isDecayableLeaf( BlockState state ) {
        return state.has( getLeafDistanceProperty() );
    }

    /**
     * Sets a block state in the world, using the flags {@link BlockUpdates#NOTIFY_CLIENTS <code>NOTIFY_CLIENTS</code>},
     * {@link BlockUpdates#NO_NEIGHBOR_REACTIONS <code>NO_NEIGHBOR_REACTIONS</code>} and {@link BlockUpdates#NO_RENDER
     * <code>NO_RENDER</code>},
     *
     * @param world The world to set the block in.
     * @param pos   The position to set the block at.
     * @param state The block state to set.
     */
    protected void setBlockState( IWorld world, BlockPos pos, BlockState state ) {
        world.setBlockState( pos, state, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random rand, BlockPos pos, NoFeatureConfig config ) {
        return generate( world, rand, pos );
    }

    /**
     * Generates the actual tree.
     * @param changedBlocks A list of block positions that changed into log blocks, used to update leaf distances.
     * @param world         The world to generate in.
     * @param pos           The position to generate the tree at.
     * @param rand          A random number generator.
     */
    protected abstract boolean generateTree( Set<BlockPos> changedBlocks, IWorld world, BlockPos pos, Random rand );

    /**
     * @deprecated Not used, and should possibly be removed.
     */
    @Deprecated
    public void generateLeaves( IWorld world, BlockPos pos, Random rand, int size, int height, int cornerCutoff, int hangingLeavesLength, int skipChance ) {
        MovingBlockPos rpos = new MovingBlockPos();
        {
            int rad = size;
            for( int y = 0; y < height; y++ ) {
                generateLeavesLayer(
                    world, pos, rand, y, rad, cornerCutoff, skipChance,
                    y == 0
                    ? hangingLeavesLength
                    : - 1,
                    rpos
                );
                rad--;
            }
        }
    }

    /**
     * @deprecated Not used, and should possibly be removed.
     */
    @Deprecated
    public void generateLeavesLayer( IWorld world, BlockPos pos, Random rand, int yoffset, int size, int cornerCutoff, int skipChance, int hangingLeavesLength, MovingBlockPos rpos ) {
        int cutoffLimit = size * 2 - 1 - cornerCutoff;
        for( int x = - size; x <= size; x++ ) {
            for( int z = - size; z <= size; z++ ) {
                if( skipChance > 0 && rand.nextInt( skipChance ) == 0 ) continue;
                int sum = Math.abs( x ) + Math.abs( z ) - 1;
                if( cornerCutoff <= 0 || sum <= cutoffLimit ) {
                    rpos.setPos( pos );
                    rpos.addPos( x, yoffset, z );

                    BlockState state = world.getBlockState( rpos );
                    Material mat = state.getMaterial();
                    if( state.canBeReplacedByLeaves( world, rpos ) || mat == Material.TALL_PLANTS ) {
                        world.setBlockState( rpos, leaves, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                        if( hangingLeavesLength > 0 && rand.nextInt( 4 ) == 0 ) {
                            generateHangingLeaves( world, rpos, rand, hangingLeavesLength );
                        }
                    }
                }
            }
        }
    }

    /**
     * @deprecated Not used, and should possibly be removed.
     */
    @Deprecated
    public void generateLog( Set<BlockPos> changed, IWorld world, BlockPos pos, Direction facing, int len, MovingBlockPos rpos ) {
        for( int i = 0; i < len; i++ ) {
            rpos.setPos( pos );
            rpos.move( facing, i );

            BlockState state = world.getBlockState( rpos );
            if( ! state.getMaterial().blocksMovement() || state.isIn( MDBlockTags.LEAVES ) ) {
                world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, facing.getAxis() ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                changed.add( rpos.toImmutable() );
            }
        }
    }

    /**
     * @deprecated Not used, and should possibly be removed.
     */
    @Deprecated
    private void generateHangingLeaves( IWorld world, MovingBlockPos pos, Random rand, int maxLen ) {
        int len = rand.nextInt( maxLen + 1 );
        for( int i = 0; i < len; i++ ) {
            pos.moveDown();

            BlockState state = world.getBlockState( pos );
            Material mat = state.getMaterial();
            if( state.canBeReplacedByLeaves( world, pos ) || mat == Material.TALL_PLANTS ) {
                world.setBlockState( pos, leaves, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
            } else {
                break;
            }
        }
    }

    /**
     * @deprecated Not used, and should possibly be removed.
     */
    @Deprecated
    public int facingFlag( Direction facing ) {
        switch( facing ) {
            default:
                return 0;
            case NORTH:
                return 1;
            case EAST:
                return 2;
            case SOUTH:
                return 4;
            case WEST:
                return 8;
            case UP:
                return 16;
            case DOWN:
                return 32;
        }
    }
}
