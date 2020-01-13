/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.EWaterlogType;
import modernity.api.util.IBlockProvider;
import modernity.common.block.base.IMurkyWaterloggedBlock;
import modernity.common.block.base.IWaterloggedBlock;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class PlantBlock extends Block implements IBlockProvider {

    public PlantBlock( Properties properties ) {
        super( properties );

        BlockState def = stateContainer.getBaseState();
        if( this instanceof IMurkyWaterloggedBlock ) {
            def = def.with( IMurkyWaterloggedBlock.WATERLOGGED, false );
        }
        if( this instanceof IWaterloggedBlock ) {
            def = def.with( IWaterloggedBlock.WATERLOGGED, EWaterlogType.NONE );
        }
        setDefaultState( def );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        if( this instanceof IMurkyWaterloggedBlock ) {
            builder.add( IMurkyWaterloggedBlock.WATERLOGGED );
        }
        if( this instanceof IWaterloggedBlock ) {
            builder.add( IWaterloggedBlock.WATERLOGGED );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isSolid( BlockState state ) {
        return false;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos poas, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean allowsMovement( BlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        return true;
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        if( this instanceof IMurkyWaterloggedBlock || this instanceof IWaterloggedBlock ) {
            Fluid fluid = world.getFluidState( pos ).getFluid();
            world.getPendingFluidTicks().scheduleTick( pos, fluid, fluid.getTickRate( world ) );
        }

        if( isValidPosition( state, world, pos ) ) {
            return state;
        }
        return Blocks.AIR.getDefaultState();
    }

    public boolean canRemain( IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState ) {
        return true;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos pos ) {
        for( Direction dir : Direction.values() ) {
            BlockPos off = pos.offset( dir );
            BlockState offState = world.getBlockState( off );
            if( ! canRemain( world, pos, state, dir, off, offState ) ) return false;
        }
        return true;
    }

    public boolean canGenerateAt( IWorld world, BlockPos pos, BlockState state ) {
        return state.isAir( world, pos ) && isValidPosition( state, world, pos );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        BlockState state = computeStateForPos( ctx.getWorld(), ctx.getPos(), getDefaultState() );

        if( this instanceof IMurkyWaterloggedBlock ) {
            IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
            state = state.with( IMurkyWaterloggedBlock.WATERLOGGED, fluid.getFluid() == MDFluids.MURKY_WATER );
        }

        if( this instanceof IWaterloggedBlock ) {
            IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
            return getDefaultState().with( IWaterloggedBlock.WATERLOGGED, EWaterlogType.getType( fluid ) );
        }

        return state;
    }

    protected BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        return state;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IFluidState getFluidState( BlockState state ) {
        if( this instanceof IMurkyWaterloggedBlock ) {
            return state.get( IMurkyWaterloggedBlock.WATERLOGGED )
                   ? MDFluids.MURKY_WATER.getDefaultState()
                   : Fluids.EMPTY.getDefaultState();
        }

        if( this instanceof IWaterloggedBlock ) {
            return state.get( IWaterloggedBlock.WATERLOGGED ).getFluidState();
        }

        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( this instanceof IDangerousPlant ) {
            IDangerousPlant dangerous = (IDangerousPlant) this;
            if( dangerous.dealsDamage( world, pos, state, entity ) ) {
                DamageSource src = dangerous.getDamageSource( world, pos, state, entity );
                float damage = dangerous.getDamage( world, pos, state, entity );

                entity.attackEntityFrom( src, damage );
            }
        }
    }
}
