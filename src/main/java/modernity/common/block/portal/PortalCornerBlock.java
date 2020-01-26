/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 26 - 2020
 * Author: rgsw
 */

package modernity.common.block.portal;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.event.MDBlockEvents;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

public class PortalCornerBlock extends Block {
    public static final EnumProperty<PortalCornerState> STATE = MDBlockStateProperties.PORTAL_CORNER_STATE;

    private static final VoxelShape SLAB_SHAPE = makeCuboidShape( 0, 0, 0, 16, 8, 16 );
    private static final VoxelShape EYE_SHAPE = makeCuboidShape( 5, 8, 5, 11, 10, 11 );

    private static final VoxelShape COMBINED_SHAPE = VoxelShapes.or( SLAB_SHAPE, EYE_SHAPE );


    public PortalCornerBlock( Properties props ) {
        super( props );
        setDefaultState( getDefaultState().with( STATE, PortalCornerState.INACTIVE ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( STATE );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return state.get( STATE ) == PortalCornerState.INACTIVE ? SLAB_SHAPE : COMBINED_SHAPE;
    }

    @Override
    public int getLightValue( BlockState state, IEnviromentBlockReader world, BlockPos pos ) {
        return state.get( STATE ).getLightValue();
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( state.get( STATE ) == PortalCornerState.INACTIVE ) {
            ItemStack held = player.getHeldItem( hand );
            if( held.getItem() == MDItems.EYE_OF_THE_CURSE ) {
                if( ! player.abilities.isCreativeMode ) {
                    held.shrink( 1 );
                }
                world.setBlockState( pos, state.with( STATE, PortalCornerState.EYE ) );
                MDBlockEvents.PLACE_EYE.play( world, pos );
                return true;
            }
        }
        if( state.get( STATE ) == PortalCornerState.EXHAUSTED ) {
            world.setBlockState( pos, state.with( STATE, PortalCornerState.INACTIVE ) );
            MDBlockEvents.BREAK_EYE.play( world, pos );
            if( ! world.isRemote ) {
                world.addEntity( new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                    new ItemStack( MDItems.EYE_OF_THE_CURSE )
                ) );
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

}
