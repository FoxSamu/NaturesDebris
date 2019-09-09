/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.layer;

import modernity.common.biome.MDBiomes;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

public enum GenLayerRiverMix implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    private static final int RIVER = IRegistry.BIOME.getId( MDBiomes.RIVER );

    public int apply( IContext context, AreaDimension dimen, IArea area1, IArea area2, int x, int z ) {
        int biomes = area1.getValue( x, z );
        int rivers = area2.getValue( x, z );
        if( rivers == RIVER ) {
            return RIVER;
        } else {
            return biomes;
        }
    }
}