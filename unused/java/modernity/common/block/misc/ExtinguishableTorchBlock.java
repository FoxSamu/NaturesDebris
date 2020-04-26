/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.common.event.MDBlockEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

import static modernity.common.block.fluid.WaterlogType.*;

/**
 * Describes a torch that can be extinghuished by placing it in water...
 */
@SuppressWarnings( "deprecation" )
public class ExtinguishableTorchBlock extends TorchBlock {

    private final TorchBlock extinguished;

    public ExtinguishableTorchBlock( boolean burning, TorchBlock extinguished, Block.Properties properties ) {
        super( burning, properties );
        this.extinguished = extinguished;
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving ) {
        if( state.get( WATERLOGGED ) != NONE ) {
            world.getPendingBlockTicks().scheduleTick( pos, this, 0 );
        }
    }

    @Override
    public void tick( BlockState state, World world, BlockPos pos, Random random ) {
        MDBlockEvents.TORCH_EXTINGUISH.play( world, pos );
        world.setBlockState( pos, extinguished.getDefaultState().with( WATERLOGGED, state.get( WATERLOGGED ) ).with( FACING, state.get( FACING ) ) );
    }

    public static void doExtinguishEffect( BlockPos pos, IWorld world, Direction dir ) {
        Vec3d part = getParticlePos( pos, dir );

        Random rand = world.getRandom();
        for( int i = 0; i < 4; i++ ) {
            double px = part.x + rand.nextDouble() * .4 - .2;
            double py = part.y + rand.nextDouble() * .4 - .2;
            double pz = part.z + rand.nextDouble() * .4 - .2;
            world.addParticle( ParticleTypes.SMOKE, px, py, pz, 0, 0, 0 );
        }
        world.playSound( null, pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, .5F, 2.6F + ( rand.nextFloat() - rand.nextFloat() ) * .8F );
    }
}
