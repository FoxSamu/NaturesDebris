package modernity.common.event.impl;

import modernity.common.block.base.LeavesBlock;
import modernity.common.event.StateBlockEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Event cast when leaves decay. This spawns a bunch of leaf particles.
 */
public class LeavesDecayBlockEvent extends StateBlockEvent {
    @Override
    @OnlyIn( Dist.CLIENT )
    public void playEvent( World world, BlockPos pos, BlockState state ) {
        Block block = state.getBlock();
        if( block instanceof LeavesBlock ) {
            LeavesBlock leaves = (LeavesBlock) block;
            leaves.spawnDecayLeaves( pos, world.rand, world, state );
        }
    }
}
