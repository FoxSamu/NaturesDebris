/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.util.BlockUpdates;
import modernity.api.util.MDVoxelShapes;
import modernity.common.item.MDItemTags;
import modernity.common.world.gen.tree.Tree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Describes a sapling.
 */
public class SaplingBlock extends SinglePlantBlock {
    public static final VoxelShape SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 12, 14 );

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;

    private final Supplier<Tree> tree;

    /**
     * Creates a sapling block.
     *
     * @param tree The tree feature to generate when this sapling is full grown.
     */
    public SaplingBlock( Supplier<Tree> tree, Properties properties ) {
        super( properties );
        setDefaultState( stateContainer.getBaseState().with( AGE, 0 ) );
        this.tree = tree;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( AGE );
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random rand ) {
        growOlder( state, world, pos, rand );
        super.randomTick( state, world, pos, rand );
    }

    /**
     * Grows the sapling.
     */
    public void growOlder( BlockState state, IWorld world, BlockPos pos, Random rand ) {
        if( world.isRemote() ) {
            if( state.get( AGE ) == 5 ) {
                long seed = rand.nextLong();
                Random local = new Random( seed );
                if( tree.get().canGenerate( world, local, pos ) ) {
                    world.removeBlock( pos, false );

                    local.setSeed( seed );
                    tree.get().generate( world, local, pos );
                }
            } else {
                world.setBlockState( pos, state.with( AGE, state.get( AGE ) + 1 ), BlockUpdates.CAUSE_UPDATE | BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_RENDER | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            }
        }
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result ) {
        if( player.getHeldItem( hand ).getItem().isIn( MDItemTags.FERTILIZER ) ) {
            growOlder( state, world, pos, world.rand );
            world.playEvent( 2005, pos, 0 );
            return true;
        }
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }
}
