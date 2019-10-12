/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.util;

import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.client.ModernityClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

import java.util.Random;

@OnlyIn( Dist.CLIENT )
public final class ClientHooks {
    public static void preRenderShaders( float partialTicks ) {

    }

    // TODO: Remove this as forge implemented custom models again...
    public static Boolean onRenderBlock( BlockRendererDispatcher blockRenderer, BlockState state, BlockPos pos, IEnviromentBlockReader world, BufferBuilder buff, Random rand, IModelData modelData ) {
        // Return null to pass render action
        return null;
    }

    public static Boolean onRenderFluid( BlockRendererDispatcher blockRenderer, BlockPos pos, IEnviromentBlockReader reader, BufferBuilder buff, IFluidState state ) {
        // Draw vanilla water and lava with our fluid renderer to enable custom rendering behaviour on our blocks...
        if( state.getFluid() instanceof ICustomRenderFluid || state.getFluid() instanceof WaterFluid || state.getFluid() instanceof LavaFluid ) {
            return ModernityClient.get().getFluidRenderer().render( reader, pos, buff, state );
        }
        // Return null to ignore render action
        return null;
    }
}
