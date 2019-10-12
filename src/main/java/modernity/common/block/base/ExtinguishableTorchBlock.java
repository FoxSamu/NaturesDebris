/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 4 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

import static modernity.api.util.EWaterlogType.*;

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
        // TODO: Custom event packet
//        world.playEvent( MDEvents.TORCH_EXTINGHUISH, pos, state.get( FACING ).ordinal() );
        world.setBlockState( pos, extinguished.getDefaultState().with( WATERLOGGED, state.get( WATERLOGGED ) ).with( FACING, state.get( FACING ) ) );
    }

    public static void doExtinguishEffect( Vec3d pos, IWorld world ) {
        Random rand = world.getRandom();
        for( int i = 0; i < 4; i++ ) {
            double px = pos.x + rand.nextDouble() * .4 - .2;
            double py = pos.y + rand.nextDouble() * .4 - .2;
            double pz = pos.z + rand.nextDouble() * .4 - .2;
            world.addParticle( ParticleTypes.SMOKE, px, py, pz, 0, 0, 0 );
        }
        world.playSound( null, new BlockPos( pos ), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, .5F, 2.6F + ( rand.nextFloat() - rand.nextFloat() ) * .8F );
    }
}
