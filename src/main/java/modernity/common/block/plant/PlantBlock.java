/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.MDVoxelShapes;
import modernity.common.block.MDBlocks;
import modernity.common.block.fluid.IMurkyWaterloggedBlock;
import modernity.common.block.fluid.IWaterloggedBlock;
import modernity.common.block.fluid.WaterlogType;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class PlantBlock extends Block {

    public PlantBlock( Properties properties ) {
        super( properties );

        BlockState def = stateContainer.getBaseState();
        if( this instanceof IMurkyWaterloggedBlock ) {
            def = def.with( IMurkyWaterloggedBlock.WATERLOGGED, false );
        }
        if( this instanceof IWaterloggedBlock ) {
            def = def.with( IWaterloggedBlock.WATERLOGGED, WaterlogType.NONE );
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
        if( this instanceof IMurkyWaterloggedBlock || this instanceof IWaterloggedBlock || this instanceof IWaterPlant ) {
            Fluid fluid = world.getFluidState( pos ).getFluid();
            world.getPendingFluidTicks().scheduleTick( pos, fluid, fluid.getTickRate( world ) );
        }

        if( state.isValidPosition( world, pos ) ) {
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
        if( this instanceof IWaterPlant ) {
            if( world.getFluidState( pos ).getFluid() != MDFluids.MURKY_WATER ) {
                return false;
            }
        }
        for( Direction dir : Direction.values() ) {
            BlockPos off = pos.offset( dir );
            BlockState offState = world.getBlockState( off );
            if( ! canRemain( world, pos, state, dir, off, offState ) ) return false;
        }
        return true;
    }

    public boolean canGenerateAt( IWorld world, BlockPos pos, BlockState state ) {
        boolean air = state.isAir( world, pos );
        if( this instanceof IWaterPlant ) {
            air = state.getFluidState().getFluid() == MDFluids.MURKY_WATER && state.getBlock() == MDBlocks.MURKY_WATER;
        }
        return air && isValidPosition( state, world, pos );
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
            return getDefaultState().with( IWaterloggedBlock.WATERLOGGED, WaterlogType.getType( fluid ) );
        }

        return state;
    }

    public BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        return state;
    }

    public BlockState computeStateForPos( IWorldReader world, BlockPos pos ) {
        return computeStateForPos( world, pos, getDefaultState() );
    }

    public BlockState computeStateForGeneration( IWorldReader world, BlockPos pos, Random rand ) {
        return computeStateForPos( world, pos );
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

        if( this instanceof IWaterPlant ) {
            return MDFluids.MURKY_WATER.getDefaultState();
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

    public boolean isBlockSideSustainable( BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction side ) {
        Block block = state.getBlock();
        if( block instanceof IPlantSustainer ) {
            return ( (IPlantSustainer) block ).canSustainPlant( world, pos, state, this, side );
        } else {
            return state.isSolid() && state.func_224755_d( world, pos, side );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        VoxelShape shape = getShape( state );
        Vec3d offset = state.getOffset( world, pos );
        return shape.withOffset( offset.x, offset.y, offset.z );
    }

    public VoxelShape getShape( BlockState state ) {
        return VoxelShapes.fullCube();
    }

    public static VoxelShape makePlantShape( double width, double height ) {
        return MDVoxelShapes.plantShape( width, height );
    }

    public static VoxelShape makeHangPlantShape( double width, double height ) {
        return MDVoxelShapes.hangPlantShape( width, height );
    }

    @Deprecated
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return false;
    }
}
