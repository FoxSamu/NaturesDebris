/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.*;
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
public class FlowerMeadowBiome extends ModernityBiome {
    protected FlowerMeadowBiome( Type type ) {
        super( type.builder );

        MurkSurfaceGeneration.addCaveDeposits( this );
        MurkSurfaceGeneration.addCaveOres( this );
        MurkSurfaceGeneration.addCavePlants( this );
        MurkSurfaceGeneration.addCaveSprings( this );
        MurkSurfaceGeneration.addClaySand( this );
        MurkSurfaceGeneration.addPebbles( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.1 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1, 3 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1, 3 ) ) );

        addDecorator( new DecorationDecorator( new SingleBushDecoration( MDBlockGenerators.SOUL_LIGHT ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.1 ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 12 ) ) );

        addDecorator( new DecorationDecorator(
            new SelectiveDecoration()
                .add(
                    new SelectiveDecoration()
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MAGENTA_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.GREEN_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.CYAN_MILLIUM ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.WHITE_MILLIUM ), 1 ),
                    70
                )
                .add(
                    new SelectiveDecoration()
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MAGENTA_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.ORANGE_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.INDIGO_MELION ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.WHITE_MELION ), 1 ),
                    50
                )
                .add(
                    new SelectiveDecoration()
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_BULBFLOWER ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_BULBFLOWER ), 1 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_BULBFLOWER ), 1 ),
                    50
                )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_FLOWERS ), 30 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MILKY_EYE ), 5 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.GLOBE_THISTLE ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_LAVENDER ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.REDWOLD ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_GRASS ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.NETTLES ), 2 ),
            new Surface( Heightmap.Type.MOTION_BLOCKING ),
            new Fixed( 10 )
        ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 9 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.CAVE_GRASS ), new InCave(), new Fixed( 12 ) ) );

        addDecorator( new DecorationDecorator( new SingleBushDecoration( MDBlockGenerators.SHADE_BLUE ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.03 ) ) );

        if( type != Type.FLOWER_MEADOW_NO_TREES ) {
            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1, 30 ) ) );
        }
    }

    public enum Type {
        FLOWER_MEADOW(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        HIGH_FLOWER_MEADOW(
            new Builder()
                .depth( 4 ).variation( 4 ).scale( 3 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        FLOWER_MEADOW_NO_TREES(
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
