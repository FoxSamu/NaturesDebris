/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.blocks;

import modernity.common.biome.MDBiomes;
import modernity.common.blockold.MDPlantBlocks;
import modernity.common.blockold.plant.DoubleDirectionalPlantBlock;
import modernity.common.blockold.plant.FacingPlantBlock;
import modernity.common.blockold.plant.SingleDirectionalPlantBlock;
import modernity.common.blockold.plant.TallDirectionalPlantBlock;
import modernity.common.generator.blocks.condition.IBlockGenCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

import java.util.Random;
import java.util.function.Function;

public final class MDBlockGenerators {
    public static final IBlockGenerator BLUE_MILLIUM = forSinglePlant(MDPlantBlocks.BLUE_MILLIUM);
    public static final IBlockGenerator CYAN_MILLIUM = forSinglePlant(MDPlantBlocks.CYAN_MILLIUM);
    public static final IBlockGenerator GREEN_MILLIUM = forSinglePlant(MDPlantBlocks.GREEN_MILLIUM);
    public static final IBlockGenerator YELLOW_MILLIUM = forSinglePlant(MDPlantBlocks.YELLOW_MILLIUM);
    public static final IBlockGenerator MAGENTA_MILLIUM = forSinglePlant(MDPlantBlocks.MAGENTA_MILLIUM);
    public static final IBlockGenerator RED_MILLIUM = forSinglePlant(MDPlantBlocks.RED_MILLIUM);
    public static final IBlockGenerator WHITE_MILLIUM = forSinglePlant(MDPlantBlocks.WHITE_MILLIUM);

    public static final IBlockGenerator RANDOM_MILLIUM = random(
        BLUE_MILLIUM,
        CYAN_MILLIUM,
        GREEN_MILLIUM,
        YELLOW_MILLIUM,
        MAGENTA_MILLIUM,
        RED_MILLIUM,
        WHITE_MILLIUM
    );

    public static final IBlockGenerator BLUE_MELION = forSinglePlant(MDPlantBlocks.BLUE_MELION);
    public static final IBlockGenerator ORANGE_MELION = forSinglePlant(MDPlantBlocks.ORANGE_MELION);
    public static final IBlockGenerator INDIGO_MELION = forSinglePlant(MDPlantBlocks.INDIGO_MELION);
    public static final IBlockGenerator YELLOW_MELION = forSinglePlant(MDPlantBlocks.YELLOW_MELION);
    public static final IBlockGenerator MAGENTA_MELION = forSinglePlant(MDPlantBlocks.MAGENTA_MELION);
    public static final IBlockGenerator RED_MELION = forSinglePlant(MDPlantBlocks.RED_MELION);
    public static final IBlockGenerator WHITE_MELION = forSinglePlant(MDPlantBlocks.WHITE_MELION);

    public static final IBlockGenerator RANDOM_MELION = random(
        BLUE_MELION,
        ORANGE_MELION,
        INDIGO_MELION,
        YELLOW_MELION,
        MAGENTA_MELION,
        RED_MELION,
        WHITE_MELION
    );

    public static final IBlockGenerator SMALL_MURK_FERN = forSinglePlant(MDPlantBlocks.MURK_FERN);
    public static final IBlockGenerator LARGE_MURK_FERN = forDoublePlant(MDPlantBlocks.TALL_MURK_FERN);

    public static final IBlockGenerator RANDOM_MURK_FERN = weighted(SMALL_MURK_FERN, 6)
                                                               .add(LARGE_MURK_FERN.orElse(SMALL_MURK_FERN), 1)
                                                               .build();

    public static final IBlockGenerator MURK_GRASS_1 = forTallPlant(MDPlantBlocks.MURK_GRASS, rand -> 1);
    public static final IBlockGenerator MURK_GRASS_2 = forTallPlant(MDPlantBlocks.MURK_GRASS, rand -> 2);
    public static final IBlockGenerator MURK_GRASS_3 = forTallPlant(MDPlantBlocks.MURK_GRASS, rand -> 3);
    public static final IBlockGenerator MURK_GRASS_4 = forTallPlant(MDPlantBlocks.MURK_GRASS, rand -> 4);

    public static final IBlockGenerator MURK_GRASS_BASIC = forTallPlant(MDPlantBlocks.MURK_GRASS, rng -> rng.nextInt(10) == 0
                                                                                                         ? 2
                                                                                                         : 1);
    public static final IBlockGenerator MURK_GRASS_LUSH = forTallPlant(MDPlantBlocks.MURK_GRASS, rng -> Math.min(4, rng.nextInt(4) + rng.nextInt(2) + 1));

