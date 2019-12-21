/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.util.IBlockProvider;
import modernity.api.util.MDVoxelShapes;
import modernity.api.util.MovingBlockPos;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

/**
 * Describes a tall plant that can grow in and out water.
 */
@SuppressWarnings( "deprecation" )
public class TallWaterloggingPlantBlock extends MurkyWaterloggedBlock implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxHeight;

    public TallWaterloggingPlantBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( BOTTOM, TOP );
    }

    /**
     * Returns the maximum height of this plant (it can't be higher)
     */
    protected int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the maximum height of this plant
     */
    public void setMaxHeight( int maxHeight ) {
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, pos, facingPos );
        if( facing == Direction.DOWN ) {
            state = state.with( BOTTOM, isBottom( world, pos, state ) );
        }
        if( facing == Direction.UP ) {
            state = state.with( TOP, isTop( world, pos, state ) );
        }
        return state;
    }

    /**
     * Checks if this is the lowest part of the plant
     */
    public boolean isBottom( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.down() ) );
    }

    /**
     * Checks if this is the highest part of the plant.
     */
    public boolean isTop( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.up() ) );
    }


    /**
     * Checks if the plant can remain in the specific context.
     */
    public boolean canRemainAt( IBlockReader world, BlockPos pos, BlockState state ) {
        BlockState down = world.getBlockState( pos.down() );
        if( canBlockSustain( world, pos.down(), down ) ) return true;
        if( ! isSelfState( down ) ) return false;
        int mh = getMaxHeight();
        if( mh > 0 ) {
            MovingBlockPos rpos = new MovingBlockPos( pos );
            for( int i = 0; i < mh; i++ ) {
                rpos.moveDown();
                if( canBlockSustain( world, rpos, world.getBlockState( rpos ) ) ) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Is the specified block state this block?
     */
    public boolean isSelfState( BlockState state ) {
        return state.getBlock() == this;
    }

    /**
     * Checks if the specified block state can sustain this plant.
     */
    public boolean canBlockSustain( BlockState state ) {
        return state.isSolid();
    }

    /**
     * Checks if the block state in the specified context can sustain this plant.
     */
    public boolean canBlockSustain( IBlockReader world, BlockPos pos, BlockState state ) {
        return canBlockSustain( state );
    }

    /**
     * Checks if this plant can generate at the specified location.
     */
    public boolean canGenerateAt( IBlockReader reader, BlockPos pos, BlockState state ) {
        return canBlockSustain( reader, pos.down(), reader.getBlockState( pos.down() ) );
    }

    /**
     * Destroys this plant, spawning its drops
     */
    public void destroy( World world, BlockPos pos, BlockState state ) {
        world.removeBlock( pos, false );
        spawnDrops( state, world, pos );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return useContext.getItem().getItem() != asItem();
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean moving ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos pos ) {
        return canRemainAt( world, pos, state );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return provide( world, pos, rand, rng -> {
            if( rng.nextInt( 10 ) == 0 ) return 2;
            return 1;
        } );
    }

    private boolean blocked( BlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() && state.getFluidState().getFluid() != MDFluids.MURKY_WATER || isSelfState( state );
    }

    /**
     * Generates this plant at the specified position
     * @param world     The world to generate in
     * @param pos       The position to generate at
     * @param rand      A random number generator
     * @param heightGen A function that generates a random height
     * @return True if something was generated
     */
    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightGen ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            int height = heightGen.apply( rand );
            if( height > getMaxHeight() ) height = getMaxHeight();
            int m = 0;
            MovingBlockPos rpos = new MovingBlockPos( pos );

            for( int i = 0; i < height; i++ ) {
                rpos.moveUp();
                boolean end = i == height - 1;
                if( blocked( world.getBlockState( rpos ) ) ) {
                    end = true;
                }
                rpos.moveDown();
                boolean start = i == 0;
                if( ! blocked( world.getBlockState( rpos ) ) ) {
                    world.setBlockState( rpos, getDefaultState().with( BOTTOM, start ).with( TOP, end ), 2 | 16 );
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

    /**
     * Grass-colored tall waterlogged plant.
     */
    public static class ColoredGrass extends TallWaterloggingPlantBlock implements IColoredBlock {
        public static final VoxelShape GRASS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 10, 14 );
        public static final VoxelShape GRASS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );

        public ColoredGrass( Properties properties ) {
            super( properties );
            setMaxHeight( 4 );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getColor( reader, pos );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getItemColor();
        }

        @Override
        public boolean canBlockSustain( BlockState state ) {
            return state.getBlock() instanceof DirtBlock;
        }

        @Override
        public OffsetType getOffsetType() {
            return OffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            Vec3d off = state.getOffset( world, pos );
            return ( state.get( TOP ) ? GRASS_END_SHAPE : GRASS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
        }
    }

    /**
     * Describes the reeds blocks.
     */
    public static class Reeds extends TallWaterloggingPlantBlock {
        public static final VoxelShape REEDS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 14, 14 );
        public static final VoxelShape REEDS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );
        public static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;

        public Reeds( Properties properties ) {
            super( properties.tickRandomly() );
        }

        @Override
        protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
            super.fillStateContainer( builder );
            builder.add( AGE );
        }

        @Override
        public boolean canBlockSustain( IBlockReader reader, BlockPos pos, BlockState state ) {
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
            return ( state.get( TOP ) ? REEDS_END_SHAPE : REEDS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
        }

        @Override
        public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
            return false;
        }

        @Override
        public void tick( BlockState state, World world, BlockPos pos, Random random ) {
            if( state.get( AGE ) < 15 ) {
                world.setBlockState( pos, state.with( AGE, state.get( AGE ) + 1 ) );
            } else if( canGrow( world, pos, state ) ) {
                // Set age to 0 before growing so that the new state receives the grow update...
                world.setBlockState( pos, state.with( AGE, 0 ) );
                world.setBlockState( pos.up(), getDefaultState().with( WATERLOGGED, world.getFluidState( pos.up() ).getFluid() == MDFluids.MURKY_WATER ) );
            }
        }

        private boolean canGrow( World world, BlockPos pos, BlockState state ) {
            BlockPos upPos = pos.up();
            BlockState upState = world.getBlockState( upPos );
            if( ! upState.isAir( world, upPos ) && upState.getBlock() != MDBlocks.MURKY_WATER ) {
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

        private boolean blocked( BlockState state ) {
            return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() && state.getFluidState().getFluid() != MDFluids.MURKY_WATER || isSelfState( state );
        }

        @Override
        public boolean provide( IWorld world, BlockPos pos, Random rand ) {
            if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
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
                    if( blocked( world.getBlockState( rpos ) ) ) {
                        end = true;
                    }
                    rpos.moveDown();
                    boolean start = i == 0;
                    if( ! blocked( world.getBlockState( rpos ) ) ) {
                        boolean water = world.getFluidState( rpos ).getFluid() == MDFluids.MURKY_WATER;
                        world.setBlockState( rpos, getDefaultState().with( WATERLOGGED, water ).with( BOTTOM, start ).with( TOP, end ), 2 | 16 );
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
    }
}
