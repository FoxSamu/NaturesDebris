/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.generator.tree;

import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import modernity.common.block.tree.HangLeavesBlock;
import modernity.common.block.prop.SignedIntegerProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.redgalaxy.exc.UnexpectedCaseException;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;
import java.util.function.Consumer;

public class HangTree extends Tree {

    private final BlockState leaves;
    private final BlockState logX;
    private final BlockState logY;
    private final BlockState logZ;
    private final int minheight;
    private final int maxheight;
    private final int mincanopy;
    private final int maxcanopy;

    public HangTree( BlockState leaves, BlockState logX, BlockState logY, BlockState logZ ) {
        this( leaves, logX, logY, logZ, 5, 12, 4, 6 );
    }

    public HangTree( BlockState leaves, BlockState logX, BlockState logY, BlockState logZ, int minheight, int maxheight, int mincanopy, int maxcanopy ) {
        this.leaves = leaves;
        this.logX = logX;
        this.logY = logY;
        this.logZ = logZ;
        this.minheight = minheight;
        this.maxheight = maxheight;
        this.mincanopy = mincanopy;
        this.maxcanopy = maxcanopy;
    }

    @Override
    public boolean isSustainable( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }

    @Override
    public boolean canGenerate( IWorldReader world, Random rand, BlockPos pos ) {
        if( world.getBlockState( pos.up() ).getMaterial().blocksMovement() || world.getBlockState( pos.up() ).getMaterial().isLiquid() ) {
            return false;
        }

        MovingBlockPos rpos = new MovingBlockPos( pos );

        int height = rand.nextInt( maxheight - minheight + 1 ) + minheight;

        // Check for space
        MovingBlockPos tpos = new MovingBlockPos();

        for( int i = 0; i < height; i++ ) {
            if( i > 4 ) {
                for( int x = - 1; x <= 1; x++ ) {
                    for( int z = - 1; z <= 1; z++ ) {
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

        return true;
    }

    private BlockState log( Direction.Axis axis ) {
        switch( axis ) {
            case X: return logX;
            case Y: return logY;
            case Z: return logZ;
            default: throw new UnexpectedCaseException( "4th axis...what?" );
        }
    }

    @Override
    protected void generateTree( Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos ) {
        MovingBlockPos rpos = new MovingBlockPos( pos );


        int height = rand.nextInt( maxheight - minheight + 1 ) + minheight;

        // Log
        rpos.setPos( pos );
        rpos.moveDown();
        for( int i = - 1; i <= height; i++ ) {
            world.setBlockState( rpos, logY, 2 | 16 );
            logs.accept( rpos.toImmutable() );
            rpos.moveUp();
        }

        // Branches / Roots
        int radius = rand.nextInt( maxcanopy - mincanopy + 1 ) + mincanopy;
        for( Direction facing : Direction.Plane.HORIZONTAL ) {
            rpos.setPos( pos ).move( facing );
            Direction.Axis axis = facing.getAxis();


            // Root
            rpos.setPos( pos ).moveDown();
            int len = rand.nextInt( 4 ) == 0 ? 2 : 1;

            for( int i = 0; i < len; i++ ) {
                rpos.move( facing );
                world.setBlockState( rpos, log( axis ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                logs.accept( rpos.toImmutable() );
            }

            // Branch
            rpos.setPos( pos );
            rpos.moveUp( height );
            len = rand.nextInt( 2 ) + 1;

            for( int i = 0; i < len; i++ ) {
                rpos.move( facing );
                world.setBlockState( rpos, log( axis ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                logs.accept( rpos.toImmutable() );
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

    /**
     * Randomly chooses whether a hanging block may generate.
     */
    protected boolean hangingBlockRandom( Random rand ) {
        return rand.nextInt( 3 ) == 0;
    }

    /**
     * Selects a random hanging block.
     */
    protected BlockState hangingBlock( Random rand ) {
        if( rand.nextBoolean() ) return MDBlocks.MURINA.getDefaultState();
        return leaves.with( HangLeavesBlock.DISTANCE, - 1 );
    }

    /**
     * Picks a hanging lenght for the specified hanger block.
     */
    protected int hangingLength( Random rand, BlockState hanger ) {
        if( hanger.getBlock() == MDBlocks.MURINA ) {
            return rand.nextInt( 8 ) + 1;
        }
        return rand.nextInt( 4 ) + 1;
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
}
