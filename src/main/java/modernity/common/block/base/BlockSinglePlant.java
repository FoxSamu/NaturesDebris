/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
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

import modernity.api.util.MDVoxelShapes;
import modernity.common.world.gen.decorate.util.IBlockProvider;

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
        if( canRemainAt( world, pos, world.getBlockState( pos ) ) && ! world.getBlockState( pos ).getMaterial().blocksMovement() ) {
            world.setBlockState( pos, getDefaultState(), 2 | 16 );
            return true;
        }
        return false;
    }

    public static class Melion extends BlockSinglePlant {
        public static final VoxelShape MELION_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 12, 14 );

        public Melion( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Melion( String id, Properties properties ) {
            super( id, properties );
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
            return MELION_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    public static class Millium extends BlockSinglePlant {
        public static final VoxelShape MILLIUM_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 8, 15 );

        public Millium( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties.lightValue( 5 ), itemProps );
        }

        public Millium( String id, Properties properties ) {
            super( id, properties.lightValue( 5 ) );
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
            return MILLIUM_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    public static class Mint extends BlockSinglePlant {
        public static final VoxelShape MINT_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 9, 15 );

        public Mint( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Mint( String id, Properties properties ) {
            super( id, properties );
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
            return MINT_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    public static class Redwold extends BlockSinglePlant {
        public static final VoxelShape REDWOLD_SHAPE = MDVoxelShapes.create16( 0, 0, 0, 16, 1, 16 );

        public Redwold( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Redwold( String id, Properties properties ) {
            super( id, properties );
        }

        @Override
        public boolean canBlockSustain( IBlockState state ) {
            return state.isTopSolid();
        }

        @Override
        public EnumOffsetType getOffsetType() {
            return EnumOffsetType.NONE;
        }

        @Override
        public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
            Vec3d off = state.getOffset( world, pos );
            return REDWOLD_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }
}
