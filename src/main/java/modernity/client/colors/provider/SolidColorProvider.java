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
import net.minecraft.world.IEnviromentBlockReader;

public class SolidColorProvider implements IColorProvider {
    private final int color;

    public SolidColorProvider( int color ) {
        this.color = color;
    }

    @Override
    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
        return color;
    }
}
