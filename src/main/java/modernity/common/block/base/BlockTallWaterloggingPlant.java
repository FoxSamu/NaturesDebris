/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.util.EcoBlockPos;
import modernity.api.util.MDVoxelShapes;
import modernity.api.util.MovingBlockPos;
import modernity.client.util.ProxyClient;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class BlockTallWaterloggingPlant extends BlockWaterlogged2 implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxHeight;

    public BlockTallWaterloggingPlant( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockTallWaterloggingPlant( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( BOTTOM, TOP );
    }

    protected int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight( int maxHeight ) {
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean isSolid( IBlockState state ) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public VoxelShape getCollisionShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return VoxelShapes.empty();
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, pos, facingPos );
        if( facing == EnumFacing.DOWN ) {
            state = state.with( BOTTOM, isBottom( world, pos, state ) );
        }
        if( facing == EnumFacing.UP ) {
            state = state.with( TOP, isTop( world, pos, state ) );
        }
        return state;
    }

    public boolean isBottom( IBlockReader world, BlockPos pos, IBlockState state ) {
        return ! isSelfState( world.getBlockState( pos.down() ) );
    }

    public boolean isTop( IBlockReader world, BlockPos pos, IBlockState state ) {
        return ! isSelfState( world.getBlockState( pos.up() ) );
    }


    public boolean canRemainAt( IBlockReader world, BlockPos pos, IBlockState state ) {
        IBlockState down = world.getBlockState( pos.down() );
        if( canBlockSustain( world, pos.down(), down ) ) return true;
        if( ! isSelfState( down ) ) return false;
        int mh = getMaxHeight();
        if( mh > 0 ) {
            EcoBlockPos rpos = EcoBlockPos.retain( pos );
            for( int i = 0; i < mh; i++ ) {
                rpos.moveDown();
                if( canBlockSustain( world, rpos, world.getBlockState( rpos ) ) ) {
                    rpos.release();
                    return true;
                }
            }
            rpos.release();
            return false;
        } else {
            return true;
        }
    }

    public boolean isSelfState( IBlockState state ) {
        return state.getBlock() == this;
    }

    public boolean canBlockSustain( IBlockState state ) {
        return state.isSolid();
    }

    public boolean canBlockSustain( IBlockReader world, BlockPos pos, IBlockState state ) {
        return canBlockSustain( state );
    }

    public boolean canGenerateAt( IBlockReader reader, BlockPos pos, IBlockState state ) {
        return canBlockSustain( reader, pos.down(), reader.getBlockState( pos.down() ) );
    }

    public void destroy( World world, BlockPos pos, IBlockState state ) {
        world.removeBlock( pos );
        state.dropBlockAsItem( world, pos, 0 );
    }

    @Override
    public boolean isReplaceable( IBlockState state, BlockItemUseContext useContext ) {
        return useContext.getItem().getItem() != asItem();
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public void neighborChanged( IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public void onBlockAdded( IBlockState state, World world, BlockPos pos, IBlockState oldState ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public boolean isValidPosition( IBlockState state, IWorldReaderBase world, BlockPos pos ) {
        return canRemainAt( world, pos, state );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return provide( world, pos, rand, rng -> {
            if( rng.nextInt( 10 ) == 0 ) return 2;
            return 1;
        } );
    }

    private boolean blocked( IBlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() || isSelfState( state );
    }

    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightGen ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            int height = heightGen.apply( rand );
            if( height > getMaxHeight() ) height = getMaxHeight();
            int m = 0;
            EcoBlockPos rpos = EcoBlockPos.retain( pos );

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
            rpos.release();
            return m > 0;
        }
        return false;
    }


    @Override
    public IItemProvider getItemDropped( IBlockState state, World worldIn, BlockPos pos, int fortune ) {
        return Items.AIR;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int quantityDropped( IBlockState state, Random random ) {
        return 0;
    }

    public static class ColoredGrass extends BlockTallWaterloggingPlant implements IColoredBlock {
        public static final VoxelShape GRASS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 10, 14 );
        public static final VoxelShape GRASS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );

        public ColoredGrass( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
            setMaxHeight( 4 );
        }

        public ColoredGrass( String id, Properties properties ) {
            super( id, properties );
            setMaxHeight( 4 );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return ProxyClient.get().getGrassColors().getColor( reader, pos );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ProxyClient.get().getGrassColors().getItemColor();
        }

        @Override
        public boolean canBlockSustain( IBlockState state ) {
            return state.getBlock() instanceof BlockDirt;
        }

        @Override
        public EnumOffsetType getOffsetType() {
            return EnumOffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
            Vec3d off = state.getOffset( world, pos );
            return ( state.get( TOP ) ? GRASS_END_SHAPE : GRASS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
        }
    }

    public static class Reeds extends BlockTallWaterloggingPlant {
        public static final VoxelShape REEDS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 14, 14 );
        public static final VoxelShape REEDS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );
        public static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;

        public Reeds( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties.tickRandomly(), itemProps );
        }

        public Reeds( String id, Properties properties ) {
            super( id, properties.tickRandomly() );
        }

        @Override
        protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
            super.fillStateContainer( builder );
            builder.add( AGE );
        }

        @Override
        public boolean canBlockSustain( IBlockReader reader, BlockPos pos, IBlockState state ) {
            if( state.isIn( MDBlockTags.REEDS_GROWABLE ) ) {
                if( reader.getFluidState( pos.up() ).getFluid() == MDFluids.MODERNIZED_WATER ) {
                    return true;
                }
                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                    BlockPos pos1 = pos.offset( facing );
                    if( reader.getFluidState( pos1 ).getFluid() == MDFluids.MODERNIZED_WATER ) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
            Vec3d off = state.getOffset( world, pos );
            return ( state.get( TOP ) ? REEDS_END_SHAPE : REEDS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
        }

        @Override
        public boolean isReplaceable( IBlockState state, BlockItemUseContext useContext ) {
            return false;
        }

        @Override
        public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
            if( state.get( AGE ) < 15 ) {
                world.setBlockState( pos, state.with( AGE, state.get( AGE ) + 1 ) );
            } else if( canGrow( world, pos, state ) ) {
                // Set age to 0 before growing so that the new state receives the grow update...
                world.setBlockState( pos, state.with( AGE, 0 ) );
                world.setBlockState( pos.up(), getDefaultState().with( WATERLOGGED, world.getFluidState( pos.up() ).getFluid() == MDFluids.MODERNIZED_WATER ) );
            }
        }

        private boolean canGrow( World world, BlockPos pos, IBlockState state ) {
            BlockPos upPos = pos.up();
            IBlockState upState = world.getBlockState( upPos );
            if( ! upState.isAir( world, upPos ) && upState.getBlock() != MDBlocks.MODERNIZED_WATER ) {
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

        private boolean blocked( IBlockState state ) {
            return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() && state.getFluidState().getFluid() != MDFluids.MODERNIZED_WATER || isSelfState( state );
        }

        public boolean provide( IWorld world, BlockPos pos, Random rand ) {
            if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
                int uwHeight = rand.nextInt( 10 );
                int owHeight = rand.nextInt( 3 );
                if( rand.nextInt( 4 ) == 0 ) owHeight++;

                int m = 0;
                EcoBlockPos rpos = EcoBlockPos.retain( pos );

                int height = 0;
                for( int i = 0; i < uwHeight; i++ ) {
                    IFluidState state = world.getFluidState( rpos );
                    if( state.getFluid() == MDFluids.MODERNIZED_WATER ) {
                        height++;
                    } else {
                        break;
                    }
                    rpos.moveUp();
                }

                for( int i = 0; i < owHeight; i++ ) {
                    IFluidState state = world.getFluidState( rpos );
                    if( state.getFluid() != MDFluids.MODERNIZED_WATER ) {
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
                        boolean water = world.getFluidState( rpos ).getFluid() == MDFluids.MODERNIZED_WATER;
                        world.setBlockState( rpos, getDefaultState().with( WATERLOGGED, water ).with( BOTTOM, start ).with( TOP, end ), 2 | 16 );
                        m++;
                    } else {
                        break;
                    }
                    rpos.moveUp();
                }
                rpos.release();
                return m > 0;
            }
            return false;
        }
    }
}
