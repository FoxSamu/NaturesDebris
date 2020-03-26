/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IEnviromentBlockReader;
import net.redgalaxy.util.MathUtil;

public abstract class RandomNoiseColorProvider extends NoiseColorProvider {
    private final IColorProvider[] colors;

    protected RandomNoiseColorProvider( IColorProvider[] colors ) {
        this.colors = colors;
    }

    @Override
    protected int computeColor( IEnviromentBlockReader world, BlockPos pos, double noise ) {
        int idx = Math.min(
            MathHelper.floor(
                MathUtil.relerp(
                    - 1, 1,
                    0, colors.length,
                    noise
                )
            ),
            colors.length - 1
        );
        return colors[ idx ].getColor( world, pos );
    }

    @Override
    public void initForSeed( long seed ) {
        super.initForSeed( seed );
        for( IColorProvider provider : colors ) {
            provider.initForSeed( seed );
        }
    }
}
