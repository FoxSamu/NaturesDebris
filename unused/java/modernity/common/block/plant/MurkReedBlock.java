/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.generic.util.MovingBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDNatureBlocks;
import modernity.common.block.fluid.IMurkyWaterloggedBlock;
import modernity.common.block.plant.growing.ReedGrowLogic;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings( "deprecation" )
public class MurkReedBlock extends TallDirectionalPlantBlock implements IMurkyWaterloggedBlock {
    public static final VoxelShape REED_END_SHAPE = makePlantShape( 12, 14 );
    public static final VoxelShape REED_MIDDLE_SHAPE = makePlantShape( 12, 16 );

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;

    public MurkReedBlock( Properties properties ) {
        super( properties, Direction.UP );
        setGrowLogic( new ReedGrowLogic() );
    }

    @Override
    public SoundType getSoundType( BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity ) {
        return state.get( WATERLOGGED ) ? SoundType.WET_GRASS : SoundType.PLANT;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( AGE );
    }

    @Override
    public boolean canBlockSustain( IWorldReader reader, BlockPos pos, BlockState state ) {
        if( state.isIn( MDBlockTags.REEDS_GROWABLE ) ) {
            if( reader.getFluidState( pos.up() ).getFluid() == MDFluids.MURKY_WATER ) {
                return true;
            }
            for( Direction facing : Direction.Plane.HORIZONTAL ) {
                BlockPos pos1 = pos.offset( facing );
                if( reader.getFluidState( pos1 ).getFluid() == MDFluids.MURKY_WATER ) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        Vec3d off = state.getOffset( world, pos );
        return ( state.get( END ) ? REED_END_SHAPE : REED_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return false;
    }


//    @Override
//    public void tick( BlockState state, World world, BlockPos pos, Random random ) {
//        if( state.get( AGE ) < 15 ) {
//            world.setBlockState( pos, state.with( AGE, state.get( AGE ) + 1 ) );
//        } else if( canGrow( world, pos, state ) ) {
//            world.setBlockState( pos, state.with( AGE, 0 ) );
//            world.setBlockState( pos.up(), getDefaultState().with( WATERLOGGED, world.getFluidState( pos.up() ).getFluid() == MDFluids.MURKY_WATER ) );
//        }
//    }

    private boolean canGrow( World world, BlockPos pos, BlockState state ) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState( upPos );
        if( ! upState.isAir( world, upPos ) && upState.getBlock() != MDNatureBlocks.MURKY_WATER ) {
            return false;
        }
        int owHeight = 0, totHeight = 0;
        MovingBlockPos mpos = new MovingBlockPos( pos );
        boolean uw = false;
        while( mpos.getY() >= 0 && state.getBlock() == this && totHeight < 10 ) {
            if( state.get( WATERLOGGED ) ) {
                uw = true;
            }
            if( ! uw ) {
                owHeight++;
            }
            totHeight++;
            mpos.moveDown();
            state = world.getBlockState( mpos );
        }
        return totHeight < 10 && owHeight < 3;
    }

    private boolean blocked( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() && state.getFluidState().getFluid() != MDFluids.MURKY_WATER || isSelfState( world, pos, state );
    }

    @Override
    public boolean canGenerateAt( IWorld world, BlockPos pos, BlockState state ) {
        boolean air = state.isAir( world, pos ) || state.getBlock() == MDNatureBlocks.MURKY_WATER;
        return air && isValidPosition( state, world, pos ) && canBlockSustain( world, pos.down(), world.getBlockState( pos.down() ) );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world, pos, world.getBlockState( pos ) ) ) {
            int uwHeight = rand.nextInt( 10 );
            int owHeight = rand.nextInt( 3 );
            if( rand.nextInt( 4 ) == 0 ) owHeight++;

            int m = 0;
            MovingBlockPos rpos = new MovingBlockPos( pos );

            int height = 0;
            for( int i = 0; i < uwHeight; i++ ) {
                IFluidState state = world.getFluidState( rpos );
                if( state.getFluid() == MDFluids.MURKY_WATER ) {
                    height++;
                } else {
                    break;
                }
                rpos.moveUp();
            }

            for( int i = 0; i < owHeight; i++ ) {
                IFluidState state = world.getFluidState( rpos );
                if( state.getFluid() != MDFluids.MURKY_WATER ) {
                    height++;
                } else {
                    break;
                }
                rpos.moveUp();
            }

            if( height > 10 ) height = 10;

            rpos.setPos( pos );

            for( int i = 0; i < height; i++ ) {
                rpos.moveUp();
                boolean end = i == height - 1;
                if( blocked( world, rpos, world.getBlockState( rpos ) ) ) {
                    end = true;
                }
                rpos.moveDown();
                boolean start = i == 0;
                if( ! blocked( world, rpos, world.getBlockState( rpos ) ) ) {
                    boolean water = world.getFluidState( rpos ).getFluid() == MDFluids.MURKY_WATER;
                    world.setBlockState( rpos, getDefaultState().with( WATERLOGGED, water ).with( ROOT, start ).with( END, end ), 2 | 16 );
                    m++;
                } else {
                    break;
                }
                rpos.moveUp();
            }
            return m > 0;
        }
        return false;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public BlockPos getRootPos( World world, BlockPos pos, BlockState state ) {
        return pos;
    }
}
