/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.ModernityClient;
import modernity.client.colors.IColorProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

import javax.annotation.Nullable;

public class ReferencedColorProvider implements IColorProvider {
    private final IColorProvider colorProvider;

    public ReferencedColorProvider( ResourceLocation loc ) {
        this.colorProvider = ModernityClient.get().getColorProfileManager().load( loc ).getProvider();
    }

    @Override
    public int getColor( @Nullable IEnviromentBlockReader world, BlockPos pos ) {
        return colorProvider.getColor( world, pos );
    }

    @Override
    public void initForSeed( long seed ) {
        colorProvider.initForSeed( seed );
    }
}
