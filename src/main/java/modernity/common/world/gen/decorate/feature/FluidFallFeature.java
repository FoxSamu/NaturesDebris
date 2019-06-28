/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 28 - 2019
 */

package modernity.common.world.gen.decorate.feature;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import modernity.api.util.BlockUpdates;
import modernity.api.util.EcoBlockPos;
import modernity.common.fluid.RegularFluid;

import java.util.Random;

public class FluidFallFeature extends Feature<FluidFallFeature.Config> {
    public static final int STILL = 1;
    public static final int FLOWING = 2;

    @Override
    public boolean place( IWorld world, IChunkGenerator<? extends IChunkGenSettings> generator, Random rand, BlockPos pos, Config config ) {
        if( ! world.getBlockState( pos ).getMaterial().blocksMovement() ) return false;

        EnumFacing noSupport = config.fluid.isGas() ? EnumFacing.DOWN : EnumFacing.UP;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            int supportingSides = 0;
            for( EnumFacing facing : EnumFacing.values() ) {
                if( facing == noSupport ) continue; // Fluid would never fall this way

                rpos.setPos( pos );
                rpos.offset( facing );

                if( world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                    supportingSides++;
                }
            }

            if( supportingSides == 5 && ( config.typeFlags & STILL ) > 0 ) {
                world.setBlockState( pos, config.fluid.getBlockState( config.fluid.getStillFluid().getDefaultState() ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
                world.getPendingFluidTicks().scheduleTick( pos, config.fluid.getStillFluid(), 0 );
                return true;
            }
            if( supportingSides == 4 && ( config.typeFlags & FLOWING ) > 0 ) {
                world.setBlockState( pos, config.fluid.getBlockState( config.fluid.getStillFluid().getDefaultState() ), BlockUpdates.NOTIFY_CLIENTS );
                world.getPendingFluidTicks().scheduleTick( pos, config.fluid.getStillFluid(), 0 );
                return true;
            }
            return false;
        }
    }

    public static class Config implements IFeatureConfig {
        private final int typeFlags;
        private final RegularFluid fluid;

        public Config( RegularFluid fluid, int typeFlags ) {
            this.typeFlags = typeFlags;
            this.fluid = fluid;
        }
    }
}
