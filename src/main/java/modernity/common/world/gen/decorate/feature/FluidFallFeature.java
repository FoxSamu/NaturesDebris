/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import modernity.api.block.fluid.IGaseousFluid;
import modernity.api.util.BlockUpdates;
import modernity.api.util.EcoBlockPos;
import modernity.common.block.base.BlockFluid;

import java.util.Random;

public class FluidFallFeature extends Feature<FluidFallFeature.Config> {
    public static final int STILL = 1;
    public static final int FLOWING = 2;

    @Override
    public boolean place( IWorld world, IChunkGenerator<? extends IChunkGenSettings> generator, Random rand, BlockPos pos, Config config ) {
        boolean gas = config.fluid.getFluidState( config.fluid.getDefaultState() ).getFluid() instanceof IGaseousFluid;
        EnumFacing noSupport = gas ? EnumFacing.DOWN : EnumFacing.UP;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            int supportingSides = 0;
            for( EnumFacing facing : EnumFacing.values() ) {
                if( facing == noSupport ) continue; // This side does not support such fluid blocks...
                rpos.setPos( pos );
                rpos.offset( facing );

                if( world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                    supportingSides++;
                }
            }

            if( supportingSides == 5 && ( config.typeFlags & STILL ) > 0 ) {
                world.setBlockState( pos, config.fluid.getDefaultState(), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                return true;
            }
            if( supportingSides == 4 && ( config.typeFlags & FLOWING ) > 0 ) {
                world.setBlockState( pos, config.fluid.getDefaultState(), BlockUpdates.CAUSE_UPDATE | BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                return true;
            }
            return false;
        }
    }

    public static class Config implements IFeatureConfig {
        private final int typeFlags;
        private final BlockFluid fluid;

        public Config( BlockFluid fluid, int typeFlags ) {
            this.typeFlags = typeFlags;
            this.fluid = fluid;
        }
    }
}
