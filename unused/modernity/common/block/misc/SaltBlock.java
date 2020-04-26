/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.common.block.base.TranslucentBlock;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * A translucent block that drops salt particles.
 */
public class SaltBlock extends TranslucentBlock {

    public SaltBlock( Properties properties ) {
        super( properties );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 5 ) == 0 ) {
            if( world.getBlockState( pos.down() ).getMaterial().blocksMovement() ) return;

            double x = rand.nextDouble() + pos.getX();
            double y = - 0.05 + pos.getY();
            double z = rand.nextDouble() + pos.getZ();

            world.addParticle( MDParticleTypes.SALT, x, y, z, 0, 0, 0 );
        }
    }
}
