/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.surface.ForestSurfaceGenerator;

/**
 * The 'Forest' or 'modernity:forest' biome.
 */
public class TallForestBiome extends ModernityBiome {
    protected TallForestBiome( Type type ) {
        super( type.builder );
        BiomeGroups.registerBiomeToGroup( "forest_like", this );
        BiomeGroups.registerBiomeToGroup( "tall_forest", this );
        if( type == Type.TALL_FOREST_RIVER ) {
            BiomeGroups.registerBiomeToGroup( "rivers", this );
        }

        // TODO Decorations
//        setGrowingPlants(
//            WeightedBlockGenerator.builder( MDBlockGenerators.MURK_GRASS_1, 900 )
//                                  .add( MDBlockGenerators.RANDOM_MILLIUM, 40 )
//                                  .add( MDBlockGenerators.RANDOM_MELION, 30 )
//                                  .add( MDBlockGenerators.MURK_FLOWERS, 30 )
//                                  .add( MDBlockGenerators.MINT_PLANT, 5 )
//                                  .add( MDBlockGenerators.FOXGLOVE, 5 )
//                                  .add( MDBlockGenerators.REDWOLD, 20 )
//                                  .add( MDBlockGenerators.SEEDLE, 10 )
//                                  .add( MDBlockGenerators.DOTTED_MUSHROOM, 10 )
//                                  .add( MDBlockGenerators.BLACK_MUSHROOM, 10 )
//                                  .add( MDBlockGenerators.NETTLES, 40 )
//                                  .add( MDBlockGenerators.TURUPT, 14 )
//                                  .add( MDBlockGenerators.HORSETAIL, 8 )
//                                  .build()
//        );
//
//        MurkSurfaceGeneration.addCaveDeposits( this );
//        MurkSurfaceGeneration.addCaveOres( this );
//        MurkSurfaceGeneration.addCavePlants( this );
//        MurkSurfaceGeneration.addCaveSprings( this );
//        MurkSurfaceGeneration.addClaySand( this );
//        MurkSurfaceGeneration.addPebbles( this );
//
//        addDecorator( new DecorationDecorator( new LakeDecoration( MDNatureBlocks.MURKY_WATER, null, null, MDNatureBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.1 ) ) );
//
//        addDecorator( new DecorationDecorator( new DepositDecoration( 2, MDBlockPredicates.TRUE, MDNatureBlocks.ROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.025 ) ) );
//        addDecorator( new DecorationDecorator( new DepositDecoration( 2, MDBlockPredicates.TRUE, MDNatureBlocks.DARKROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.0125 ) ) );
//        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDNatureBlocks.MURKY_COARSE_DIRT.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
//        addDecorator( new DecorationDecorator( new DepositDecoration( 3, BlockState::isSolid, MDNatureBlocks.MUD.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.2 ) ) );
//
//        if( type == Type.OPEN_TALL_FOREST || type == Type.HIGH_OPEN_TALL_FOREST ) {
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.RED_INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.02 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.2 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.07 ) ) );
//        } else {
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.RED_INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.2 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.8 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Fixed( 1 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD_TALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 6 ) ) );
//        }
//
//        IBlockGenerator mushrooms = new RandomBlockGenerator(
//            MDBlockGenerators.SEEDLE,
//            MDBlockGenerators.BLACK_MUSHROOM,
//            MDBlockGenerators.DOTTED_MUSHROOM
//        );
//
//        addDecorator( new DecorationDecorator( new DeadLogDecoration( 5, 8, mushrooms, MDTreeBlocks.BLACKWOOD_LOG.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
//        addDecorator( new DecorationDecorator( new DeadLogDecoration( 5, 8, mushrooms, MDTreeBlocks.INVER_LOG.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
//
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Fixed( 6 ) ) );
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.PEBBLES ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Fixed( 6 ) ) );
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.DEAD_GRASS ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ) ) );
//
//        addDecorator( new DecorationDecorator(
//            new SelectiveDecoration()
//                .add(
//                    new SelectiveDecoration()
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MAGENTA_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.GREEN_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.CYAN_MILLIUM ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.WHITE_MILLIUM ), 1 ),
//                    40
//                )
//                .add(
//                    new SelectiveDecoration()
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.RED_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MAGENTA_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLUE_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.YELLOW_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.ORANGE_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.INDIGO_MELION ), 1 )
//                        .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.WHITE_MELION ), 1 ),
//                    30
//                )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_FLOWERS ), 30 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MINT_PLANT ), 20 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.FOXGLOVE ), 5 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.REDWOLD ), 20 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.SEEDLE ), 10 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DOTTED_MUSHROOM ), 10 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLACK_MUSHROOM ), 10 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.NETTLES ), 40 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.TURUPT ), 14 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.HORSETAIL ), 8 ),
//            new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ),
//            new MinMax( 2, 3 )
//        ) );
//
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 5 ) ) );
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );
//
//        if( type == Type.TALL_BUSH_FOREST ) {
//            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 4, 5, 1, MDBlockGenerators.MUXUS_BUSH ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Fixed( 5 ) ) );
//        } else {
//            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.MUXUS_BUSH ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Chance( 0.5 ) ) );
//        }
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.WATER_WIRE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
//
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.RANDOM_WIREPLANT ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Chance( 0.5 ) ) );
//
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.RANDOM_MURK_FERN ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Chance( 0.7 ) ) );
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 3D ) ) );
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );
    }

    public enum Type {
        TALL_FOREST(
            new Builder()
                .depth( 4 ).variation( 6 ).scale( 3 )
                .surfaceGen( new ForestSurfaceGenerator( 11, 10, 10 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        OPEN_TALL_FOREST(
            new Builder()
                .depth( 4 ).variation( 6 ).scale( 3 )
                .surfaceGen( new ForestSurfaceGenerator( 4, 12, 7.8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        TALL_BUSH_FOREST(
            new Builder()
                .depth( 4 ).variation( 6 ).scale( 3 )
                .surfaceGen( new ForestSurfaceGenerator( 11, 10, 10 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        HIGH_TALL_FOREST(
            new Builder()
                .depth( 9 ).variation( 6 ).scale( 4 )
                .surfaceGen( new ForestSurfaceGenerator( 11, 10, 10 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        HIGH_OPEN_TALL_FOREST(
            new Builder()
                .depth( 9 ).variation( 6 ).scale( 4 )
                .surfaceGen( new ForestSurfaceGenerator( 4, 12, 7.8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        ),
        TALL_FOREST_RIVER(
            new Builder()
                .depth( - 8 ).variation( 0 ).scale( 2 )
                .blendWeight( 10 )
                .surfaceGen( new ForestSurfaceGenerator( 4, 12, 7.8 ) )
                .precipitation( IPrecipitationFunction.standard() )
        );
        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
