/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.tree;

import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

import javax.annotation.Nullable;

/**
 * Inver-color leaves
 */
public class InverLeavesBlock extends DecayLeavesBlock implements IColoredBlock {

    public InverLeavesBlock( Tag<Block> logTag, Properties properties ) {
        super( logTag, properties );
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ModernityClient.get().getInverColors().getColor( reader, pos );
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return ModernityClient.get().getInverColors().getItemColor();
    }
}
