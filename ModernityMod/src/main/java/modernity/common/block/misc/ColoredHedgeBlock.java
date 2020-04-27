/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.misc;

import modernity.client.colors.IColorProfile;
import modernity.generic.block.IColoredBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
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
    private IColorProfile getColors() {
//        switch( type ) { // TODO
//            case 0: return ModernityClient.get().getBlackwoodColors();
//            case 1: return ModernityClient.get().getInverColors();
//            case 2: return ModernityClient.get().getRedInverColors();
//            default: throw new IllegalStateException();
//        }
        return null;
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColors().getColor( reader, pos );
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColors().getItemColor();
    }
}
