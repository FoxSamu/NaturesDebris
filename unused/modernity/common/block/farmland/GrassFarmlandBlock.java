/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.generic.block.IColoredBlock;
import modernity.client.colors.IColorProfile;
import modernity.common.block.dirt.logic.FarmlandDirtLogic;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nullable;

public class GrassFarmlandBlock extends FarmlandBlock implements IColoredBlock {
    public GrassFarmlandBlock( FarmlandDirtLogic logic, Properties properties ) {
        super( logic, properties );
    }

    @Override
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ( (IColorProfile) null ).getColor( reader, pos ); // TODO Re-evaluate - Null use to make things compile
    }

    @Override
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return ( (IColorProfile) null ).getItemColor();
    }
}
