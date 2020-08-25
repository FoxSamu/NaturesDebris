/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.farmland;

import modernity.client.colors.IColorProfile;
import modernity.common.blockold.dirt.logic.FarmlandDirtLogic;
import modernity.generic.block.IColoredBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nullable;

public class GrassFarmlandBlock extends FarmlandBlock implements IColoredBlock {
    public GrassFarmlandBlock(FarmlandDirtLogic logic, Properties properties) {
        super(logic, properties);
    }

    @Override
    public int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex) {
        return ((IColorProfile) null).getColor(reader, pos); // TODO Re-evaluate - Null use to make things compile
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return ((IColorProfile) null).getItemColor();
    }
}
