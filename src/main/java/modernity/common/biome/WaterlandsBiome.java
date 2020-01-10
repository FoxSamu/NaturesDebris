/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlockProviders;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decoration.TreeDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Waterlands' or 'modernity:waterlands' biome.
 */
public class WaterlandsBiome extends ModernityBiome {
    protected WaterlandsBiome() {
        super(
            new Builder()
                .depth( 0 ).variation( 0 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REEDS ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REDWOLD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlocks.MURK_GRASS ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockProviders.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 1 / 6D ) ) );

        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 2D ) ) );
    }

}
