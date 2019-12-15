/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 25 - 2019
 */

package modernity.api.block.fluid;

import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
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
    default int getColor( IFluidState state, BlockPos pos, IEnviromentBlockReader world ) {
        return 0xffffff;
    }

    default int getSourceSlopeWeight() {
        return 10;
    }
}
