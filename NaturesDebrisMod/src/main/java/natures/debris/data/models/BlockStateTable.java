package natures.debris.data.models;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import natures.debris.common.block.NdBlocks;
import natures.debris.data.models.modelgen.IModelGen;
import natures.debris.data.models.stategen.IBlockStateGen;
import natures.debris.data.models.stategen.ModelInfo;
import natures.debris.data.models.stategen.VariantBlockStateGen;

import static natures.debris.data.models.modelgen.InheritingModelGen.*;

public final class BlockStateTable {
    private static BiConsumer<Block, IBlockStateGen> consumer;

    public static void registerBlockStates(BiConsumer<Block, IBlockStateGen> c) {
        consumer = c;

        register(NdBlocks.ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.ROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.MOSSY_ROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.CRACKED_ROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.ROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_ROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_ROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.POLISHED_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CHISELED_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.ROCK_LANTERN, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.ROCK_PILLAR, block -> rotatedPillar(name(block, "block/%s"), cubeColumn(name(block, "block/%s_top"), name(block, "block/%s_side"))));

        register(NdBlocks.ROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.MOSSY_ROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.ROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.MOSSY_ROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.CRACKED_ROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.ROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_ROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_ROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_ROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.POLISHED_ROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));

        register(NdBlocks.ROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.MOSSY_ROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.ROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.ROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_ROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_ROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_ROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.POLISHED_ROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));

        register(NdBlocks.ROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.MOSSY_ROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.ROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.MOSSY_ROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.CRACKED_ROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.ROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_ROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_ROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_ROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.POLISHED_ROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));

        register(NdBlocks.DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.DARKROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, block -> cubeAllRandomized(name(block, "block/%s"), 16, 2, 2));
        register(NdBlocks.DARKROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_DARKROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_DARKROCK_TILES, block -> cubeAllRandomized(name(block, "block/%s"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.POLISHED_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CHISELED_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.DARKROCK_LANTERN, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.DARKROCK_PILLAR, block -> rotatedPillar(name(block, "block/%s"), cubeColumn(name(block, "block/%s_top"), name(block, "block/%s_side"))));

        register(NdBlocks.DARKROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.MOSSY_DARKROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.DARKROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 16, 2, 2));
        register(NdBlocks.DARKROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_DARKROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_DARKROCK_TILES_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_DARKROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));
        register(NdBlocks.POLISHED_DARKROCK_SLAB, block -> slabRandomized(name(block, "block/%s", "_slab"), 1));

        register(NdBlocks.DARKROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.MOSSY_DARKROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.DARKROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 16, 2, 2));
        register(NdBlocks.DARKROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_DARKROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));
        register(NdBlocks.POLISHED_DARKROCK_STAIRS, block -> stairsRandomized(name(block, "block/%s", "_stairs"), 1));

        register(NdBlocks.DARKROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.MOSSY_DARKROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.DARKROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 16, 2, 2));
        register(NdBlocks.DARKROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.MOSSY_DARKROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.CRACKED_DARKROCK_TILES_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 32, 2, 4, 2));
        register(NdBlocks.SMOOTH_DARKROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
        register(NdBlocks.POLISHED_DARKROCK_STEP, block -> stepRandomized(name(block, "block/%s", "_step"), 1));
    }

    private static IBlockStateGen simple(String name, IModelGen model) {
        return VariantBlockStateGen.create(ModelInfo.create(name, model));
    }

    private static IBlockStateGen cubeAllRandomized(String name, int... weights) {
        ModelInfo[] random = new ModelInfo[weights.length];
        for (int i = 0; i < random.length; i++) {
            String n = name + (i == 0 ? "" : "_alt_" + i);
            random[i] = ModelInfo.create(n, cubeAll(n)).weight(weights[i]);
        }
        return VariantBlockStateGen.create(random);
    }

    private static IBlockStateGen slabRandomized(String name, int... weights) {
        ModelInfo[] lo = new ModelInfo[weights.length];
        ModelInfo[] hi = new ModelInfo[weights.length];
        ModelInfo[] dbl = new ModelInfo[weights.length];
        for (int i = 0; i < lo.length; i++) {
            String ln = name + (i == 0 ? "_slab" : "_slab_alt_" + i);
            String hn = name + (i == 0 ? "_slab_top" : "_slab_top_alt_" + i);
            String n = name + (i == 0 ? "" : "_alt_" + i);
            lo[i] = ModelInfo.create(ln, slab(n)).weight(weights[i]);
            hi[i] = ModelInfo.create(hn, slabTop(n)).weight(weights[i]);
            dbl[i] = ModelInfo.create(n).weight(weights[i]);
        }
        return VariantBlockStateGen.create("type=bottom", lo)
                                   .variant("type=top", hi)
                                   .variant("type=double", dbl);
    }

    private static IBlockStateGen stairsRandomized(String name, int... weights) {
        ModelInfo[] innerL = new ModelInfo[weights.length];
        ModelInfo[] outerL = new ModelInfo[weights.length];
        ModelInfo[] innerR = new ModelInfo[weights.length];
        ModelInfo[] outerR = new ModelInfo[weights.length];
        ModelInfo[] stairs = new ModelInfo[weights.length];
        boolean[] innerM = new boolean[weights.length];
        boolean[] outerM = new boolean[weights.length];
        boolean[] stairsM = new boolean[weights.length];

        VariantBlockStateGen gen = VariantBlockStateGen.create();
        int y = 270;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                int x = half == Half.TOP ? 180 : 0;
                String state = String.format("facing=%s,half=%s", dir.getName(), half.getName());

                for (int i = 0; i < innerL.length; i++) {
                    String in = name + (i == 0 ? "_stairs_inner" : "_stairs_inner_alt_" + i);
                    String on = name + (i == 0 ? "_stairs_outer" : "_stairs_outer_alt_" + i);
                    String sn = name + (i == 0 ? "_stairs" : "_stairs_alt_" + i);
                    String tn = name + (i == 0 ? "" : "_alt_" + i);

                    int yp = y == 0 ? 270 : y - 90;
                    int yn = y == 270 ? 0 : y + 90;

                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stairsInner(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stairsOuter(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    innerR[i] = ModelInfo.create(in)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerR[i] = ModelInfo.create(on)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : stairs(tn))
                                         .rotate(x, y)
                                         .uvlock(true)
                                         .weight(weights[i]);

                    innerM[i] = true;
                    outerM[i] = true;
                    stairsM[i] = true;
                }

                gen.variant(state + ",shape=straight", stairs);
                gen.variant(state + ",shape=inner_left", innerL);
                gen.variant(state + ",shape=inner_right", innerR);
                gen.variant(state + ",shape=outer_left", outerL);
                gen.variant(state + ",shape=outer_right", outerR);
            }
            if (y == 270) y = 0;
            else y += 90;
        }
        return gen;
    }

    private static IBlockStateGen stepRandomized(String name, int... weights) {
        ModelInfo[] innerL = new ModelInfo[weights.length];
        ModelInfo[] outerL = new ModelInfo[weights.length];
        ModelInfo[] innerR = new ModelInfo[weights.length];
        ModelInfo[] outerR = new ModelInfo[weights.length];
        ModelInfo[] stairs = new ModelInfo[weights.length];
        boolean[] innerM = new boolean[weights.length];
        boolean[] outerM = new boolean[weights.length];
        boolean[] stairsM = new boolean[weights.length];

        VariantBlockStateGen gen = VariantBlockStateGen.create();
        int y = 270;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                int x = half == Half.TOP ? 180 : 0;
                String state = String.format("facing=%s,half=%s", dir.getName(), half.getName());

                for (int i = 0; i < innerL.length; i++) {
                    String in = name + (i == 0 ? "_step_inner" : "_step_inner_alt_" + i);
                    String on = name + (i == 0 ? "_step_outer" : "_step_outer_alt_" + i);
                    String sn = name + (i == 0 ? "_step" : "_step_alt_" + i);
                    String tn = name + (i == 0 ? "" : "_alt_" + i);

                    int yp = y == 0 ? 270 : y - 90;
                    int yn = y == 270 ? 0 : y + 90;

                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stepInner(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stepOuter(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    innerR[i] = ModelInfo.create(in)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerR[i] = ModelInfo.create(on)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : step(tn))
                                         .rotate(x, y)
                                         .uvlock(true)
                                         .weight(weights[i]);

                    innerM[i] = true;
                    outerM[i] = true;
                    stairsM[i] = true;
                }

                gen.variant(state + ",shape=straight", stairs);
                gen.variant(state + ",shape=inner_left", innerL);
                gen.variant(state + ",shape=inner_right", innerR);
                gen.variant(state + ",shape=outer_left", outerL);
                gen.variant(state + ",shape=outer_right", outerR);
            }
            if (y == 270) y = 0;
            else y += 90;
        }
        return gen;
    }

    private static IBlockStateGen rotateY(String name, IModelGen model) {
        return VariantBlockStateGen.create(
            ModelInfo.create(name, model).rotate(0, 0),
            ModelInfo.create(name, model).rotate(0, 90),
            ModelInfo.create(name, model).rotate(0, 180),
            ModelInfo.create(name, model).rotate(0, 270)
        );
    }

    private static IBlockStateGen rotateXY(String name, IModelGen model) {
        return VariantBlockStateGen.create(
            ModelInfo.create(name, model).rotate(0, 0),
            ModelInfo.create(name, model).rotate(0, 90),
            ModelInfo.create(name, model).rotate(0, 180),
            ModelInfo.create(name, model).rotate(0, 270),
            ModelInfo.create(name, model).rotate(90, 0),
            ModelInfo.create(name, model).rotate(90, 90),
            ModelInfo.create(name, model).rotate(90, 180),
            ModelInfo.create(name, model).rotate(90, 270),
            ModelInfo.create(name, model).rotate(180, 0),
            ModelInfo.create(name, model).rotate(180, 90),
            ModelInfo.create(name, model).rotate(180, 180),
            ModelInfo.create(name, model).rotate(180, 270),
            ModelInfo.create(name, model).rotate(270, 0),
            ModelInfo.create(name, model).rotate(270, 90),
            ModelInfo.create(name, model).rotate(270, 180),
            ModelInfo.create(name, model).rotate(270, 270)
        );
    }

    private static IBlockStateGen doublePlant(String lower, IModelGen lowerModel, String upper, IModelGen upperModel) {
        return VariantBlockStateGen.create("half=lower", ModelInfo.create(lower, lowerModel))
                                   .variant("half=upper", ModelInfo.create(upper, upperModel));
    }

    private static IBlockStateGen rotatedPillar(String name, IModelGen model) {
        return VariantBlockStateGen.create("axis=y", ModelInfo.create(name, model).rotate(0, 0))
                                   .variant("axis=z", ModelInfo.create(name, model).rotate(90, 0))
                                   .variant("axis=x", ModelInfo.create(name, model).rotate(90, 90));
    }

    private static void register(Block block, Function<Block, IBlockStateGen> genFactory) {
        consumer.accept(block, genFactory.apply(block));
    }

    private static String name(Block block, String nameFormat) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, id.getPath()));
    }

    private static String name(Block block, String nameFormat, String omitSuffix) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;

        String path = id.getPath();
        if (path.endsWith(omitSuffix)) {
            path = path.substring(0, path.length() - omitSuffix.length());
        }

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, path));
    }

    private static String name(Block block) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;
        return id.toString();
    }


    private BlockStateTable() {
    }
}
