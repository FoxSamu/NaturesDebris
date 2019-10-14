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

import javax.annotation.Nullable;

/**
 * Implementing fluids are rendered using Modernity's fluid render instead of Vanilla's fluid renderer, allowing gas
 * fluids to be rendered...
 *
 * @author RGSW
 */
public interface ICustomRenderFluid {

    /** Still fluid texture */
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getStill();

    /** Flowing fluid texture */
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getFlowing();

    /** Overlay texture, can be null */
    @OnlyIn( Dist.CLIENT )
    @Nullable
    ResourceLocation getOverlay();

    /** Color multiplier of the fluid */
    @OnlyIn( Dist.CLIENT )
    default int getColor( IFluidState state, BlockPos pos, IEnviromentBlockReader world ) {
        return 0xffffff;
    }

    /** Slope weight of source blocks, usually 10 */
    default int getSourceSlopeWeight() {
        return 10;
    }
}
