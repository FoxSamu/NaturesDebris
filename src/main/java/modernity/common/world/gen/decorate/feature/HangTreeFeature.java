/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.rgsw.noise.FractalPerlin3D;

import modernity.api.util.BlockUpdates;
import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.BlockBranch;
import modernity.common.block.base.BlockHangLeaves;
import modernity.common.block.prop.SignedIntegerProperty;

import java.util.Random;
import java.util.Set;

public class HangTreeFeature extends TreeFeature {

    protected final int minheight;
    protected final int maxheight;
    protected final int mincanopy;
    protected final int maxcanopy;

    public HangTreeFeature( IBlockState leaves, IBlockState log, IBlockState branch ) {this( leaves, log, branch, 5, 12, 4, 6 );}

    public HangTreeFeature( IBlockState leaves, IBlockState log, IBlockState branch, int minheight, int maxheight, int mincanopy, int maxcanopy ) {
        super( leaves, log, branch );
        this.minheight = minheight;
        this.maxheight = maxheight;
        this.mincanopy = mincanopy;
        this.maxcanopy = maxcanopy;
    }

    private boolean isSustainable( IBlockState state ) {
        return state.getBlock() == MDBlocks.DARK_GRASS || state.getBlock() == MDBlocks.DARK_DIRT;
    }

    // Fallback: this was the old generator
    public boolean generateTree1( Set<BlockPos> changed, IWorld world, BlockPos pos, Random rand ) {
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
        EcoBlockPos rpos = EcoBlockPos.retain();

        int height = rand.nextInt( 3 ) + 4;
        generateLog( changed, world, pos.down(), EnumFacing.UP, height + 1, rpos );

        rpos.setPos( pos );
        int leaveHeight = height - rand.nextInt( 2 ) - 1;
        rpos.moveUp( leaveHeight );

//        generateLeaves( world, rpos, rand, rand.nextInt( 2 ) + 3, 3, 2, - 1, - 1 );

        int branches = rand.nextInt( 3 );
        for( int i = 0; i < branches; i++ ) {
            EnumFacing facing = EnumFacing.Plane.HORIZONTAL.random( rand );
            int branchHeight = 1 + rand.nextInt( leaveHeight - 1 );

            rpos.setPos( pos );
            rpos.moveUp( branchHeight );
            rpos.move( facing );

//            generateLeaves( world, rpos, rand, rand.nextInt( 2 ) + 1, 2, 1, - 1, - 1 );

            IBlockState branch = BlockBranch.withFluid( branch( facing, true, true, true, true, true, false ), world, rpos );
            world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
            changed.add( rpos.toImmutable() );
        }

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            rpos.setPos( pos );
            rpos.move( facing );

            if( ! world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                IBlockState branch = BlockBranch.withFluid( branch( facing, 32 | facingFlag( facing.getOpposite() ) ), world, rpos );
                world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
                changed.add( rpos.toImmutable() );
            }

            rpos.moveUp( leaveHeight );

            IBlockState branch = BlockBranch.withFluid( branch( facing, true, true, true, true, true, false ), world, rpos );
            world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NO_RENDER );
            changed.add( rpos.toImmutable() );
        }

        rpos.release();
        return true;
    }


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

        try( EcoBlockPos rpos = EcoBlockPos.retain( pos ) ) {
            int height = rand.nextInt( this.maxheight - this.minheight + 1 ) + this.minheight;

            // Check for space
            try( EcoBlockPos tpos = EcoBlockPos.retain( pos ) ) {
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
                world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, EnumFacing.Axis.Y ), 2 | 16 );
                rpos.moveUp();
            }

            // Branches / Roots
            int radius = rand.nextInt( this.maxcanopy - this.mincanopy + 1 ) + this.mincanopy;
            for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                rpos.setPos( pos ).move( facing );
                EnumFacing.Axis axis = facing.getAxis();
                int len;


                // Root
                rpos.setPos( pos ).moveDown();
                len = rand.nextInt( 4 ) == 0 ? 2 : 1;

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
                                if( y == 0 && rand.nextInt( 3 ) == 0 ) {
                                    IBlockState hanger = hangingBlock( rand );
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
    protected boolean isDecayableLeaf( IBlockState state ) {
        return state.has( BlockHangLeaves.DISTANCE ) && state.get( BlockHangLeaves.DISTANCE ) > 0;
    }

    @Override
    protected SignedIntegerProperty getLeafDistanceProperty() {
        return BlockHangLeaves.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }

    public IBlockState hangingBlock( Random rand ) {
        return leaves.with( BlockHangLeaves.DISTANCE, - 1 );
    }

    public int hangingLength( Random rand, IBlockState hanger ) {
        return rand.nextInt( 5 ) + 1;
    }
}
