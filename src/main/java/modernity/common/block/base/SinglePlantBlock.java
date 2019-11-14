/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.util.IBlockProvider;
import modernity.api.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Describes a single plant block. This is a one-block plant that usually grows on the floor.
 */
@SuppressWarnings( "deprecation" )
public class SinglePlantBlock extends Block implements IBlockProvider {
    public SinglePlantBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }

    /**
     * Checks whether this plant can remain at the specified position in the specified world.
     */
    public boolean canRemainAt( IBlockReader world, BlockPos pos, BlockState state ) {
        return canBlockSustain( world.getBlockState( pos.down() ) );
    }

    /**
     * Checks whether the specified block state can sustain this plant.
     */
    public boolean canBlockSustain( BlockState state ) {
        return state.isSolid();
    }

    /**
     * Checks whether the specified block at specified location can sustain this plant.
     */
    public boolean canBlockSustain( IBlockReader reader, BlockPos pos, BlockState state ) {
        return canBlockSustain( state );
    }

    /**
     * Destroys this plant.
     */
    public void destroy( World world, BlockPos pos, BlockState state ) {
        world.removeBlock( pos, false );
        spawnDrops( state, world, pos );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return true;
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


    public boolean isSelfState( BlockState state ) {
        return state.getBlock() == this;
    }

    private boolean blocked( BlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() || isSelfState( state );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( canRemainAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            world.setBlockState( pos, getDefaultState(), 2 | 16 );
            return true;
        }
        return false;
    }

    /**
     * Describes a melion block.
     */
    public static class Melion extends SinglePlantBlock {
        public static final VoxelShape MELION_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 12, 14 );

        public Melion( Properties properties ) {
            super( properties );
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
            return MELION_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    /**
     * Describes a millium block.
     */
    public static class Millium extends SinglePlantBlock {
        public static final VoxelShape MILLIUM_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 8, 15 );

        public Millium( Properties properties ) {
            super( properties.lightValue( 5 ) );
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
            return MILLIUM_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    /**
     * Describes a mint plant block.
     */
    public static class Mint extends SinglePlantBlock {
        public static final VoxelShape MINT_SHAPE = MDVoxelShapes.create16( 1, 0, 1, 15, 9, 15 );

        public Mint( Properties properties ) {
            super( properties );
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
            return MINT_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }

    /**
     * Describes a redwold block.
     */
    public static class Redwold extends SinglePlantBlock {
        public static final VoxelShape REDWOLD_SHAPE = MDVoxelShapes.create16( 0, 0, 0, 16, 1, 16 );

        public Redwold( Properties properties ) {
            super( properties );
        }

        @Override
        public boolean canBlockSustain( IBlockReader reader, BlockPos pos, BlockState state ) {
            return state.func_224755_d( reader, pos, Direction.UP );
        }

        @Override
        public OffsetType getOffsetType() {
            return OffsetType.NONE;
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            Vec3d off = state.getOffset( world, pos );
            return REDWOLD_SHAPE.withOffset( off.x, off.y, off.z );
        }
    }
}
