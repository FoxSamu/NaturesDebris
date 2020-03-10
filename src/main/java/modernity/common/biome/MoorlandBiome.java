/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 10 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.blocks.WeightedBlockGenerator;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.ChanceMinMax;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.MinMax;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.FixedHeight;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.MoorlandSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Meadow' or 'modernity:meadow' biome.
 */
public class MoorlandBiome extends ModernityBiome {
    protected MoorlandBiome( Type type ) {
        super( type.builder );
        BiomeGroups.registerBiomeToGroup( "moorland", this );

        setGrowingPlants(
            WeightedBlockGenerator.builder( MDBlockGenerators.MURK_GRASS_1, 900 )
                                  .add( MDBlockGenerators.RANDOM_LIVING_HEATH, 100 )
                                  .add( MDBlockGenerators.CREEP_OF_THE_MOOR, 90 )
                                  .add( MDBlockGenerators.RANDOM_MILLIUM, 70 )
                                  .add( MDBlockGenerators.RANDOM_WIREPLANT, 10 )
                                  .add( MDBlockGenerators.HORSETAIL, 40 )
                                  .add( MDBlockGenerators.BLACK_MUSHROOM, 20 )
                                  .add( MDBlockGenerators.DOTTED_MUSHROOM, 20 )
                                  .add( MDBlockGenerators.SEEDLE, 20 )
                                  .add( MDBlockGenerators.TURUPT, 10 )
                                  .add( MDBlockGenerators.MINT_PLANT, 5 )
                                  .build()
        );

        MurkSurfaceGeneration.addCaveDeposits( this );
        MurkSurfaceGeneration.addCaveOres( this );
        MurkSurfaceGeneration.addCavePlants( this );
        MurkSurfaceGeneration.addCaveSprings( this );
        MurkSurfaceGeneration.addClaySand( this );
        MurkSurfaceGeneration.addPebbles( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.HEATH_BLOCK ), new FixedHeight( 128 ), new Chance( 0.1 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1, 3 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1, 3 ) ) );

        addDecorator( new DecorationDecorator( new SingleBushDecoration( MDBlockGenerators.SOUL_LIGHT ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.1 ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 7, MDBlockGenerators.CREEP_OF_THE_MOOR ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new ChanceMinMax( 0.7, 2, 4 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 7, MDBlockGenerators.DEAD_HEATH ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new ChanceMinMax( 0.6, 1, 2 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 7, MDBlockGenerators.RANDOM_LIVING_HEATH ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 23 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new MinMax( 2, 7 ) ) );

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
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RANDOM_WIREPLANT ), 10 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DEAD_GRASS ), 30 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.HORSETAIL ), 40 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLACK_MUSHROOM ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DOTTED_MUSHROOM ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.SEEDLE ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.TURUPT ), 10 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MINT_PLANT ), 5 ),
            new Surface( Heightmap.Type.OCEAN_FLOOR_WG )
        ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 9 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );

        if( type != Type.MOORLAND_NO_TREES ) {
            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1, 30 ) ) );
        }
    }

    public enum Type {
        MOORLAND(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new MoorlandSurfaceGenerator( 11, 8, 7 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        HIGH_MOORLAND(
            new Builder()
                .depth( 4 ).variation( 4 ).scale( 3 )
                .surfaceGen( new MoorlandSurfaceGenerator( 11, 8, 7 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        MOORLAND_NO_TREES(
            new Builder()
                .depth( 2 ).variation( 3 ).scale( 2 )
                .surfaceGen( new MoorlandSurfaceGenerator( 12, 7, 8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        MOORLAND_RIVER(
            new Builder()
                .depth( - 6 ).variation( 0 ).scale( 2 )
                .blendWeight( 10 )
                .surfaceGen( new MoorlandSurfaceGenerator( 7, 12, 8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
