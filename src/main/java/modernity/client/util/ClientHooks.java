/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

import modernity.api.block.fluid.ICustomRenderFluid;

import java.util.Random;

@OnlyIn( Dist.CLIENT )
public class ClientHooks {
    public static void preRenderShaders( float partialTicks ) {

    }

    // MAYBE: Do we need this?
    public static Boolean onRenderBlock( BlockRendererDispatcher blockRenderer, IBlockState state, BlockPos pos, IWorldReader world, BufferBuilder buff, Random rand, IModelData modelData ) {
        // Return null to pass render action
        return null;
    }

    public static Boolean onRenderFluid( BlockRendererDispatcher blockRenderer, BlockPos pos, IWorldReader reader, BufferBuilder buff, IFluidState state ) {
        if( state.getFluid() instanceof ICustomRenderFluid || state.getFluid() instanceof WaterFluid || state.getFluid() instanceof LavaFluid ) {
            return ProxyClient.get().getFluidRenderer().render( reader, pos, buff, state );
        }
        // Return null to pass render action
        return null;
    }
}
