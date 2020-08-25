/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.biome;

import modernity.common.generator.decorate.decoration.*;
import natures.debris.common.environment.precipitation.IPrecipitationFunction;
import natures.debris.common.generator.MurkSurfaceGeneration;
import natures.debris.common.generator.blocks.MDBlockGenerators;
import natures.debris.common.generator.blocks.WeightedBlockGenerator;
import natures.debris.common.generator.decorate.count.Chance;
import natures.debris.common.generator.decorate.count.ChanceMinMax;
import natures.debris.common.generator.decorate.count.Fixed;
import natures.debris.common.generator.decorate.count.MinMax;
import natures.debris.common.generator.decorate.decoration.*;
import natures.debris.common.generator.decorate.decorator.DecorationDecorator;
import natures.debris.common.generator.decorate.position.Surface;
import natures.debris.common.generator.surface.ForestSurfaceGenerator;
import natures.debris.common.generator.surface.GrassSurfaceGenerator;
import natures.debris.common.generator.surface.MoorlandSurfaceGenerator;
import natures.debris.common.generator.surface.SandSurfaceGenerator;
import natures.debris.common.generator.tree.MDTrees;
import net.minecraft.world.gen.Heightmap;

/**
 * The 'Meadow' or 'modernity:meadow' biome.
 */
