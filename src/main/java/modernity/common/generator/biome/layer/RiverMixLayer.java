/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.biome.MDBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

/**
 * Biome layer that mixes the {@linkplain RiverLayer river shapes} into the biome layers.
 */
public enum RiverMixLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    private static final int RIVER = Registry.BIOME.getId( MDBiomes.RIVER );

    @Override
    public int apply( INoiseRandom context, IArea area1, IArea area2, int x, int z ) {
        int biomes = area1.getValue( x, z );
        int rivers = area2.getValue( x, z );
        if( rivers == RIVER ) {
            return RIVER;
        } else {
            return biomes;
        }
    }
}