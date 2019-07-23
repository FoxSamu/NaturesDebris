/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.render.block;

import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.client.util.ProxyClient;

@Deprecated
@OnlyIn( Dist.CLIENT )
public class WrappingFluidRenderer extends BlockFluidRenderer {

    public WrappingFluidRenderer() {
        this.initAtlasSprites();
    }

    protected void initAtlasSprites() {
        ProxyClient.get().getFluidRenderer().initAtlasSprites();
    }

    public boolean render( IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state ) {
        return ProxyClient.get().getFluidRenderer().render( world, pos, buffer, state );
    }
}
