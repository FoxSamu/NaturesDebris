/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import modernity.client.colors.ColorProfile;
import modernity.common.block.dirt.logic.DirtLogic;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class GrassBlock extends SnowyDirtlikeBlock implements IColoredBlock {

    public GrassBlock( DirtLogic logic, Properties properties ) {
        super( logic, properties );
    }

    @OnlyIn( Dist.CLIENT )
    protected ColorProfile getColorMap() {
        return ModernityClient.get().getGrassColors();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColorMap().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColorMap().getItemColor();
    }
}
