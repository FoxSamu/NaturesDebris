/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 28 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import modernity.common.util.MDEvents;

import java.util.Random;

import static modernity.api.util.EWaterlogType.*;

public class BlockExtinguishableTorch extends BlockTorch {

    private final BlockTorch extinguished;

    public BlockExtinguishableTorch( String id, boolean burning, BlockTorch extinguished, Properties properties, Item.Properties itemProps ) {
        super( id, burning, properties, itemProps );
        this.extinguished = extinguished;
    }

    public BlockExtinguishableTorch( String id, boolean burning, BlockTorch extinguished, Properties properties ) {
        super( id, burning, properties );
        this.extinguished = extinguished;
    }

    @Override
    public void onBlockAdded( IBlockState state, World world, BlockPos pos, IBlockState oldState ) {
        if( state.get( WATERLOGGED ) != NONE ) {
            world.playEvent( MDEvents.TORCH_EXTINGHUISH, pos, state.get( FACING ).ordinal() );
            world.setBlockState( pos, extinguished.getDefaultState().with( WATERLOGGED, state.get( WATERLOGGED ) ).with( FACING, state.get( FACING ) ) );
        }
    }

    public static void doExtinguishEffect( Vec3d pos, IWorld world ) {
        Random rand = world.getRandom();
        for( int i = 0; i < 4; i++ ) {
            double px = pos.x + rand.nextDouble() * .4 - .2;
            double py = pos.y + rand.nextDouble() * .4 - .2;
            double pz = pos.z + rand.nextDouble() * .4 - .2;
            world.addParticle( Particles.SMOKE, px, py, pz, 0, 0, 0 );
        }
        world.playSound( null, new BlockPos( pos ), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, .5F, 2.6F + ( rand.nextFloat() - rand.nextFloat() ) * .8F );
    }
}
