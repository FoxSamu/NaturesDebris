/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 28 - 2019
 */

package modernity.common.util;

import modernity.common.block.base.BlockExtinguishableTorch;
import modernity.common.block.base.BlockLeaves;
import modernity.common.block.base.BlockTorch;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MDEvents {
    public static final int SUMMON_PORTAL = 4000;
    public static final int LEAVES_DECAY = 4001;
    public static final int TORCH_EXTINGHUISH = 4002;


    @OnlyIn( Dist.CLIENT )
    public static void playEvent( EntityPlayer player, World world, int type, BlockPos pos, int data ) {
        switch( type ) {
            case SUMMON_PORTAL: {
                playSummonPortal( world, pos );
            } break;
            case LEAVES_DECAY: {
                playLeavesDecay( world, pos, data );
            } break;
            case TORCH_EXTINGHUISH: {
                playTorchExtinghuish( world, pos, data );
            } break;
        }
    }

    @OnlyIn( Dist.CLIENT )
    private static void playSummonPortal( World world, BlockPos pos ) {
        double dx = pos.getX();
        double dy = pos.getY();
        double dz = pos.getZ();

        world.addParticle( Particles.EXPLOSION, true, dx + 0.5, dy + 0.5, dz + 0.5, 0, 0, 0 );
        world.playSound( dx + 0.5, dy + 0.5, dz + 0.5, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1, 1, true );
    }

    @OnlyIn( Dist.CLIENT )
    private static void playLeavesDecay( World world, BlockPos pos, int data ) {
        IBlockState state = Block.BLOCK_STATE_IDS.getByValue( data );
        if( state == null ) return;
        Block block = state.getBlock();
        if( block instanceof BlockLeaves ) {
            BlockLeaves leaves = (BlockLeaves) block;
            leaves.spawnDecayLeaves( pos, world.rand, world, state );
        }
    }

    @OnlyIn( Dist.CLIENT )
    private static void playTorchExtinghuish( World world, BlockPos pos, int data ) {
        EnumFacing facing = EnumFacing.values()[ data ];
        if( facing == EnumFacing.UP ) return;
        BlockExtinguishableTorch.doExtinguishEffect( BlockTorch.getParticlePos( pos, facing ), world );
    }
}
