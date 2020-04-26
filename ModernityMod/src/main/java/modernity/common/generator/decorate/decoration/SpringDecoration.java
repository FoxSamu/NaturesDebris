/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.common.fluid.RegularFluid;
import modernity.generic.util.BlockUpdates;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class SpringDecoration implements IDecoration {
    /**
     * Allows the fluid to generate in a still state: it should have no way to flow.
     */
    public static final int STILL = 1;

    /**
     * Allows the fluid to generate in a flowing state: it should have a way to flow.
     */
    public static final int FLOWING = 2;


    private final RegularFluid fluid;
    private final int typeFlags;

    public SpringDecoration( RegularFluid fluid, int typeFlags ) {
        this.fluid = fluid;
        this.typeFlags = typeFlags;
    }

    public RegularFluid getFluid() {
        return fluid;
    }

    public int getTypeFlags() {
        return typeFlags;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        if( ! world.getBlockState( pos ).getMaterial().blocksMovement() ) return;

        Direction noSupport = fluid.isGas() ? Direction.DOWN : Direction.UP;

        MovingBlockPos rpos = new MovingBlockPos();

        int supportingSides = 0;
        for( Direction facing : Direction.values() ) {
            if( facing == noSupport ) continue; // Fluid would never fall this way

            rpos.setPos( pos );
            rpos.offset( facing );

            if( world.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                supportingSides++;
            }
        }

        if( supportingSides == 5 && ( typeFlags & STILL ) > 0 ) {
            world.setBlockState( pos, fluid.getBlockState( fluid.getStillFluid().getDefaultState() ), BlockUpdates.NOTIFY_CLIENTS | BlockUpdates.NO_NEIGHBOR_REACTIONS );
            world.getPendingFluidTicks().scheduleTick( pos, fluid.getStillFluid(), 0 );
        } else if( supportingSides == 4 && ( typeFlags & FLOWING ) > 0 ) {
            world.setBlockState( pos, fluid.getBlockState( fluid.getStillFluid().getDefaultState() ), BlockUpdates.NOTIFY_CLIENTS );
            world.getPendingFluidTicks().scheduleTick( pos, fluid.getStillFluid(), 0 );
        }
    }
}
