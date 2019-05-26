package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.client.util.MDBiomeValues;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.util.IBlockProvider;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSinglePlant extends BlockNoDrop implements IBlockProvider {
    public BlockSinglePlant( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockSinglePlant( String id, Properties properties ) {
        super( id, properties );
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

    public boolean canRemainAt( IBlockReader world, BlockPos pos, IBlockState state ) {
        return canBlockSustain( world.getBlockState( pos.down() ) );
    }

    public boolean canBlockSustain( IBlockState state ) {
        return state.isSolid();
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
        if( canRemainAt( world, pos, world.getBlockState( pos ) ) && ! world.getBlockState( pos ).getMaterial().blocksMovement() ) {
            world.setBlockState( pos, getDefaultState(), 2 | 16 );
            return true;
        }
        return false;
    }

    public static class ColoredGrass extends BlockSinglePlant implements IColoredBlock {
        protected static final int GRASS_ITEM_COLOR = ColorUtil.rgb( 0, 109, 38 );

        public ColoredGrass( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public ColoredGrass( String id, Properties properties ) {
            super( id, properties );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return MDBiomeValues.get( reader, pos, MDBiomeValues.GRASS_COLOR );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return GRASS_ITEM_COLOR;
        }

        @Override
        public boolean canBlockSustain( IBlockState state ) {
            return state.getBlock() == MDBlocks.DARK_DIRT || state.getBlock() == MDBlocks.DARK_GRASS;
        }

        @Override
        public EnumOffsetType getOffsetType() {
            return EnumOffsetType.XZ;
        }
    }
}
