/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.util.EcoBlockPos;
import modernity.api.util.MDVoxelShapes;
import modernity.client.util.ProxyClient;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

public class BlockHangingPlant extends BlockNoDrop implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxLength;

    public BlockHangingPlant( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockHangingPlant( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( BOTTOM, TOP );
    }

    protected int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength( int len ) {
        this.maxLength = len;
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
        IBlockState up = world.getBlockState( pos.up() );
        if( canBlockSustain( up ) ) return true;
        if( ! isSelfState( up ) ) return false;
        int len = getMaxLength();
        if( len > 0 ) {
            EcoBlockPos rpos = EcoBlockPos.retain();
            for( int i = 0; i < len; i++ ) {
                rpos.moveUp();
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
        return canBlockSustain( reader.getBlockState( pos.up() ) );
    }

    public void destroy( World world, BlockPos pos, IBlockState state ) {
        world.removeBlock( pos );
        state.dropBlockAsItem( world, pos, 0 );
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
        return provide( world, pos, rand, rng -> rand.nextInt( 8 ) + 1 );
    }

    private boolean blocked( IBlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() || isSelfState( state );
    }

    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightGen ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            int len = heightGen.apply( rand );
            int max = getMaxLength();
            if( max > 0 && len > max ) len = max;
            int m = 0;
            EcoBlockPos rpos = EcoBlockPos.retain( pos );

            for( int i = 0; i < len; i++ ) {
                rpos.moveDown();
                boolean end = i == len - 1;
                if( blocked( world.getBlockState( rpos ) ) ) {
                    end = true;
                }
                rpos.moveUp();
                boolean start = i == 0;
                if( ! blocked( world.getBlockState( rpos ) ) ) {
                    world.setBlockState( rpos, getDefaultState().with( BOTTOM, end ).with( TOP, start ), 2 | 16 );
                    m++;
                } else {
                    break;
                }
                rpos.moveDown();
            }
            rpos.release();
            return m > 0;
        }
        return false;
    }

    public static class ColoredMurina extends BlockHangingPlant implements IColoredBlock {
        public static final VoxelShape MURINA_SHAPE = MDVoxelShapes.create16( 5, 0, 5, 11, 16, 11 );

        public ColoredMurina( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public ColoredMurina( String id, Properties properties ) {
            super( id, properties );
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
            return state.isFullCube();
        }

        @Override
        public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
            return MURINA_SHAPE;
        }
    }
}
