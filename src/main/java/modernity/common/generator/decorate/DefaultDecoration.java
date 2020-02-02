/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate;

import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.generator.SurfaceGeneration;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.condition.IsBelowHeight;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.BelowHeight;
import modernity.common.generator.decorate.position.BetweenHeight;
import modernity.common.generator.decorate.position.InCave;
import modernity.common.generator.decorate.position.Surface;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.Heightmap;

import static modernity.api.util.BlockPredicates.*;

public final class DefaultDecoration {
    private DefaultDecoration() {
    }


    public static void setupDefaultDecoration( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_DIRT.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_SAND.getDefaultState(), 50 ), new BelowHeight( 128 ), new Chance( 0.4 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_COARSE_DIRT.getDefaultState(), 50 ), new BelowHeight( 128 ), new Chance( 0.4 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.REGOLITH.getDefaultState(), 50 ), new BelowHeight( 128 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.DARKROCK.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.LIGHTROCK.getDefaultState(), 30 ), new BelowHeight( 128 ), new Chance( 0.5 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.REDROCK.getDefaultState(), 40 ), new BelowHeight( 128 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.LIMESTONE.getDefaultState(), 40 ), new BelowHeight( 128 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.SALT_ORE.getDefaultState(), 15 ), new BetweenHeight( 18, 128 ), new Fixed( 17 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.ALUMINIUM_ORE.getDefaultState(), 9 ), new BetweenHeight( 18, 128 ), new Fixed( 11 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.ANTHRACITE_ORE.getDefaultState(), 15 ), new BetweenHeight( 18, 128 ), new Fixed( 20 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.FINNERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.IVERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.SAGERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.DARK_SUMESTONE.getDefaultState(), 40 ), new BetweenHeight( 0, 26 ) ) );

        biome.addDecorator( new DecorationDecorator( new SpringDecoration( MDFluids.MURKY_WATER, SpringDecoration.STILL | SpringDecoration.FLOWING ), new InCave(), new Fixed( 10 ) ) );

        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDBlocks.MURKY_SAND.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 24 ), new IsBelowHeight( SurfaceGeneration.MAIN_HEIGHT - 1 ) ) );
        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDBlocks.MURKY_CLAY.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.25 ), new IsBelowHeight( SurfaceGeneration.MAIN_HEIGHT - 1 ) ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 50, 5, MDBlockGenerators.SALT_CRYSTAL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 35D ), new IsBelowHeight( SurfaceGeneration.MAIN_HEIGHT - 1 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 50, 5, MDBlockGenerators.SALT_CRYSTAL ), new InCave(), new Fixed( 4 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.MURINA ), new InCave(), new Fixed( 10 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 8, MDBlockGenerators.CAVE_GRASS ), new InCave(), new Fixed( 9 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.HANGING_MOSS ), new BetweenHeight( 0, 60 ), new Fixed( 4 ) ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.SEEDLE ), new InCave() ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.BLACK_MUSHROOM ), new InCave() ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.DOTTED_MUSHROOM ), new InCave() ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 30, 8, MDBlockGenerators.PEBBLES ), new InCave(), new Fixed( 10 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 30, 8, MDBlockGenerators.PEBBLES ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 2 ) ) );

        biome.addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.MOSS ), new InCave(), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.DEAD_MOSS ), new InCave(), new Fixed( 1 ) ) );
        biome.addDecorator( new DecorationDecorator( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.LICHEN ), new InCave(), new Fixed( 2 ) ) );

        biome.addDecorator( new DecorationDecorator( new GroupedBushDecoration( 4, 5, 4, MDBlockGenerators.PUDDLE ), new InCave(), new Fixed( 7 ) ) );
    }
}
