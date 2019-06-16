/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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

import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.api.util.EcoBlockPos;
import modernity.api.util.MDVoxelShapes;
import modernity.client.util.BiomeValues;
import modernity.common.world.gen.decorate.util.IBlockProvider;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class BlockTallPlant extends BlockNoDrop implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxHeight;

    public BlockTallPlant( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockTallPlant( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
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
        if( canBlockSustain( down ) ) return true;
        if( ! isSelfState( down ) ) return false;
        int mh = getMaxHeight();
        if( mh > 0 ) {
            EcoBlockPos rpos = EcoBlockPos.retain( pos );
            for( int i = 0; i < mh; i++ ) {
                rpos.moveDown();
                if( canBlockSustain( world.getBlockState( rpos ) ) ) {
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

    public boolean canGenerateAt( IBlockReader reader, BlockPos pos, IBlockState state ) {
        return canBlockSustain( reader.getBlockState( pos.down() ) );
    }

    public void destroy( World world, BlockPos pos, IBlockState state ) {
        world.setBlockState( pos, Blocks.AIR.getDefaultState(), 3 );
        dropBlockAsItemWithChance( state, world, pos, 1, 0 );
    }

    @Override
    public boolean isReplaceable( IBlockState state, BlockItemUseContext useContext ) {
        return true;
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

    public static class ColoredGrass extends BlockTallPlant implements IColoredBlock {
        public static final VoxelShape GRASS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 10, 14 );
        public static final VoxelShape GRASS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );

        protected static final int GRASS_ITEM_COLOR = ColorUtil.rgb( 0, 109, 38 );

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
            return BiomeValues.get( reader, pos, BiomeValues.GRASS_COLOR );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return GRASS_ITEM_COLOR;
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
}
