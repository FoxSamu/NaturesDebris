/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.One;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'River' or 'modernity:river' biome.
 */
public class RiverBiome extends ModernityBiome {
    protected RiverBiome() {
        super(
            new Builder()
                .depth( - 6 ).variation( 0 ).scale( 2 )
                .blendWeight( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new One() ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 1 / 6D ) ) );
    }

}
