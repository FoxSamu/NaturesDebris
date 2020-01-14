/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlockProviders;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.One;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decoration.LakeDecoration;
import modernity.common.generator.decorate.decoration.TreeDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.FixedHeight;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Lush Meadow' or 'modernity:lush_meadow' biome.
 */
public class LushMeadowBiome extends ModernityBiome {
    protected LushMeadowBiome() {
        super(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.2 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REEDS ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REDWOLD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlocks.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 10D ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockProviders.HIGH_GRASS ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 24 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlocks.NETTLES ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.25 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockProviders.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockProviders.RANDOM_MELION ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new One() ) );

        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 30D ) ) );
    }
}
