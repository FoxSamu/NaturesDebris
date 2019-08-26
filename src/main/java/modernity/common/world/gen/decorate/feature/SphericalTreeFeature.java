/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.BlockDecayLeaves;
import modernity.common.block.base.BlockDirt;

import java.util.Random;
import java.util.Set;

import static modernity.api.util.BlockUpdates.*;

public class SphericalTreeFeature extends TreeFeature {

    protected final int minheight;
    protected final int maxheight;
    protected final int mincanopy;
    protected final int maxcanopy;

    public SphericalTreeFeature( IBlockState leaves, IBlockState log, IBlockState branch ) {this( leaves, log, branch, 5, 12, 4, 6 );}

    public SphericalTreeFeature( IBlockState leaves, IBlockState log, IBlockState branch, int minheight, int maxheight, int mincanopy, int maxcanopy ) {
        super( leaves, log, branch );
        this.minheight = minheight;
        this.maxheight = maxheight;
        this.mincanopy = mincanopy;
        this.maxcanopy = maxcanopy;
    }

    private boolean isSustainable( IBlockState state ) {
        return state.getBlock() instanceof BlockDirt;
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

            // Roots
//            int radius = rand.nextInt( this.maxcanopy - this.mincanopy + 1 ) + this.mincanopy;
            for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                rpos.setPos( pos ).move( facing );
                EnumFacing.Axis axis = facing.getAxis();
                int len;


                // Root
                rpos.setPos( pos ).moveDown();
                len = rand.nextInt( 4 ) == 0 ? 2 : 1;

                for( int i = 0; i < len; i++ ) {
                    rpos.move( facing );
                    world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, axis ), NOTIFY_CLIENTS | NO_NEIGHBOR_REACTIONS | NO_RENDER );
                    changed.add( rpos.toImmutable() );
                }
            }

            rpos.setPos( pos ).moveUp( height );
            addCanopy( world, rand, rpos, rand.nextDouble() * 1.4 + 3 );

            for( int i = 0; i < 8; i++ ) {
                if( rand.nextInt( 10 ) != 0 ) {
                    rpos.setPos( pos ).moveUp( height - 3 + rand.nextInt( 3 ) );
                    addBranch( world, rand, rpos, i );
                }
            }
        }
        return true;
    }

    private void addBranch( IWorld world, Random rand, BlockPos pos, int dir ) {
        int length = rand.nextInt( 2 ) + 1;
        int xdir = 0;
        switch( dir ) {
            case 7:
            case 0:
            case 1:
                xdir = 1;
                break;
            case 3:
            case 4:
            case 5:
                xdir = - 1;
                break;
        }
        int zdir = 0;
        switch( dir ) {
            case 1:
            case 2:
            case 3:
                zdir = 1;
                break;
            case 5:
            case 6:
            case 7:
                zdir = - 1;
                break;
        }
        int ydir = 1;

        IBlockState log;
        if( zdir == 0 ) {
            log = this.log.with( BlockStateProperties.AXIS, EnumFacing.Axis.X );
        } else if( xdir == 0 ) {
            log = this.log.with( BlockStateProperties.AXIS, EnumFacing.Axis.Z );
        } else {
            log = this.log.with( BlockStateProperties.AXIS, rand.nextBoolean() ? EnumFacing.Axis.X : EnumFacing.Axis.Z );
        }

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            rpos.setPos( pos );
            for( int i = 0; i < length; i++ ) {
                rpos.addPos( xdir, ydir, zdir );
                world.setBlockState( rpos, log, NOTIFY_CLIENTS | NO_NEIGHBOR_REACTIONS | NO_RENDER );
            }
            addCanopy( world, rand, rpos, rand.nextDouble() * 1.2 + 2.2 );
        }
    }

    private void addCanopy( IWorld world, Random rand, BlockPos pos, double radius ) {
        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            int ri = (int) ( radius + 2 );
            double spherex = pos.getX() + rand.nextDouble();
            double spherey = pos.getY() + rand.nextDouble();
            double spherez = pos.getZ() + rand.nextDouble();
            for( int x = - ri; x <= ri; x++ ) {
                for( int z = - ri; z <= ri; z++ ) {
                    for( int y = - ri; y <= ri; y++ ) {
                        rpos.setPos( pos ).addPos( x, y, z );
                        double cx = rpos.getX() + 0.5 - spherex;
                        double cy = rpos.getY() + 0.5 - spherey;
                        double cz = rpos.getZ() + 0.5 - spherez;

                        if( cx * cx + cy * cy + cz * cz < radius * radius ) {
                            if( ! world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                                world.setBlockState( rpos, leaves, NOTIFY_CLIENTS | NO_NEIGHBOR_REACTIONS | NO_RENDER );
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isDecayableLeaf( IBlockState state ) {
        return state.has( BlockDecayLeaves.DISTANCE ) && state.get( BlockDecayLeaves.DISTANCE ) > 0;
    }

    @Override
    protected IntegerProperty getLeafDistanceProperty() {
        return BlockDecayLeaves.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }

    public boolean hangingBlockRandom( Random rand ) {
        return rand.nextInt( 3 ) == 0;
    }

    public IBlockState hangingBlock( Random rand ) {
        if( rand.nextBoolean() ) return MDBlocks.MURINA.getDefaultState();
        return leaves.with( BlockDecayLeaves.DISTANCE, - 1 );
    }

    public int hangingLength( Random rand, IBlockState hanger ) {
        if( hanger.getBlock() == MDBlocks.MURINA ) {
            return rand.nextInt( 8 ) + 1;
        }
        return rand.nextInt( 5 ) + 1;
    }
}