    public static final IBlockGenerator LUSH_GRASSLAND_GRASS = alternatives(
        conditional(MURK_GRASS_LUSH, (world, pos, rand) -> {
            Biome biome = world.getBiome(pos);
            return biome == MDBiomes.LUSH_GRASSLAND || biome == MDBiomes.HIGH_LUSH_GRASSLAND;
        }),
        conditional(MURK_GRASS_BASIC, (world, pos, rand) -> rand.nextBoolean())
    );

    public static final IBlockGenerator NETTLES = forSinglePlant(MDPlantBlocks.NETTLES);
    public static final IBlockGenerator MINT_PLANT = forSinglePlant(MDPlantBlocks.MINT_PLANT);
    public static final IBlockGenerator REDWOLD = forSinglePlant(MDPlantBlocks.REDWOLD);
    public static final IBlockGenerator SALT_CRYSTAL = forSinglePlant(MDPlantBlocks.SALT_CRYSTAL);
    public static final IBlockGenerator HORSETAIL = forSinglePlant(MDPlantBlocks.HORSETAIL);
    public static final IBlockGenerator LAKEWEED = forSinglePlant(MDPlantBlocks.LAKEWEED);
    public static final IBlockGenerator SEEPWEED = forSinglePlant(MDPlantBlocks.SEEPWEED);
    public static final IBlockGenerator CAVE_GRASS = forSinglePlant(MDPlantBlocks.CAVE_GRASS);
    public static final IBlockGenerator NUDWART = forSinglePlant(MDPlantBlocks.NUDWART);
    public static final IBlockGenerator COTTONSEDGE = forSinglePlant(MDPlantBlocks.COTTONSEDGE);
    public static final IBlockGenerator RED_GRASS = forSinglePlant(MDPlantBlocks.RED_GRASS);
    public static final IBlockGenerator DEAD_GRASS = forSinglePlant(MDPlantBlocks.DEAD_GRASS);
    public static final IBlockGenerator PEBBLES = forSinglePlant(MDPlantBlocks.PEBBLES);

    public static final IBlockGenerator MURK_FLOWERS = forSinglePlant(MDPlantBlocks.MURK_FLOWER);
    public static final IBlockGenerator MILKY_EYE = forSinglePlant(MDPlantBlocks.MILKY_EYE);
    public static final IBlockGenerator EGIUM = forSinglePlant(MDPlantBlocks.EGIUM);
    public static final IBlockGenerator SHADE_BLUE = forSinglePlant(MDPlantBlocks.SHADE_BLUE);
    public static final IBlockGenerator TURUPT = forSinglePlant(MDPlantBlocks.TURUPT);
    public static final IBlockGenerator CREEP_OF_THE_MOOR = forSinglePlant(MDPlantBlocks.CREEP_OF_THE_MOOR);

    public static final IBlockGenerator RED_BULBFLOWER = forSinglePlant(MDPlantBlocks.RED_BULBFLOWER);
    public static final IBlockGenerator YELLOW_BULBFLOWER = forSinglePlant(MDPlantBlocks.YELLOW_BULBFLOWER);
    public static final IBlockGenerator BLUE_BULBFLOWER = forSinglePlant(MDPlantBlocks.BLUE_BULBFLOWER);

    public static final IBlockGenerator RANDOM_BULBFLOWER = random(
        RED_BULBFLOWER,
        YELLOW_BULBFLOWER,
        BLUE_BULBFLOWER
    );

    public static final IBlockGenerator SEEDLE = forSinglePlant(MDPlantBlocks.SEEDLE);
    public static final IBlockGenerator DOTTED_MUSHROOM = forSinglePlant(MDPlantBlocks.DOTTED_MUSHROOM);
    public static final IBlockGenerator BLACK_MUSHROOM = forSinglePlant(MDPlantBlocks.BLACK_MUSHROOM);

    public static final IBlockGenerator HEATH = forSinglePlant(MDPlantBlocks.HEATH);
    public static final IBlockGenerator FLOWERED_HEATH = forSinglePlant(MDPlantBlocks.FLOWERED_HEATH);
    public static final IBlockGenerator DEAD_HEATH = forSinglePlant(MDPlantBlocks.DEAD_HEATH);

    public static final IBlockGenerator RANDOM_HEATH = weighted(HEATH, 6)
                                                           .add(FLOWERED_HEATH, 1)
                                                           .add(DEAD_HEATH, 0.1)
                                                           .build();

    public static final IBlockGenerator RANDOM_LIVING_HEATH = weighted(HEATH, 6)
                                                                  .add(FLOWERED_HEATH, 1)
                                                                  .build();

