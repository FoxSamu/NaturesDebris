/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 26 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.generic.block.IColoredBlock;
import modernity.client.ModernityClient;
import modernity.client.colors.ColorProfile;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ColoredHedgeBlock extends HedgeBlock implements IColoredBlock {
    private final int type;

    public ColoredHedgeBlock( Properties properties, int type ) {
        super( properties );
        this.type = type;
    }

    @OnlyIn( Dist.CLIENT )
    private ColorProfile getColors() {
        switch( type ) {
            case 0: return ModernityClient.get().getBlackwoodColors();
            case 1: return ModernityClient.get().getInverColors();
            case 2: return ModernityClient.get().getRedInverColors();
            default: throw new IllegalStateException();
        }
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColors().getColor( reader, pos );
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColors().getItemColor();
    }
}
