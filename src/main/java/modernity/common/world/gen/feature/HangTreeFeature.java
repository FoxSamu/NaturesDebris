/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.feature;

import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.DirtBlock;
import modernity.common.block.base.HangLeavesBlock;
import modernity.common.block.prop.SignedIntegerProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;
import java.util.Set;

/**
 * A feature that generates a droopy hang tree.
 * <pre>
 *          * * *
 *      * * * * * * *
 *    * * * ##### * * *
 *    *   *   #   * * *
 *    *       #   * *
 *            #     *
 *            #
 *          #####
 * </pre>
 */
public class HangTreeFeature extends TreeFeature {

    protected final int minheight;
    protected final int maxheight;
    protected final int mincanopy;
    protected final int maxcanopy;

    public HangTreeFeature( BlockState leaves, BlockState log, BlockState bark ) {
        this( leaves, log, bark, 5, 12, 4, 6 );
    }

    public HangTreeFeature( BlockState leaves, BlockState log, BlockState bark, int minheight, int maxheight, int mincanopy, int maxcanopy ) {
        super( leaves, log, bark );
        this.minheight = minheight;
        this.maxheight = maxheight;
        this.mincanopy = mincanopy;
        this.maxcanopy = maxcanopy;
    }

    private boolean isSustainable( BlockState state ) {
        return state.getBlock() instanceof DirtBlock;
    }


    @Override
    public boolean generateTree( Set<BlockPos> changed, IWorld world, BlockPos pos, Random rand ) {

        // Find actual generation height
        if( ! isSustainable( world.getBlockState( pos.down() ) ) ) {
            for( int i = 0; i < 20; i++ ) {
                pos = pos.down();
                if( world.getBlockState( pos.down() ).isSolid() ) {
                    break;
                }
            }
            if( ! isSustainable( world.getBlockState( pos.down() ) ) ) {
                return false;
            }
        }
        if( world.getBlockState( pos.up() ).getMaterial().blocksMovement() || world.getBlockState( pos.up() ).getMaterial().isLiquid() ) {
            return false;
        }

        MovingBlockPos rpos = new MovingBlockPos( pos );
        {
            int height = rand.nextInt( this.maxheight - this.minheight + 1 ) + this.minheight;

            // Check for space
            MovingBlockPos tpos = new MovingBlockPos();
            {
                for( int i = 0; i < height + 4; i++ ) {
                    if( i > 4 ) {
                        for( int x = - 3; x <= 3; x++ ) {
                            for( int z = - 3; z <= 3; z++ ) {
                                tpos.setPos( rpos );
                                tpos.addPos( x, 0, z );
                                if( world.getBlockState( tpos ).getMaterial().blocksMovement() )
                                    return false;
                            }
                        }
                    } else {
                        if( world.getBlockState( rpos ).getMaterial().blocksMovement() )
                            return false;
                    }
                    rpos.moveUp();
                }
            }

            // Log
            rpos.setPos( pos );
            rpos.moveDown();
            for( int i = - 1; i <= height; i++ ) {
                world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, Direction.Axis.Y ), 2 | 16 );
                rpos.moveUp();
            }

            // Branches / Roots
            int radius = rand.nextInt( this.maxcanopy - this.mincanopy + 1 ) + this.mincanopy;
            for( Direction facing : Direction.Plane.HORIZONTAL ) {
                rpos.setPos( pos ).move( facing );
                Direction.Axis axis = facing.getAxis();


                // Root
                rpos.setPos( pos ).moveDown();
                int len = rand.nextInt( 4 ) == 0 ? 2 : 1;

                for( int i = 0; i < len; i++ ) {
                    rpos.move( facing );
                    world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, axis ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                    changed.add( rpos.toImmutable() );
                }

                // Branch
                rpos.setPos( pos );
                rpos.moveUp( height );
                len = rand.nextInt( 2 ) + 1;

                for( int i = 0; i < len; i++ ) {
                    rpos.move( facing );
                    world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, axis ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                    changed.add( rpos.toImmutable() );
                }
            }

            // Canopy (leaves)
            FractalPerlin3D noise = new FractalPerlin3D( rand.nextInt(), 2 );
            for( int x = - radius; x <= radius; x++ ) {
                for( int z = - radius; z <= radius; z++ ) {
                    for( int y = 0; y <= radius / 5D * 4; y++ ) {
                        double r = radius + noise.generate( x / 5.3D, y / 5.3D, z / 5.3D );
                        if( x * x + 2 * y * y + z * z <= r * r ) {
                            rpos.setPos( pos );
                            rpos.moveUp( height );
                            rpos.addPos( x, y, z );
                            if( ! world.getBlockState( rpos ).getMaterial().blocksMovement() && rand.nextInt( 8 ) != 0 ) {
                                world.setBlockState( rpos, leaves, 2 | 16 );
                                if( y == 0 && hangingBlockRandom( rand ) ) {
                                    BlockState hanger = hangingBlock( rand );
                                    int len = hangingLength( rand, hanger );
                                    for( int i = 0; i < len; i++ ) {
                                        rpos.moveDown();
                                        Material mat = world.getBlockState( rpos ).getMaterial();
                                        if( mat.blocksMovement() ) break;
                                        if( mat.isLiquid() ) break;
                                        world.setBlockState( rpos, hanger, 2 | 16 );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected boolean isDecayableLeaf( BlockState state ) {
        return state.has( HangLeavesBlock.DISTANCE ) && state.get( HangLeavesBlock.DISTANCE ) > 0;
    }

    @Override
    protected SignedIntegerProperty getLeafDistanceProperty() {
        return HangLeavesBlock.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }

    /**
     * Randomly chooses whether a hanging block may generate.
     */
    public boolean hangingBlockRandom( Random rand ) {
        return rand.nextInt( 3 ) == 0;
    }

    /**
     * Selects a random hanging block.
     */
    public BlockState hangingBlock( Random rand ) {
        if( rand.nextBoolean() ) return MDBlocks.MURINA.getDefaultState();
        return leaves.with( HangLeavesBlock.DISTANCE, - 1 );
    }

    /**
     * Picks a hanging lenght for the specified hanger block.
     */
    public int hangingLength( Random rand, BlockState hanger ) {
        if( hanger.getBlock() == MDBlocks.MURINA ) {
            return rand.nextInt( 8 ) + 1;
        }
        return rand.nextInt( 5 ) + 1;
    }
}
