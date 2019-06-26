/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.common.block.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.*;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import modernity.api.block.fluid.IGaseousFluid;
import modernity.common.fluid.ImprovedFluid;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockFluid extends BlockBase implements IBucketPickupHandler {

    // The fluid this block represents
    protected final ImprovedFluid fluid;

    // A list of possible fluid states for this fluid
    private final List<IFluidState> fluidStates;

    // Cache to map states to their respective VoxelShape
    private final Map<IBlockState, VoxelShape> stateToShapeCache = Maps.newIdentityHashMap();

    protected final StateContainer<Block, IBlockState> fluidStateContainer;

    private final int maxLevel;
    public final IntegerProperty level;

    public BlockFluid( String id, ImprovedFluid fluid, Block.Properties builder ) {
        super( id, builder );
        this.fluid = fluid;

        maxLevel = fluid.maxLevel;
        level = IntegerProperty.create( "level", 0, maxLevel );


        StateContainer.Builder<Block, IBlockState> containerBuilder = new StateContainer.Builder<>( this );
        containerBuilder.add( level );
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

    @Override
    @SuppressWarnings( "deprecation" )
    public void randomTick( IBlockState state, World world, BlockPos pos, Random random ) {
        // Tick the fluid
        world.getFluidState( pos ).randomTick( world, pos, random );
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean allowsMovement( IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return ! this.fluid.isIn( FluidTags.LAVA );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IFluidState getFluidState( IBlockState state ) {
        int i = state.get( level );
        // Return the fluid state in the list
        return this.fluidStates.get( Math.min( i, maxLevel ) );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public boolean isCollidable( IBlockState state ) {
        return false;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isSideInvisible( IBlockState state, IBlockState adjacentState, EnumFacing side ) {
        return adjacentState.getFluidState().getFluid().isEquivalentTo( this.fluid ) || isSolid( state );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        // Get or compute state
        boolean gas = fluid.getFluid() instanceof IGaseousFluid;
        int fall = gas ? - 1 : 1;
        IFluidState fluidState = world.getFluidState( pos.up( fall ) );
        return fluidState.getFluid().isEquivalentTo( this.fluid ) ? VoxelShapes.fullCube() : this.stateToShapeCache.computeIfAbsent( state, s -> {
            // State not cached, compute...
            IFluidState fluid = s.getFluidState();
            if( gas ) {
                return VoxelShapes.create( 0, 1 - fluid.getHeight(), 0, 1, 1, 1 );
            } else {
                return VoxelShapes.create( 0, 0, 0, 1, (double) fluid.getHeight(), 1 );
            }
        } );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public EnumBlockRenderType getRenderType( IBlockState state ) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World worldIn, BlockPos pos, int fortune ) {
        return Items.AIR;
    }

    @Override
    public int tickRate( IWorldReaderBase worldIn ) {
        return this.fluid.getTickRate( worldIn );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void onBlockAdded( IBlockState state, World world, BlockPos pos, IBlockState oldState ) {
        if( this.reactWithNeighbors( world, pos, state ) ) {
            world.getPendingFluidTicks().scheduleTick( pos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState adjacentState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( state.getFluidState().isSource() || adjacentState.getFluidState().isSource() ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }

        return super.updatePostPlacement( state, facing, adjacentState, world, currentPos, facingPos );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void neighborChanged( IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos ) {
        if( this.reactWithNeighbors( world, pos, state ) ) {
            world.getPendingFluidTicks().scheduleTick( pos, state.getFluidState().getFluid(), this.tickRate( world ) );
        }
    }

    public boolean reactWithNeighbors( World world, BlockPos pos, IBlockState state ) {
        if( this.fluid.isIn( FluidTags.LAVA ) ) {
            boolean shouldReact = false;

            for( EnumFacing facing : EnumFacing.values() ) {
                if( facing != EnumFacing.DOWN && world.getFluidState( pos.offset( facing ) ).isTagged( FluidTags.WATER ) ) {
                    shouldReact = true;
                    break;
                }
            }

            if( shouldReact ) {
                IFluidState fstate = world.getFluidState( pos );
                if( fstate.isSource() ) {
                    world.setBlockState( pos, Blocks.OBSIDIAN.getDefaultState() );
                    triggerMixEffects( world, pos );
                    return false;
                }

                if( fstate.getHeight() >= 0.4444444F ) {
                    world.setBlockState( pos, Blocks.COBBLESTONE.getDefaultState() );
                    triggerMixEffects( world, pos );
                    return false;
                }
            }
        }

        return true;
    }

    protected void triggerMixEffects( IWorld world, BlockPos pos ) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        world.playSound( null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.getRandom().nextFloat() - world.getRandom().nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( Particles.LARGE_SMOKE, x + Math.random(), y + 1.2D, z + Math.random(), 0, 0, 0 );
        }

    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public StateContainer<Block, IBlockState> getStateContainer() {
        return fluidStateContainer;
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, IBlockState state ) {
        if( state.get( level ) == 0 ) { // Only pick up source blocks
            world.setBlockState( pos, Blocks.AIR.getDefaultState(), 11 );
            return fluid;
        } else {
            return Fluids.EMPTY;
        }
    }
}
