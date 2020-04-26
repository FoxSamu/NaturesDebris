/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.generic.util.EntityUtil;
import modernity.common.block.plant.growing.BushGrowLogic;
import modernity.common.entity.MDEntityTags;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.redgalaxy.exc.UnexpectedCaseException;

import javax.annotation.Nullable;

public class WaterWireBlock extends PlantBlock implements IWaterPlant {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public WaterWireBlock( Properties properties ) {
        super( properties );
        setGrowLogic( new BushGrowLogic( this ) );

        setDefaultState(
            stateContainer
                .getBaseState()
                .with( UP, false )
                .with( NORTH, false )
                .with( EAST, false )
                .with( SOUTH, false )
                .with( WEST, false )
        );
    }

    @Override
    public boolean needsPostProcessing( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return true;
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        state = super.updatePostPlacement( state, dir, adjState, world, pos, adjPos );
        if( state.getBlock() != this ) return state;
        if( dir == Direction.DOWN ) return state;
        BooleanProperty conn = getPropertyForFacing( dir );

        Direction connFace = dir.getOpposite();
        state = state.with( conn, canConnectTo( world, adjPos, adjState, connFace ) );
        return state;
    }

    public boolean canConnectTo( IWorldReader world, BlockPos pos, BlockState state, Direction dir ) {
        return isBlockSideSustainable( state, world, pos, dir ) || isSelfState( state );
    }

    public boolean canRemain( IWorldReader world, BlockPos pos, BlockState state ) {
        BlockPos down = pos.down();
        return canConnectTo( world, down, world.getBlockState( down ), Direction.UP );
    }

    @Override
    public boolean canRemain( IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState ) {
        return dir != Direction.DOWN || canRemain( world, pos, state );
    }

    public boolean isSelfState( BlockState state ) {
        return state.getBlock() == this;
    }

    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos pos ) {
        if( world.getFluidState( pos ).getFluid() != MDFluids.MURKY_WATER ) {
            return false;
        }
        return canRemain( world, pos, state );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getPos();

        BlockState state = getDefaultState();

        for( Direction dir : Direction.values() ) {
            if( dir == Direction.DOWN ) continue;
            BlockPos off = pos.offset( dir );
            BlockState offState = world.getBlockState( off );

            if( canConnectTo( world, off, offState, dir.getOpposite() ) ) {
                state = state.with( getPropertyForFacing( dir ), true );
            }
        }
        return state;
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return false;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( UP, NORTH, EAST, SOUTH, WEST );
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! entity.getType().isContained( MDEntityTags.WATER_WIRE_IMMUNE ) ) {
            EntityUtil.setSmallerMotionMutliplier( state, entity, new Vec3d( 0.65, 0.65, 0.65 ) );
        }
    }

    private static BooleanProperty getPropertyForFacing( Direction dir ) {
        switch( dir ) {
            case UP: return UP;
            case NORTH: return NORTH;
            case EAST: return EAST;
            case SOUTH: return SOUTH;
            case WEST: return WEST;
            default: throw new UnexpectedCaseException( "WWHAT?" );
        }
    }
}
