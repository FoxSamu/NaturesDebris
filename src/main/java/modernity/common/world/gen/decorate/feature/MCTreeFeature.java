package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

import java.util.Random;
import java.util.Set;

public class MCTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {
    private static final IBlockState DEFAULT_TRUNK = Blocks.OAK_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAF = Blocks.OAK_LEAVES.getDefaultState();
    /** The minimum height of a generated tree. */
    protected final int minTreeHeight;
    /** True if this tree should grow Vines. */
    private final boolean vinesGrow;
    /** The metadata value of the wood to use in tree generation. */
    private final IBlockState metaWood;
    /** The metadata value of the leaves to use in tree generation. */
    private final IBlockState metaLeaves;
    protected IPlantable sapling = (IPlantable) Blocks.OAK_SAPLING;

    public MCTreeFeature( boolean notify ) {
        this( notify, 4, DEFAULT_TRUNK, DEFAULT_LEAF, false );
    }

    public MCTreeFeature( boolean notify, int minTreeHeight, IBlockState wood, IBlockState leaves, boolean growVines ) {
        super( notify );
        this.minTreeHeight = minTreeHeight;
        this.metaWood = wood;
        this.metaLeaves = leaves;
        this.vinesGrow = growVines;
    }

    public boolean place( Set<BlockPos> changedBlocks, IWorld world, Random rand, BlockPos position ) {
        int height = this.randomHeight( rand );
        boolean canGenerate = true;
        if( position.getY() >= 1 && position.getY() + height + 1 <= world.getWorld().getHeight() ) {
            for( int y = position.getY(); y <= position.getY() + 1 + height; ++ y ) {
                int radius = 1;
                if( y == position.getY() ) {
                    radius = 0;
                }

                if( y >= position.getY() + 1 + height - 2 ) {
                    radius = 2;
                }

                BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

                for( int x = position.getX() - radius; x <= position.getX() + radius && canGenerate; ++ x ) {
                    for( int z = position.getZ() - radius; z <= position.getZ() + radius && canGenerate; ++ z ) {

                        if( y >= 0 && y < world.getWorld().getHeight() ) {
                            if( ! this.canGrowInto( world, mpos.setPos( x, y, z ) ) ) {
                                canGenerate = false;
                            }
                        } else {
                            canGenerate = false;
                        }
                    }
                }
            }

            if( ! canGenerate ) {
                return false;
            } else {
                if( world.getBlockState( position.down() ).canSustainPlant( world, position.down(), EnumFacing.UP, (BlockSapling) Blocks.OAK_SAPLING ) && position.getY() < world.getWorld().getHeight() - height - 1 ) {
                    this.setDirtAt( world, position.down(), position );

                    for( int y = position.getY() - 3 + height; y <= position.getY() + height; ++ y ) {

                        int locY = y - ( position.getY() + height );
                        int radius = 1 - locY / 2;

                        for( int x = position.getX() - radius; x <= position.getX() + radius; ++ x ) {
                            int locX = x - position.getX();

                            for( int z = position.getZ() - radius; z <= position.getZ() + radius; ++ z ) {
                                int locZ = z - position.getZ();

                                if( Math.abs( locX ) != radius || Math.abs( locZ ) != radius || rand.nextInt( 2 ) != 0 && locY != 0 ) {
                                    BlockPos pos = new BlockPos( x, y, z );
                                    IBlockState state = world.getBlockState( pos );
                                    Material mat = state.getMaterial();
                                    if( state.canBeReplacedByLeaves( world, pos ) || mat == Material.VINE ) {
                                        this.setBlockState( world, pos, this.metaLeaves );
                                    }
                                }
                            }
                        }
                    }

                    for( int y = 0; y < height; ++ y ) {
                        IBlockState state = world.getBlockState( position.up( y ) );
                        Material mat = state.getMaterial();
                        if( state.canBeReplacedByLeaves( world, position.up( y ) ) || mat == Material.VINE ) {
                            this.placeLog( changedBlocks, world, position.up( y ), this.metaWood );

                            if( this.vinesGrow && y > 0 ) {
                                if( rand.nextInt( 3 ) > 0 && world.isAirBlock( position.add( - 1, y, 0 ) ) ) {
                                    this.addVine( world, position.add( - 1, y, 0 ), BlockVine.EAST );
                                }

                                if( rand.nextInt( 3 ) > 0 && world.isAirBlock( position.add( 1, y, 0 ) ) ) {
                                    this.addVine( world, position.add( 1, y, 0 ), BlockVine.WEST );
                                }

                                if( rand.nextInt( 3 ) > 0 && world.isAirBlock( position.add( 0, y, - 1 ) ) ) {
                                    this.addVine( world, position.add( 0, y, - 1 ), BlockVine.SOUTH );
                                }

                                if( rand.nextInt( 3 ) > 0 && world.isAirBlock( position.add( 0, y, 1 ) ) ) {
                                    this.addVine( world, position.add( 0, y, 1 ), BlockVine.NORTH );
                                }
                            }
                        }
                    }

                    if( this.vinesGrow ) {
                        for( int y = position.getY() - 3 + height; y <= position.getY() + height; ++ y ) {
                            int locY = y - ( position.getY() + height );
                            int radius = 2 - locY / 2;
                            BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

                            for( int x = position.getX() - radius; x <= position.getX() + radius; ++ x ) {
                                for( int z = position.getZ() - radius; z <= position.getZ() + radius; ++ z ) {
                                    mpos.setPos( x, y, z );
                                    if( world.getBlockState( mpos ).isIn( BlockTags.LEAVES ) ) {
                                        BlockPos west = mpos.west();
                                        BlockPos east = mpos.east();
                                        BlockPos north = mpos.north();
                                        BlockPos south = mpos.south();
                                        if( rand.nextInt( 4 ) == 0 && world.isAirBlock( west ) ) {
                                            this.addHangingVine( world, west, BlockVine.EAST );
                                        }

                                        if( rand.nextInt( 4 ) == 0 && world.isAirBlock( east ) ) {
                                            this.addHangingVine( world, east, BlockVine.WEST );
                                        }

                                        if( rand.nextInt( 4 ) == 0 && world.isAirBlock( north ) ) {
                                            this.addHangingVine( world, north, BlockVine.SOUTH );
                                        }

                                        if( rand.nextInt( 4 ) == 0 && world.isAirBlock( south ) ) {
                                            this.addHangingVine( world, south, BlockVine.NORTH );
                                        }
                                    }
                                }
                            }
                        }

                        if( rand.nextInt( 5 ) == 0 && height > 5 ) {
                            for( int y = 0; y < 2; ++ y ) {
                                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                                    if( rand.nextInt( 4 - y ) == 0 ) {
                                        EnumFacing opposite = facing.getOpposite();
                                        this.placeCocoa( world, rand.nextInt( 3 ), position.add( opposite.getXOffset(), height - 5 + y, opposite.getZOffset() ), facing );
                                    }
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    protected int randomHeight( Random rand ) {
        return this.minTreeHeight + rand.nextInt( 3 );
    }

    private void placeCocoa( IWorld world, int age, BlockPos pos, EnumFacing side ) {
        this.setBlockState( world, pos, Blocks.COCOA.getDefaultState().with( BlockCocoa.AGE, age ).with( BlockCocoa.HORIZONTAL_FACING, side ) );
    }

    private void addVine( IWorld world, BlockPos pos, BooleanProperty prop ) {
        this.setBlockState( world, pos, Blocks.VINE.getDefaultState().with( prop, true ) );
    }

    private void addHangingVine( IWorld world, BlockPos pos, BooleanProperty prop ) {
        this.addVine( world, pos, prop );
        int vines = 4;

        for( BlockPos vinePos = pos.down(); world.isAirBlock( vinePos ) && vines > 0; vines-- ) {
            this.addVine( world, vinePos, prop );
            vinePos = vinePos.down();
        }

    }

    protected final void placeLog( Set<BlockPos> changedBlocks, IWorld world, BlockPos pos, IBlockState state ) {
        setLogState( changedBlocks, world, pos, state );
    }

    public MCTreeFeature setSapling( IPlantable sapling ) {
        this.sapling = sapling;
        return this;
    }
}