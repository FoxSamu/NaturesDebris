package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import modernity.api.util.BlockUpdates;
import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.BlockBranch;

import java.util.Random;
import java.util.Set;

public class DarkwoodTreeFeature extends TreeFeature {

    public DarkwoodTreeFeature( IBlockState leaves, IBlockState log, IBlockState branch ) {
        super( leaves, log, branch );
    }

    private boolean isSustainable( IBlockState state ) {
        return state.getBlock() == MDBlocks.DARK_GRASS || state.getBlock() == MDBlocks.DARK_DIRT;
    }

    @Override
    public boolean generateTree( Set<BlockPos> changed, IWorld world, BlockPos pos, Random rand ) {
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
            world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            changed.add( rpos.toImmutable() );
        }

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            rpos.setPos( pos );
            rpos.move( facing );

            if( ! world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                IBlockState branch = BlockBranch.withFluid( branch( facing, 32 | facingFlag( facing.getOpposite() ) ), world, rpos );
                world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                changed.add( rpos.toImmutable() );
            }

            rpos.moveUp( leaveHeight );

            IBlockState branch = BlockBranch.withFluid( branch( facing, true, true, true, true, true, false ), world, rpos );
            world.setBlockState( rpos, branch, BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            changed.add( rpos.toImmutable() );
        }

        rpos.release();
        return true;
    }
}
