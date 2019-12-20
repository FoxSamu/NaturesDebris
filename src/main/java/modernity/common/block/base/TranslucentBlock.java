/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IFluidOverlayBlock;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * Describes a translucent block. A translucent block only culls faces of equivalent blocks.
 */
public class TranslucentBlock extends Block implements IFluidOverlayBlock {

    public TranslucentBlock( Block.Properties properties ) {
        super( properties );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentBlockState, Direction side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }

    /**
     * A translucent block that drops salt particles.
     */
    public static class Salt extends TranslucentBlock {

        public Salt( Block.Properties properties ) {
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
}
