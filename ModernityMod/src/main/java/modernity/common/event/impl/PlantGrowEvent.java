/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.event.impl;

import modernity.common.event.SimpleBlockEvent;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class PlantGrowEvent extends SimpleBlockEvent {
    @Override
    public void playEvent( World world, BlockPos pos, Void data ) {
        BlockState state = world.getBlockState( pos );
        VoxelShape shape = state.getShape( world, pos );
        AxisAlignedBB bb = shape.getBoundingBox();

        double area = bb.getXSize() * bb.getYSize() * bb.getZSize();
        int particles = (int) ( area * 13 );

        for( int i = 0; i < particles; i++ ) {
            double x = random( bb.minX, bb.maxX, pos.getX(), world );
            double y = random( bb.minY, bb.maxY, pos.getY(), world );
            double z = random( bb.minZ, bb.maxZ, pos.getZ(), world );

            world.addParticle( ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0 );
        }
    }

    private static double random( double min, double max, int pos, World world ) {
        return world.rand.nextDouble() * ( max - min ) + min + pos;
    }
}
