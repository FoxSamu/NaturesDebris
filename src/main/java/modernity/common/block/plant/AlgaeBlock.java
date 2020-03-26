/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.block.IColoredBlock;
import modernity.common.block.MDBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

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
            return state.get( DENSITY ) < 16 && ! useContext.func_225518_g_();
        }
        return true;
    }

    @Override
    public BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        BlockState current = world.getBlockState( pos );
        if( current.getBlock() == this ) {
            state = state.with( DENSITY, Math.min( 16, current.get( DENSITY ) + 1 ) );
        }
        return state;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return 0; // TODO
//        return ModernityClient.get().getAlgaeColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return 0;
//        return ModernityClient.get().getAlgaeColors().getItemColor();
    }
}
