/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ConnectionTestBlock extends Block implements ITopTextureConnectionBlock, IColoredBlock {
    public ConnectionTestBlock( Properties properties ) {
        super( properties );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ModernityClient.get().getGrassColors().getColor( reader, pos );
    }
}
