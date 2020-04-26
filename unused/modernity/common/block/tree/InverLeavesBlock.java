/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.tree;

import modernity.generic.block.IColoredBlock;
import modernity.client.colors.IColorProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

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

    private IColorProfile getColorProfile() {
//        return red ? ModernityClient.get().getRedInverColors()
//                   : ModernityClient.get().getInverColors();
        return null;
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColorProfile().getColor( reader, pos );
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColorProfile().getItemColor();
    }
}
