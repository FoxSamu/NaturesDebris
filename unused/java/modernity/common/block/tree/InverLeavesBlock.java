/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.tree;

import modernity.generic.block.IColoredBlock;
import modernity.client.ModernityClient;
import modernity.client.colors.ColorProfile;
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

    private final boolean red;

    public InverLeavesBlock( Tag<Block> logTag, Properties properties, boolean red ) {
        super( logTag, properties );
        this.red = red;
    }

    private ColorProfile getColorProfile() {
        return red ? ModernityClient.get().getRedInverColors()
                   : ModernityClient.get().getInverColors();
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColorProfile().getColor( reader, pos );
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColorProfile().getItemColor();
    }
}
