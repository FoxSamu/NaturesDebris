/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.generic.block.IColoredBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MossBlock extends FacingPlantBlock implements IColoredBlock {
    public MossBlock( Properties properties ) {
        super( properties, 2, 0 );
    }


    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return 0;
//        return ModernityClient.get().getMossColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return 0;
//        return ModernityClient.get().getMossColors().getItemColor();
    }
}
