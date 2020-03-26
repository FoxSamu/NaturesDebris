/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.surface.SwampSurfaceGenerator;

/**
 * The 'Swamp' or 'modernity:swamp' biome.
 */
public class SwampBiome extends ModernityBiome {
    protected SwampBiome( Type type ) {
        super( type.builder );
        BiomeGroups.registerBiomeToGroup( "swamp", this );
        if( type == Type.SWAMP_RIVER ) {
            BiomeGroups.registerBiomeToGroup( "rivers", this );
        }

        // TODO Decorations
//        setGrowingPlants(
//            WeightedBlockGenerator.builder( MDBlockGenerators.MURK_GRASS_1, 900 )
//                                  .add( MDBlockGenerators.RANDOM_MILLIUM, 40 )
//                                  .add( MDBlockGenerators.MURK_FLOWERS, 30 )
//                                  .add( MDBlockGenerators.FOXGLOVE, 5 )
//                                  .add( MDBlockGenerators.REDWOLD, 20 )
//                                  .add( MDBlockGenerators.SEEDLE, 34 )
//                                  .add( MDBlockGenerators.DOTTED_MUSHROOM, 34 )
//                                  .add( MDBlockGenerators.BLACK_MUSHROOM, 34 )
//                                  .add( MDBlockGenerators.NETTLES, 30 )
//                                  .add( MDBlockGenerators.TURUPT, 4 )
//                                  .add( MDBlockGenerators.EGIUM, 19 )
//                                  .add( MDBlockGenerators.MURK_LAVENDER, 17 )
//                                  .add( MDBlockGenerators.CATTAIL, 28 )
//                                  .add( MDBlockGenerators.HORSETAIL, 13 )
//                                  .add( MDBlockGenerators.MILKY_EYE, 7 )
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
//        double mudChance = type == Type.SWAMP_MARSHES ? 0.9 : 0.3;
//        addDecorator( new DecorationDecorator( new DepositDecoration( 3, BlockState::isSolid, MDNatureBlocks.MUD.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( mudChance ) ) );
//
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.DEAD_GRASS ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ) ) );
//
//        if( type == Type.SWAMP || type == Type.SWAMP_HILLS ) {
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD_TINY ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );
//        }
//
//        if( type == Type.SWAMP_LAND ) {
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new MinMax( 1, 2 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD_TINY ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new MinMax( 1, 2 ) ) );
//        }
//
//        if( type == Type.SWAMP_MARSHES ) {
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.03 ) ) );
//            addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD_TINY ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 0.03 ) ) );
//        }
//
//        addDecorator( new DecorationDecorator( new FossilDecoration(), new FossilPosition(), new Chance( 1 / 64D ) ) );
//
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
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_FLOWERS ), 30 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.FOXGLOVE ), 5 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.REDWOLD ), 20 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.SEEDLE ), 34 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DOTTED_MUSHROOM ), 34 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.BLACK_MUSHROOM ), 34 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.NETTLES ), 30 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.TURUPT ), 4 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.EGIUM ), 19 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_LAVENDER ), 17 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.CATTAIL ), 28 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.HORSETAIL ), 13 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.DEAD_GRASS ), 24 )
//                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MILKY_EYE ), 7 ),
//            new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ),
//            new MinMax( 3, 5 )
//        ) );
//
//        if( type == Type.SWAMP_LAND ) {
//            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.MUXUS_BUSH ), new Surface( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES ), new Chance( 0.3 ) ) );
//
//
//            IBlockGenerator mushrooms = new RandomBlockGenerator(
//                MDBlockGenerators.SEEDLE,
//                MDBlockGenerators.BLACK_MUSHROOM,
//                MDBlockGenerators.DOTTED_MUSHROOM
//            );
//
//            addDecorator( new DecorationDecorator( new DeadLogDecoration( 5, 8, mushrooms, MDTreeBlocks.BLACKWOOD_LOG.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.08 ) ) );
//        }
//
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 8 ) ) );
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 1, 2 ) ) );
//
//        if( type == Type.SWAMP_WATER ) {
//            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.WATER_WIRE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
//        }
//
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 5 ) ) );
//        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.COTTONSEDGE ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 1 / 6D ) ) );
//
//        if( type == Type.SWAMP_MARSHES ) {
//            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 2D ) ) );
//        }
//
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 2D ) ) );
//
//        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 7, 5, 0.9, MDBlockGenerators.ALGAE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 0, 4 ) ) );
    }

    public enum Type {
        SWAMP(
            new Builder()
                .depth( - 1 ).variation( 3 ).scale( 1 )
                .surfaceGen( new SwampSurfaceGenerator( true, 8, 10, 0 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        SWAMP_HILLS(
            new Builder()
                .depth( - 2 ).variation( 5 ).scale( 3 )
                .surfaceGen( new SwampSurfaceGenerator( true, 8, 10, 0 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        SWAMP_MARSHES(
            new Builder()
                .depth( - 1 ).variation( 1 ).scale( 1 )
                .surfaceGen( new SwampSurfaceGenerator( true, 0, 10, 8 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        SWAMP_WATER(
            new Builder()
                .depth( - 3 ).variation( 2 ).scale( 1 )
                .surfaceGen( new SwampSurfaceGenerator( false, 8, 10, 0 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        SWAMP_LAND(
            new Builder()
                .depth( 2 ).variation( 2 ).scale( 1 )
                .surfaceGen( new SwampSurfaceGenerator( true, 8, 10, 0 ) )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        SWAMP_RIVER(
            new Builder()
                .depth( - 6 ).variation( 0 ).scale( 2 )
                .blendWeight( 10 )
                .surfaceGen( new SwampSurfaceGenerator( false, 8, 8, 9 ) )
                .precipitation( IPrecipitationFunction.standard() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
