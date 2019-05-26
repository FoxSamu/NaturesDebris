package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import modernity.api.util.BlockUpdates;
import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.base.BlockBranch;

import java.util.Random;
import java.util.Set;

public abstract class TreeFeature extends ImprovedFeature<NoFeatureConfig> {

    private final IBlockState leaves;
    private final IBlockState log;
    private final IBlockState branch;

    public TreeFeature( IBlockState leaves, IBlockState log, IBlockState branch ) {
        this.leaves = leaves;
        this.log = log;
        this.branch = branch;
    }

    @Override
    public boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGen, Random rand, BlockPos pos, NoFeatureConfig config ) {
        return generateTree( world, pos, rand );
    }


    public abstract boolean generateTree( IWorld world, BlockPos pos, Random rand );

    public void generateLeaves( IWorld world, BlockPos pos, Random rand, int size, int height, int cornerCutoff, int hangingLeavesLength, int skipChance ) {
        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            int rad = size;
            for( int y = 0; y < height; y++ ) {
                generateLeavesLayer( world, pos, rand, y, rad, cornerCutoff, skipChance, y == 0 ? hangingLeavesLength : - 1, rpos );
                rad--;
            }
        }
    }

    public void generateLeavesLayer( IWorld world, BlockPos pos, Random rand, int yoffset, int size, int cornerCutoff, int skipChance, int hangingLeavesLength, EcoBlockPos rpos ) {
        int cutoffLimit = size * 2 - 1 - cornerCutoff;
        for( int x = - size; x <= size; x++ ) {
            for( int z = - size; z <= size; z++ ) {
                if( skipChance > 0 && rand.nextInt( skipChance ) == 0 ) continue;
                int sum = Math.abs( x ) + Math.abs( z ) - 1;
                if( cornerCutoff <= 0 || sum <= cutoffLimit ) {
                    rpos.setPos( pos );
                    rpos.addPos( x, yoffset, z );

                    IBlockState state = world.getBlockState( rpos );
                    Material mat = state.getMaterial();
                    if( state.canBeReplacedByLeaves( world, rpos ) || mat == Material.VINE ) {
                        world.setBlockState( rpos, leaves, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                        if( hangingLeavesLength > 0 && rand.nextInt( 4 ) == 0 ) {
                            generateHangingLeaves( world, rpos, rand, hangingLeavesLength );
                        }
                    }
                }
            }
        }
    }

    public void generateLog( IWorld world, BlockPos pos, EnumFacing facing, int len, EcoBlockPos rpos ) {
        for( int i = 0; i < len; i++ ) {
            rpos.setPos( pos );
            rpos.move( facing, i );

            IBlockState state = world.getBlockState( rpos );
            if( ! state.getMaterial().blocksMovement() || state.isIn( BlockTags.LEAVES ) ) {
                world.setBlockState( rpos, log.with( BlockStateProperties.AXIS, facing.getAxis() ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            }
        }
    }

    private void generateHangingLeaves( IWorld world, EcoBlockPos pos, Random rand, int maxLen ) {
        int len = rand.nextInt( maxLen + 1 );
        for( int i = 0; i < len; i++ ) {
            pos.moveDown();

            IBlockState state = world.getBlockState( pos );
            Material mat = state.getMaterial();
            if( state.canBeReplacedByLeaves( world, pos ) || mat == Material.VINE ) {
                world.setBlockState( pos, leaves, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            } else {
                break;
            }
        }
    }

    public void generateBranch( IWorld world, BlockPos pos, Random rand, EnumFacing root, EnumFacing facing, int len, int subBranchChance, int leaveConnectChance, EcoBlockPos rpos, EnumFacing.Axis axis ) {
        for( int i = 0; i < len; i++ ) {
            rpos.setPos( pos );
            rpos.move( facing, i );

            int flags = 0;

            boolean leftBranch = subBranchChance > 0 && rand.nextInt( subBranchChance ) == 0;
            boolean rightBranch = subBranchChance > 0 && rand.nextInt( subBranchChance ) == 0;
            if( leftBranch ) {
                EnumFacing left = EnumFacing.getFacingFromAxis( EnumFacing.AxisDirection.NEGATIVE, axis );
                flags |= facingFlag( left );
                rpos.move( left );
                IBlockState branchBlock = generateBranchBlock( world, rpos, rand, left, leaveConnectChance, 0 );
                world.setBlockState( rpos, branchBlock, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            }
            if( rightBranch ) {
                EnumFacing right = EnumFacing.getFacingFromAxis( EnumFacing.AxisDirection.POSITIVE, axis );
                flags |= facingFlag( right );
                rpos.move( right );
                IBlockState branchBlock = generateBranchBlock( world, rpos, rand, right, leaveConnectChance, 0 );
                world.setBlockState( rpos, branchBlock, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            }

            IBlockState state = world.getBlockState( rpos );
            if( ! state.getMaterial().blocksMovement() || state.isIn( BlockTags.LEAVES ) ) {
                world.setBlockState( rpos, generateBranchBlock( world, rpos, rand, i == 0 ? root : facing, leaveConnectChance, flags ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            }
        }
    }

    private IBlockState generateBranchBlock( IWorld world, EcoBlockPos pos, Random rand, EnumFacing root, int leaveConnectChance, int flags ) {
        for( EnumFacing facing : EnumFacing.values() ) {
            int flag = facingFlag( facing );
            if( facing == root.getOpposite() ) {
                flags |= flag;
                continue;
            }

            pos.move( facing );
            if( rand.nextInt( leaveConnectChance ) == 0 && world.getBlockState( pos ).isIn( MDBlockTags.DARKWOOD_LEAVES ) ) {
                flags |= flag;
            }
            pos.move( facing, - 1 );
        }
        return branch( root, flags );
    }

    public int facingFlag( EnumFacing facing ) {
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

    public IBlockState branch( EnumFacing root, boolean n, boolean e, boolean s, boolean w, boolean u, boolean d ) {
        return branch.with( BlockBranch.ROOT, root )
                     .with( BlockBranch.NORTH, n )
                     .with( BlockBranch.EAST, e )
                     .with( BlockBranch.SOUTH, s )
                     .with( BlockBranch.WEST, w )
                     .with( BlockBranch.UP, u )
                     .with( BlockBranch.DOWN, d );
    }

    public IBlockState branch( EnumFacing root, int flags ) {
        return branch.with( BlockBranch.ROOT, root )
                     .with( BlockBranch.NORTH, ( flags & 1 ) > 0 )
                     .with( BlockBranch.EAST, ( flags & 2 ) > 0 )
                     .with( BlockBranch.SOUTH, ( flags & 4 ) > 0 )
                     .with( BlockBranch.WEST, ( flags & 8 ) > 0 )
                     .with( BlockBranch.UP, ( flags & 16 ) > 0 )
                     .with( BlockBranch.DOWN, ( flags & 32 ) > 0 );
    }

    public IBlockState branch( EnumFacing root, Set<EnumFacing> facings ) {
        IBlockState state = branch.with( BlockBranch.ROOT, root );
        state = state.with( BlockBranch.facingProperty( root.getOpposite() ), true );
        for( EnumFacing facing : facings ) {
            state = state.with( BlockBranch.facingProperty( facing ), true );
        }
        return state;
    }
}
