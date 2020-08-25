/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.tree;

import modernity.common.blockold.MDBlockTags;
import modernity.common.blockold.plant.SimplePlantBlock;
import modernity.common.generator.tree.Tree;
import modernity.common.itemold.MDItemTags;
import modernity.generic.util.Events;
import modernity.generic.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Describes a sapling.
 */
public abstract class AbstractSaplingBlock extends SimplePlantBlock {
    public static final VoxelShape SHAPE = MDVoxelShapes.create16(2, 0, 2, 14, 12, 14);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;

    /**
     * Creates a sapling block.
     */
    public AbstractSaplingBlock(Properties properties) {
        super(properties, SHAPE);
        setDefaultState(stateContainer.getBaseState().with(AGE, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(AGE);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        growOlder(state, world, pos, rand);
        super.randomTick(state, world, pos, rand);
    }

    /**
     * Grows the sapling.
     */
    public void growOlder(BlockState state, IWorld world, BlockPos pos, Random rand) {
        if(!world.isRemote()) {
            if(state.get(AGE) == 5) {
                long seed = rand.nextLong();
                Random local = new Random();
                local.setSeed(seed);

                int growState = findGrowState(world, pos);
                BlockPos growPos = getGrowPos(world, pos, growState);
                Tree tree = getTree(world, pos, growState);

                if(tree != null && tree.canGenerate(world, local, growPos)) {
                    removeSaplings(world, pos, growState);

                    local.setSeed(seed);
                    tree.generate(world, local, growPos);
                }
            } else {
                world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), 3);
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if(player.getHeldItem(hand).getItem().isIn(MDItemTags.FERTILIZER)) {
            growOlder(state, world, pos, world.rand);
            world.playEvent(Events.FERTILIZER_USE, pos, 0);

            if(!player.abilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }

            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.DIRTLIKE);
    }

    protected abstract int findGrowState(IWorld world, BlockPos pos);

    protected abstract BlockPos getGrowPos(IWorld world, BlockPos pos, int growState);
    protected abstract Tree getTree(IWorld world, BlockPos pos, int growState);
    protected abstract void removeSaplings(IWorld world, BlockPos pos, int growState);
}
