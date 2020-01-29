/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class AlgaeBlock extends PlantBlock implements IWaterPlant, IColoredBlock {
    public static final IntegerProperty DENSITY = MDBlockStateProperties.DENSITY_1_16;

    public AlgaeBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState().with( DENSITY, 1 ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( DENSITY );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        if( useContext.getItem().getItem() == asItem() ) {
            return state.get( DENSITY ) < 16 && ! useContext.isPlacerSneaking();
        }
        return true;
    }

    @Override
    protected BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        BlockState current = world.getBlockState( pos );
        if( current.getBlock() == this ) {
            state = state.with( DENSITY, Math.min( 16, current.get( DENSITY ) + 1 ) );
        }
        return state;
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        BlockState state = world.getBlockState( pos );
        int density = 0;
        if( state.getFluidState().getFluid() == MDFluids.MURKY_WATER ) {
            density = 1;
        } else if( state.getBlock() == this ) {
            density = state.get( DENSITY ) + 1;
            if( density > 16 ) density = 0;
        }
        if( density == 0 ) return false;
        return world.setBlockState( pos, getDefaultState().with( DENSITY, density ), 2 );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ModernityClient.get().getAlgaeColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return ModernityClient.get().getAlgaeColors().getItemColor();
    }
}
