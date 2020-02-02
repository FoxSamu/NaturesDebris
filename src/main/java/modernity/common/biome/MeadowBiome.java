/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decoration.LakeDecoration;
import modernity.common.generator.decorate.decoration.TreeDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.FixedHeight;
import modernity.common.generator.decorate.position.InCave;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Meadow' or 'modernity:meadow' biome.
 */
public class MeadowBiome extends ModernityBiome {
    protected MeadowBiome( Type type ) {
        super( type.builder );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.2 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 3D ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );

        if( type == Type.FLOWER_MEADOW ) {
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 3 ) ) );
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MELION ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 4 ) ) );
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.NUDWART ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 2 ) ) );
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.REDWOLD ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 2 ) ) );
        }

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 12 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.NETTLES ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.25 ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MELION ), new Surface( Heightmap.Type.MOTION_BLOCKING ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 9 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.CAVE_GRASS ), new InCave(), new Fixed( 12 ) ) );

        if( type != Type.FLOWER_NO_TREES ) {
            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 30D ) ) );
        }
    }

    public enum Type {
        MEADOW(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        FLOWER_MEADOW(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        FLOWER_NO_TREES(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
