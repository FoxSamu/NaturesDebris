/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.biome;

import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.MinMax;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decoration.SelectiveDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Swamp' or 'modernity:swamp' biome.
 */
public class LakeBiome extends ModernityBiome {
    protected LakeBiome( Type type ) {
        super( type.builder );
        BiomeGroups.registerBiomeToGroup( "lake", this );

        MurkSurfaceGeneration.addCaveDeposits( this );
        MurkSurfaceGeneration.addCaveOres( this );
        MurkSurfaceGeneration.addCavePlants( this );
        MurkSurfaceGeneration.addCaveSprings( this );
        MurkSurfaceGeneration.addClaySand( this );
        MurkSurfaceGeneration.addPebbles( this );

        addDecorator( new DecorationDecorator(
            new SelectiveDecoration()
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.MURK_FLOWERS ), 30 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.REDWOLD ), 20 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.CATTAIL ), 68 )
                .add( new ClusterBushDecoration( 81, 5, MDBlockGenerators.FLY_FLOWER ), 30 ),
            new Surface( Heightmap.Type.OCEAN_FLOOR_WG ),
            new Fixed( 2 )
        ) );

        boolean grass = type == Type.GRASS_LAKE || type == Type.DEEP_GRASS_LAKE;

        if( grass ) {
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_LARGE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 30 ) ) );
        } else {
            addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.WATERGRASS_SMALL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 10 ) ) );
        }


        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 125, 7, MDBlockGenerators.LAKEWEED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 2, 3 ) ) );
        if( ! grass ) {
            addDecorator( new DecorationDecorator( new GroupedBushDecoration( 2, 3, 1, MDBlockGenerators.WATER_WIRE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
        }

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 7, 5, 0.9, MDBlockGenerators.ALGAE ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new MinMax( 0, 4 ) ) );
    }

    public enum Type {
        LAKE(
            new Builder()
                .depth( - 14 ).variation( 4 ).scale( 3 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        DEEP_LAKE(
            new Builder()
                .depth( - 22 ).variation( 6 ).scale( 4 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        UNDEEP_LAKE(
            new Builder()
                .depth( - 5 ).variation( 2 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        GRASS_LAKE(
            new Builder()
                .depth( - 14 ).variation( 4 ).scale( 3 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        DEEP_GRASS_LAKE(
            new Builder()
                .depth( - 22 ).variation( 6 ).scale( 4 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        ),
        LAKE_SHORE(
            new Builder()
                .depth( - 1 ).variation( 2 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
