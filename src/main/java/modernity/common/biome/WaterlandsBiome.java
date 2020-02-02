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
import modernity.common.generator.decorate.decoration.DepositDecoration;
import modernity.common.generator.decorate.decoration.GroupedBushDecoration;
import modernity.common.generator.decorate.decoration.TreeDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.GrassSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Waterlands' or 'modernity:waterlands' biome.
 */
public class WaterlandsBiome extends ModernityBiome {
    protected WaterlandsBiome( Type type ) {
        super( type.builder );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.REDWOLD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 1 / 6D ) ) );

        addDecorator( new DecorationDecorator( new DepositDecoration( 3, BlockState::isSolid, MDBlocks.MUD.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );

        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 2D ) ) );
    }

    public enum Type {
        WATERLANDS(
            new Builder()
                .depth( 0 ).variation( 0 ).scale( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        );

        private final Builder builder;

        Type( Builder builder ) {
            this.builder = builder;
        }
    }
}