    public static final IBlockGenerator CATTAIL = forDoublePlant(MDPlantBlocks.CATTAIL);
    public static final IBlockGenerator MURK_LAVENDER = forDoublePlant(MDPlantBlocks.MURK_LAVENDER);
    public static final IBlockGenerator FOXGLOVE = forDoublePlant(MDPlantBlocks.FOXGLOVE);
    public static final IBlockGenerator GLOBE_THISTLE = forDoublePlant(MDPlantBlocks.GLOBE_THISTLE);

    public static final IBlockGenerator WIREPLANT = forSinglePlant(MDPlantBlocks.WIREPLANT);
    public static final IBlockGenerator FLOWERED_WIREPLANT = forSinglePlant(MDPlantBlocks.FLOWERED_WIREPLANT);

    public static final IBlockGenerator RANDOM_WIREPLANT = weighted(WIREPLANT, 4)
                                                               .add(FLOWERED_WIREPLANT, 1)
                                                               .build();

    public static final IBlockGenerator MURINA = forTallPlant(MDPlantBlocks.MURINA, rng -> rng.nextInt(15) + 1);
    public static final IBlockGenerator HANGING_MOSS = forTallPlant(MDPlantBlocks.HANGING_MOSS, rng -> rng.nextInt(7) + 1);

    public static final IBlockGenerator WATERGRASS_SMALL = forTallPlant(MDPlantBlocks.WATERGRASS, rng -> rng.nextInt(3) + 1);
    public static final IBlockGenerator WATERGRASS_LARGE = forTallPlant(MDPlantBlocks.WATERGRASS, rng -> rng.nextInt(10) + 1);

    public static final IBlockGenerator MUXUS_BUSH_SINGLE = new SimpleBushBlockGenerator(MDPlantBlocks.MUXUS_BUSH);
    public static final IBlockGenerator MUXUS_BUSH = new BushBlockGenerator(MDPlantBlocks.MUXUS_BUSH);
    public static final IBlockGenerator WATER_WIRE = forBlock(MDPlantBlocks.WATER_WIRE);

    public static final IBlockGenerator MOSS = forFacingPlant(MDPlantBlocks.MOSS);
    public static final IBlockGenerator DEAD_MOSS = forFacingPlant(MDPlantBlocks.DEAD_MOSS);
    public static final IBlockGenerator LICHEN = forFacingPlant(MDPlantBlocks.LICHEN);

    public static final IBlockGenerator GOO_DRIPS = forSinglePlant(MDPlantBlocks.GOO_DRIPS);

    public static final IBlockGenerator MURK_REED = new MurkReedBlockGenerator();
    public static final IBlockGenerator PUDDLE = new PuddleBlockGenerator();
    public static final IBlockGenerator FLY_FLOWER = new FlyFlowerBlockGenerator();
    public static final IBlockGenerator ALGAE = new AlgaeBlockGenerator();
    public static final IBlockGenerator SOUL_LIGHT = new SoulLightBlockGenerator();

    private MDBlockGenerators() {
    }

    public static IBlockGenerator forBlockState(BlockState block) {
        return new SimpleBlockGenerator(block);
    }

    public static IBlockGenerator forBlock(Block block) {
        return new SimpleBlockGenerator(block);
    }

    public static IBlockGenerator forSinglePlant(SingleDirectionalPlantBlock block) {
        return new SinglePlantBlockGenerator(block);
    }

    public static IBlockGenerator forDoublePlant(DoubleDirectionalPlantBlock block) {
        return new DoublePlantBlockGenerator(block);
    }

    public static IBlockGenerator forTallPlant(TallDirectionalPlantBlock block, Function<Random, Integer> heightFn) {
        return new TallPlantBlockGenerator(block, heightFn);
    }

    public static IBlockGenerator forFacingPlant(FacingPlantBlock block) {
        return new FacingPlantBlockGenerator(block);
    }

    public static IBlockGenerator random(IBlockGenerator first, IBlockGenerator... rest) {
        return new RandomBlockGenerator(first, rest);
    }

    public static IBlockGenerator alternatives(IBlockGenerator first, IBlockGenerator... rest) {
        return new AlternativeBlockGenerator(first, rest);
    }

    public static IBlockGenerator conditional(IBlockGenerator gen, IBlockGenCondition cond) {
        return new ConditionalBlockGenerator(cond, gen);
    }

    public static WeightedBlockGenerator.Builder weighted(IBlockGenerator first, double wgt) {
        return WeightedBlockGenerator.builder(first, wgt);
    }
}
