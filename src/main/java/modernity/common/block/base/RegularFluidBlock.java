/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.block.base;

import com.google.common.collect.Lists;
import modernity.common.fluid.RegularFluid;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

@SuppressWarnings( "deprecation" )
public class RegularFluidBlock extends Block implements IBucketPickupHandler {

    // The fluid this block represents
    protected final RegularFluid fluid;

    // A list of possible fluid states for this fluid
    private final List<IFluidState> fluidStates;

    protected final StateContainer<Block, BlockState> fluidStateContainer;

    private final int maxLevel;
    public final IntegerProperty level;

    public RegularFluidBlock( RegularFluid fluid, Block.Properties builder ) {
        super( builder );
        this.fluid = fluid;

        maxLevel = fluid.maxLevel;
        level = IntegerProperty.create( "level", 0, maxLevel );


        StateContainer.Builder<Block, BlockState> containerBuilder = new StateContainer.Builder<>( this );
        fillFluidStateContainer( containerBuilder );
        fluidStateContainer = containerBuilder.create( BlockState::new );

        // Fill the list with fluid states
        fluidStates = Lists.newArrayList();
        fluidStates.add( fluid.getStillFluidState( false ) );
        for( int i = 1; i < maxLevel; ++ i ) {
            fluidStates.add( fluid.getFlowingFluidState( maxLevel - i, false ) );
        }
        fluidStates.add( fluid.getFlowingFluidState( maxLevel, true ) );

        // Default is source state
        setDefaultState( fluidStateContainer.getBaseState().with( level, 0 ) );
    }

    protected void fillFluidStateContainer( StateContainer.Builder<Block, BlockState> container ) {
        container.add( level );
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random random ) {
        // Tick the fluid
        world.getFluidState( pos ).randomTick( world, pos, random );
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return ! this.fluid.isIn( FluidTags.LAVA );
    }

    @Override
    public IFluidState getFluidState( BlockState state ) {
        int i = state.get( level );
        // Return the fluid state in the list
        return this.fluidStates.get( Math.min( i, maxLevel ) );
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentState, Direction side ) {
        return adjacentState.getFluidState().getFluid().isEquivalentTo( this.fluid ) || isSolid( state );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
//        // Get or compute state
//        boolean gas = fluid.getFluid() instanceof IGaseousFluid;
//        int fall = gas ? - 1 : 1;
//        IFluidState fluidState = world.getFluidState( pos.up( fall ) );
//        return fluidState.getFluid().isEquivalentTo( this.fluid ) ? VoxelShapes.fullCube() : this.stateToShapeCache.computeIfAbsent( state, s -> {
//            // State not cached, compute...
//            IFluidState fluid = s.getFluidState();
//            if( gas ) {
//                return VoxelShapes.create( 0, 1 - fluid.func_223408_f(), 0, 1, 1, 1 );
//            } else {
//                return VoxelShapes.create( 0, 0, 0, 1, (double) fluid.func_223408_f(), 1 );
//            }
//        } );
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public int tickRate( IWorldReader world ) {
        return this.fluid.getTickRate( world );
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving ) {
        if( this.reactWithNeighbors( world, pos, state ) ) {
            world.getPendingFluidTicks().scheduleTick( pos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState adjacentState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( state.getFluidState().isSource() || adjacentState.getFluidState().isSource() ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }

        return super.updatePostPlacement( state, facing, adjacentState, world, currentPos, facingPos );
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving ) {
        if( this.reactWithNeighbors( world, pos, state ) ) {
            world.getPendingFluidTicks().scheduleTick( pos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }
    }

    public boolean reactWithNeighbors( World world, BlockPos pos, BlockState state ) {
        return this.fluid.reactWithNeighbors( world, pos, state );
    }

    @Override
    public StateContainer<Block, BlockState> getStateContainer() {
        return fluidStateContainer;
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        if( state.get( level ) == 0 ) { // Only pick up source blocks
            world.setBlockState( pos, Blocks.AIR.getDefaultState(), 11 );
            return fluid;
        } else {
            return Fluids.EMPTY;
        }
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( this.fluid.isIn( FluidTags.LAVA ) ) {
            entity.setInLava();
        }
    }
}
