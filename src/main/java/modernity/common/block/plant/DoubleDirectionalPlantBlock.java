/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.Events;
import modernity.common.block.prop.IntEnumProperty;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class DoubleDirectionalPlantBlock extends DirectionalPlantBlock {
    public static final int ROOT = 0;
    public static final int END = 1;
    public static final IntEnumProperty TYPE = IntEnumProperty.builder( "type" )
                                                              .with( ROOT, "root" )
                                                              .with( END, "end" )
                                                              .create();

    public DoubleDirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties, growDir );

        setDefaultState( stateContainer.getBaseState().with( TYPE, ROOT ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( TYPE );
    }

    public boolean isSelfState( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.getBlock() == this;
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        state = super.updatePostPlacement( state, dir, adjState, world, pos, adjPos );
        if( state.getBlock() != this ) return state;

        int type = state.get( TYPE );
        if( dir == growDir && type == ROOT || dir == growDir.getOpposite() && type == END ) {
            if( ! isSelfState( world, adjPos, adjState ) || adjState.get( TYPE ) == type ) {
                return Blocks.AIR.getDefaultState();
            }
        }

        return state;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        boolean inRange = pos.getY() < world.getDimension().getHeight() - 1;
        boolean replaceable = world.getBlockState( pos.up() ).isReplaceable( context );

        return inRange && replaceable ? super.getStateForPlacement( context ) : null;
    }

    @Override
    public boolean canRemainOn( IWorldReader world, BlockPos pos, BlockState state, BlockState selfState ) {
        if( selfState.get( TYPE ) == ROOT ) {
            return canBlockSustain( world, pos, state );
        } else {
            return isSelfState( world, pos, state ) && state.get( TYPE ) == ROOT;
        }
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack ) {
        world.setBlockState( pos.offset( growDir ), state.with( TYPE, END ) );
    }

    @Override
    public void harvestBlock( World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack ) {
        player.addStat( Stats.BLOCK_MINED.get( this ) );
        player.addExhaustion( 0.005F );
    }

    @Override
    public void onBlockHarvested( World world, BlockPos pos, BlockState state, PlayerEntity player ) {
        int type = state.get( TYPE );
        BlockPos offPos = pos.offset( growDir, type == ROOT ? 1 : - 1 );
        BlockState offState = world.getBlockState( offPos );
        if( offState.getBlock() == this && offState.get( TYPE ) != type ) {
            world.setBlockState( offPos, Blocks.AIR.getDefaultState(), 35 );
            world.playEvent( player, Events.BREAK_BLOCK, offPos, getStateId( offState ) );

            if( ! world.isRemote && ! player.isCreative() ) {
                spawnDrops( state, world, pos, null, player, player.getHeldItemMainhand() );
                spawnDrops( offState, world, offPos, null, player, player.getHeldItemMainhand() );
            }
        }

        super.onBlockHarvested( world, pos, state, player );
    }

    @Override
    public boolean canGenerateAt( IWorld world, BlockPos pos, BlockState state ) {
        BlockPos upPos = pos.offset( growDir );
        BlockState upState = world.getBlockState( upPos );
        boolean upAir = upState.isAir( world, upPos );
        if( this instanceof IWaterPlant ) {
            upAir = upState.getFluidState().getFluid() == MDFluids.MURKY_WATER;
        }
        return upAir && super.canGenerateAt( world, pos, state );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) ) {
            BlockState lower = computeStateForPos( world, pos, getDefaultState().with( TYPE, ROOT ) );
            BlockState upper = computeStateForPos( world, pos, getDefaultState().with( TYPE, END ) );
            world.setBlockState( pos, lower, 2 );
            world.setBlockState( pos.offset( growDir ), upper, 2 );
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public long getPositionRandom( BlockState state, BlockPos pos ) {
        return MathHelper.getPositionRandom( pos.offset( growDir, state.get( TYPE ) == END ? - 1 : 0 ) );
    }
}
