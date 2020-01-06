/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 06 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate;

import modernity.api.util.BlockPredicates;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.generator.decorate.condition.IsBelowHeight;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.ClusterBushDecoration;
import modernity.common.generator.decorate.decoration.DepositDecoration;
import modernity.common.generator.decorate.decoration.MineableDecoration;
import modernity.common.generator.decorate.decoration.SpringDecoration;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.BelowHeight;
import modernity.common.generator.decorate.position.BetweenHeight;
import modernity.common.generator.decorate.position.InCave;
import modernity.common.generator.decorate.position.Surface;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.Heightmap;

public final class DefaultDecoration {
    private DefaultDecoration() {
    }

    public static void setupDefaultDecoration( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.MURKY_DIRT.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.REGOLITH.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 2 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.DARKROCK.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.LIGHTROCK.getDefaultState(), 30 ), new BelowHeight( 128 ), new Chance( 0.5 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.REDROCK.getDefaultState(), 40 ), new BelowHeight( 128 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.ROCK_TYPES, MDBlocks.LIMESTONE.getDefaultState(), 40 ), new BelowHeight( 128 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.block( MDBlocks.ROCK ), MDBlocks.SALT_ORE.getDefaultState(), 15 ), new BetweenHeight( 4, 128 ), new Fixed( 17 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.block( MDBlocks.ROCK ), MDBlocks.ALUMINIUM_ORE.getDefaultState(), 9 ), new BetweenHeight( 4, 128 ), new Fixed( 11 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( BlockPredicates.block( MDBlocks.ROCK ), MDBlocks.ANTHRACITE_ORE.getDefaultState(), 15 ), new BetweenHeight( 4, 128 ), new Fixed( 20 ) ) );

        biome.addDecorator( new DecorationDecorator( new SpringDecoration( MDFluids.MURKY_WATER, SpringDecoration.STILL | SpringDecoration.FLOWING ), new InCave(), new Fixed( 10 ) ) );

        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDBlocks.MURKY_SAND.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 24 ), new IsBelowHeight( 71 ) ) );
        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockState::isSolid, MDBlocks.MURKY_CLAY.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.25 ), new IsBelowHeight( 71 ) ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 50, 5, MDBlocks.SALT_CRYSTAL ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 1 / 35D ), new IsBelowHeight( 71 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 50, 5, MDBlocks.SALT_CRYSTAL ), new InCave(), new Fixed( 4 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlocks.MURINA ), new InCave(), new Fixed( 7 ) ) );
    }
}
