/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.event.impl;

import modernity.common.block.misc.ExtinguishableTorchBlock;
import modernity.common.block.misc.TorchBlock;
import modernity.common.event.SimpleBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TorchExtinguishEvent extends SimpleBlockEvent {
    @Override
    @OnlyIn( Dist.CLIENT )
    public void playEvent( World world, BlockPos pos, Void data ) {
        ExtinguishableTorchBlock.doExtinguishEffect( pos, world, world.getBlockState( pos ).get( TorchBlock.FACING ) );
        world.playSound( Minecraft.getInstance().player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.7F, 1.5F );
    }
}
