/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.fluid.MDFluids;
import modernity.common.fluid.RegularFluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

/**
 * A feature that generates a fluid fall.
 */
public class FluidFallFeature extends Feature<FluidFallFeature.Config> {
    /**
     * Allows the fluid to generate in a still state: it should have no way to flow.
     */
    public static final int STILL = 1;

    /**
     * Allows the fluid to generate in a flowing state: it should have a way to flow.
     */
    public static final int FLOWING = 2;

    public FluidFallFeature() {
        super( dynamic -> new Config( MDFluids.MODERNIZED_WATER, STILL ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos pos, Config config ) {
        if( ! world.getBlockState( pos ).getMaterial().blocksMovement() ) return false;

        Direction noSupport = config.fluid.isGas() ? Direction.DOWN : Direction.UP;

        MovingBlockPos rpos = new MovingBlockPos();
        {
            int supportingSides = 0;
            for( Direction facing : Direction.values() ) {
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

    /**
     * Configuration of the {@link FluidFallFeature}.
     */
    public static class Config implements IFeatureConfig {
        private final int typeFlags;
        private final RegularFluid fluid;

        /**
         * Creates a fluid fall config.
         *
         * @param fluid     The fluid to place.
         * @param typeFlags Which types of generation are allowed? Possible flags are {@link #STILL} and {@link
         *                  #FLOWING}. Flags can be OR-ed ({@code A|B}).
         */
        public Config( RegularFluid fluid, int typeFlags ) {
            this.typeFlags = typeFlags;
            this.fluid = fluid;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
