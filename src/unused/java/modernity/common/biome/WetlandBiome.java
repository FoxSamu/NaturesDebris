/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.block.MDBlockPredicates;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDNatureBlocks;
import modernity.common.block.MDTreeBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.blocks.IBlockGenerator;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.blocks.RandomBlockGenerator;
import modernity.common.generator.blocks.WeightedBlockGenerator;
import modernity.common.generator.decorate.condition.IsBelowHeight;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.MinMax;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.SwampSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Waterlands' or 'modernity:waterlands' biome.
 */
public class WetlandBiome extends ModernityBiome {
    protected WetlandBiome( Type type ) {
        super( type.builder );
        BiomeGroups.registerBiomeToGroup( "wetland", this );
        if( type == Type.WETLAND_RIVER ) {
            BiomeGroups.registerBiomeToGroup( "rivers", this );
        }


        setGrowingPlants(
            WeightedBlockGenerator.builder( MDBlockGenerators.MURK_GRASS_1, 900 )
                                  .add( MDBlockGenerators.RANDOM_MILLIUM, 40 )
                                  .add( MDBlockGenerators.RANDOM_BULBFLOWER, 10 )
                                  .add( MDBlockGenerators.MURK_FLOWERS, 10 )
                                  .add( MDBlockGenerators.FOXGLOVE, 5 )
                                  .add( MDBlockGenerators.SEEDLE, 34 )
                                  .add( MDBlockGenerators.DOTTED_MUSHROOM, 34 )
                                  .add( MDBlockGenerators.BLACK_MUSHROOM, 34 )
                                  .add( MDBlockGenerators.EGIUM, 19 )
                                  .add( MDBlockGenerators.HORSETAIL, 13 )
                                  .add( MDBlockGenerators.SEEPWEED, 15 )
                                  .add( MDBlockGenerators.COTTONSEDGE, 25 )
                                  .add( MDBlockGenerators.MILKY_EYE, 7 )
                                  .build()
        );

        MurkSurfaceGeneration.addCaveDeposits( this );
        MurkSurfaceGeneration.addCaveOres( this );
        MurkSurfaceGeneration.addCavePlants( this );
        MurkSurfaceGeneration.addCaveSprings( this );
        MurkSurfaceGeneration.addPebbles( this );

        addDecorator( new DecorationDecorator( new DepositDecoration( 4, MDBlockPredicates.tag( MDBlockTags.SOIL ), MDNatureBlocks.MURKY_CLAY.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 0, 2 ), new IsBelowHeight( MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 4, MDBlockPredicates.tag( MDBlockTags.SOIL ), MDNatureBlocks.MUD.getDefaultState() ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new MinMax( 0, 2 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 4, MDBlockPredicates.tag( MDBlockTags.DIRT ), MDNatureBlocks.SALTY_DIRT.getDefaultState() ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new MinMax( 0, 2 ) ) );

        if( type == Type.WETLAND_FOREST ) {
            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 1, 3 ) ) );

            IBlockGenerator mushrooms = new RandomBlockGenerator(
                MDBlockGenerators.SEEDLE,
                MDBlockGenerators.BLACK_MUSHROOM,
                MDBlockGenerators.DOTTED_MUSHROOM
            );

            addDecorator( new DecorationDecorator( new DeadLogDecoration( 5, 8, mushrooms, MDTreeBlocks.BLACKWOOD_LOG.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.08 ) ) );
        } else if( type == Type.WETLAND ) {
            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 2D ) ) );
        }

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
                    40
                )
                .add(
                    new SelectiveDecoration()
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_BULBFLOWER ), 10 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_BULBFLOWER ), 8 )
                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_BULBFLOWER ), 7 ),
                    10
                )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_FLOWERS ), 10 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.FOXGLOVE ), 5 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.REDWOLD ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.SEEDLE ), 34 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DOTTED_MUSHROOM ), 34 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLACK_MUSHROOM ), 34 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.EGIUM ), 19 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.HORSETAIL ), 13 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DEAD_GRASS ), 24 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.SEEPWEED ), 15 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.COTTONSEDGE ), 25 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MILKY_EYE ), 7 )
                .add( new GroupedBushDecoration( 4, 4, 0.7, MDBlockGenerators.RANDOM_WIREPLANT ), 7 ),
            new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ),
            new MinMax( 2, 4 )
        ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 8 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 1, 2 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 10 ) ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 7, 5, 0.9, MDBlockGenerators.ALGAE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 0, 4 ) ) );

        if( type == Type.WETLAND_MARSH || type == Type.WETLAND_RIVER ) {
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.CATTAIL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 7, 20 ) ) );
        } else {
            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.MUXUS_BUSH ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Chance( 0.3 ) ) );
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.CATTAIL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 2, 5 ) ) );
        }
    }

    public enum Type {
        WETLAND(
            new Builder()
                .depth( - 1.3 ).variation( 0.1 ).scale( 0.7 )
                .surfaceGen( new SwampSurfaceGenerator( false, 6, 10, 7 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        WETLAND_FOREST(
            new Builder()
                .depth( - 1.3 ).variation( 0.1 ).scale( 0.7 )
                .surfaceGen( new SwampSurfaceGenerator( false, 7, 10, 2 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        WETLAND_MARSH(
            new Builder()
                .depth( - 1.2 ).variation( 0.1 ).scale( 0.5 )
                .surfaceGen( new SwampSurfaceGenerator( true, 2, 10, 8 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        WETLAND_RIVER(
            new Builder()
                .depth( - 6 ).variation( 0 ).scale( 2 )
                .blendWeight( 10 )
                .surfaceGen( new SwampSurfaceGenerator( false, 2, 10, 8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