public class ShrublandBiome extends ModernityBiome {
    protected ShrublandBiome(Type type) {
        super(type.builder);
        BiomeGroups.registerBiomeToGroup("shrubland", this);
        BiomeGroups.registerBiomeToGroup("moor_like", this);
        if (type == Type.SHRUBLAND_RIVER) {
            BiomeGroups.registerBiomeToGroup("rivers", this);
        }

        setGrowingPlants(
            WeightedBlockGenerator.builder(MDBlockGenerators.MURK_GRASS_1, 900)
                                  .add(MDBlockGenerators.RANDOM_LIVING_HEATH, 30)
                                  .add(MDBlockGenerators.CREEP_OF_THE_MOOR, 10)
                                  .add(MDBlockGenerators.RANDOM_MILLIUM, 70)
                                  .add(MDBlockGenerators.BLACK_MUSHROOM, 20)
                                  .add(MDBlockGenerators.DOTTED_MUSHROOM, 20)
                                  .add(MDBlockGenerators.SEEDLE, 20)
                                  .build()
        );

        MurkSurfaceGeneration.addCaveDeposits(this);
        MurkSurfaceGeneration.addCaveOres(this);
        MurkSurfaceGeneration.addCavePlants(this);
        MurkSurfaceGeneration.addCaveSprings(this);
        MurkSurfaceGeneration.addPebbles(this);

        addDecorator(new DecorationDecorator(new GroupedBushDecoration(3, 5, 4, MDBlockGenerators.MURK_REED), new Surface(Heightmap.Type.OCEAN_FLOOR_WG), new Chance(1, 3)));

        addDecorator(new DecorationDecorator(new SingleBushDecoration(MDBlockGenerators.SOUL_LIGHT), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new Chance(0.1)));

        if (type == Type.SHRUBLAND_HEATH) {
            addDecorator(new DecorationDecorator(new GroupedBushDecoration(8, 5, 6, MDBlockGenerators.MUXUS_BUSH_SINGLE), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new MinMax(3, 4)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 7, MDBlockGenerators.CREEP_OF_THE_MOOR), new Surface(Heightmap.Type.MOTION_BLOCKING), new ChanceMinMax(0.7, 2, 4)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 7, MDBlockGenerators.DEAD_HEATH), new Surface(Heightmap.Type.MOTION_BLOCKING), new ChanceMinMax(0.6, 1, 2)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 7, MDBlockGenerators.RANDOM_LIVING_HEATH), new Surface(Heightmap.Type.MOTION_BLOCKING), new Fixed(23)));
        }

        if (type == Type.SHRUBLAND_GRASS) {
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.MURK_GRASS_BASIC), new Surface(Heightmap.Type.MOTION_BLOCKING), new MinMax(9, 16)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.HORSETAIL), new Surface(Heightmap.Type.MOTION_BLOCKING), new MinMax(2, 5)));
        } else {
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.MURK_GRASS_BASIC), new Surface(Heightmap.Type.MOTION_BLOCKING), new MinMax(2, 7)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.DEAD_GRASS), new Surface(Heightmap.Type.MOTION_BLOCKING), new MinMax(1, 2)));
        }

        if (type == Type.SHRUBLAND_REEDS) {
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.HORSETAIL), new Surface(Heightmap.Type.MOTION_BLOCKING), new MinMax(9, 16)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.MURK_REED), new Surface(Heightmap.Type.OCEAN_FLOOR_WG), new MinMax(2, 5)));
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.CATTAIL), new Surface(Heightmap.Type.OCEAN_FLOOR_WG), new MinMax(1, 2)));
        }

        if (type == Type.SHRUBLAND_RIVER) {
            addDecorator(new DecorationDecorator(new ClusterBushDecoration(120, 6, MDBlockGenerators.CATTAIL), new Surface(Heightmap.Type.OCEAN_FLOOR_WG), new MinMax(4, 8)));
        }

        if (type == Type.SHRUBLAND) {
            addDecorator(new DecorationDecorator(new GroupedBushDecoration(8, 5, 6, MDBlockGenerators.MUXUS_BUSH_SINGLE), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new MinMax(5, 7)));
        }

        addDecorator(new DecorationDecorator(
            new SelectiveDecoration()
                .add(
                    new SelectiveDecoration()
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.RED_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.MAGENTA_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.BLUE_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.YELLOW_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.GREEN_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.CYAN_MILLIUM), 1)
                        .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.WHITE_MILLIUM), 1),
                    70
                )
                .add(new GroupedBushDecoration(5, 3, 3, MDBlockGenerators.RANDOM_WIREPLANT), 30)
                .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.BLACK_MUSHROOM), 20)
                .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.DOTTED_MUSHROOM), 20)
                .add(new ClusterBushDecoration(81, 5, MDBlockGenerators.SEEDLE), 20),
            new Surface(Heightmap.Type.OCEAN_FLOOR_WG),
            new Chance(0.6)
        ));

        addDecorator(new DecorationDecorator(new ClusterBushDecoration(81, 7, MDBlockGenerators.WATERGRASS_SMALL), new Surface(Heightmap.Type.OCEAN_FLOOR_WG), new Fixed(9)));
        addDecorator(new DecorationDecorator(new ClusterBushDecoration(125, 7, MDBlockGenerators.LAKEWEED), new Surface(Heightmap.Type.OCEAN_FLOOR_WG)));

        if (type == Type.SHRUBLAND_TREES) {
            addDecorator(new DecorationDecorator(new TreeDecoration(MDTrees.BLACKWOOD), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new MinMax(1, 2)));
            addDecorator(new DecorationDecorator(new TreeDecoration(MDTrees.BLACKWOOD_TINY), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new MinMax(0, 2)));
        } else {
            addDecorator(new DecorationDecorator(new TreeDecoration(MDTrees.BLACKWOOD_TINY), new Surface(Heightmap.Type.WORLD_SURFACE_WG), new Chance(1, 30)));
        }
    }

    public enum Type {
        SHRUBLAND(
            new Builder()
                .depth(2).variation(2).scale(2)
                .surfaceGen(new MoorlandSurfaceGenerator(6, 11, 9))
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_HEATH(
            new Builder()
                .depth(2).variation(2).scale(1)
                .surfaceGen(new MoorlandSurfaceGenerator(11, 7, 6))
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_GRASS(
            new Builder()
                .depth(1).variation(1).scale(2)
                .surfaceGen(new GrassSurfaceGenerator())
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_TREES(
            new Builder()
                .depth(3).variation(3).scale(2)
                .surfaceGen(new ForestSurfaceGenerator(2, 8, 12))
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_SAND(
            new Builder()
                .depth(2).variation(1).scale(1)
                .surfaceGen(new SandSurfaceGenerator())
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_REEDS(
            new Builder()
                .depth(0.2).variation(0).scale(1)
                .surfaceGen(new GrassSurfaceGenerator())
                .precipitation(IPrecipitationFunction.standard())
        ),
        SHRUBLAND_RIVER(
            new Builder()
                .depth(-6).variation(0).scale(2)
                .blendWeight(10)
                .surfaceGen(new GrassSurfaceGenerator())
                .precipitation(IPrecipitationFunction.standard())
        );

        private final Builder builder;

        Type(Builder builder) {
            this.builder = builder;
        }
    }
}
