/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.api.block.fluid;

import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICustomRenderFluid {
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getStill();
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getFlowing();
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getOverlay();
    @OnlyIn( Dist.CLIENT )
    int getColor( IFluidState state, BlockPos pos, IWorldReaderBase world );

    default int getSourceSlopeWeight() {
        return 10;
    }
}
