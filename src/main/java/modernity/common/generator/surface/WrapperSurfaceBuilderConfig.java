/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.surface;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

/**
 * Configuration for {@link WrapperSurfaceBuilder}.
 */
public class WrapperSurfaceBuilderConfig extends SurfaceBuilderConfig {
    private final ISurfaceGenerator surfaceGen;

    public WrapperSurfaceBuilderConfig( ISurfaceGenerator surfaceGen ) {
        super( Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState() );
        this.surfaceGen = surfaceGen;
    }

    public ISurfaceGenerator getSurfaceGen() {
        return surfaceGen;
    }
}