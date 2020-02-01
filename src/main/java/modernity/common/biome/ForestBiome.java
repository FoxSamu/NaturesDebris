/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.BlockPredicates;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.One;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.FixedHeight;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.ForestSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Forest' or 'modernity:forest' biome.
 */
public class ForestBiome extends ModernityBiome {
    protected ForestBiome() {
        super(
            new Builder()
                .depth( 4 ).variation( 6 ).scale( 3 )
                .surfaceGen( new ForestSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.2 ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlockGenerators.MURK_GRASS_BASIC ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 3 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.NETTLES ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.MINT_PLANT ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new One() ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockGenerators.RANDOM_MELION ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new One() ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.RANDOM_MURK_FERN ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.7 ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.MURK_REED ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 3D ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlockGenerators.REDWOLD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 6D ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 4, 0.6, MDBlockGenerators.MOSS ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 3D ) ) );

        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.TRUE, MDBlocks.ROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.125 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.TRUE, MDBlocks.DARKROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.0625 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDBlocks.MURKY_COARSE_DIRT.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.3 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 3, BlockState::isSolid, MDBlocks.MUD.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.2 ) ) );

        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Fixed( 6 ) ) );
    }
}
