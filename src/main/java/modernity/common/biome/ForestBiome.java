/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.BlockPredicates;
import modernity.common.block.MDBlockProviders;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.generator.decorate.DefaultDecoration;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.count.One;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.FixedHeight;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.surface.HumusSurfaceGenerator;
import modernity.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Forest' or 'modernity:forest' biome.
 */
public class ForestBiome extends ModernityBiome {
    protected ForestBiome() {
        super(
            new Builder()
                .depth( 4 ).variation( 6 ).scale( 3 )
                .surfaceGen( new HumusSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        DefaultDecoration.setupDefaultDecoration( this );

        addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), new FixedHeight( 128 ), new Chance( 0.2 ) ) );

        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 100, 6, MDBlocks.MURK_GRASS ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Fixed( 3 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlocks.NETTLES ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlocks.MINT_PLANT ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new Chance( 0.5 ) ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockProviders.RANDOM_MILLIUM ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new One() ) );
        addDecorator( new DecorationDecorator( new ClusterBushDecoration( 81, 7, MDBlockProviders.RANDOM_MELION ), new Surface( Heightmap.Type.MOTION_BLOCKING ), new One() ) );

        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REEDS ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 3D ) ) );
        addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 5, 4, MDBlocks.REDWOLD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Chance( 1 / 6D ) ) );

        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.TRUE, MDBlocks.ROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.125 ) ) );
        addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.TRUE, MDBlocks.DARKROCK.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.0625 ) ) );

        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.INVER ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Fixed( 5 ) ) );
        addDecorator( new DecorationDecorator( new TreeDecoration( MDTrees.BLACKWOOD ), new Surface( Heightmap.Type.WORLD_SURFACE_WG ), new Fixed( 6 ) ) );
    }
}
