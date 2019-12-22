/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.surface;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

/**
 * Configuration for {@link WrapperSurfaceBuilder}.
 */
public class WrapperSurfaceBuilderConfig extends SurfaceBuilderConfig {
    private final ISurfaceGenerator<?> surfaceGen;
    private final GenerationSettings chunkgenSettings;

    public WrapperSurfaceBuilderConfig( ISurfaceGenerator<?> surfaceGen, GenerationSettings settings ) {
        super( Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState() );
        this.surfaceGen = surfaceGen;
        this.chunkgenSettings = settings;
    }

    public ISurfaceGenerator<?> getSurfaceGen() {
        return surfaceGen;
    }

    public GenerationSettings getChunkgenSettings() {
        return chunkgenSettings;
    }
}