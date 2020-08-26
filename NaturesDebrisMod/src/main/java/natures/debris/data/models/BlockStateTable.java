package natures.debris.data.models;

import natures.debris.common.block.NdBlocks;
import natures.debris.data.models.modelgen.IModelGen;
import natures.debris.data.models.stategen.IBlockStateGen;
import natures.debris.data.models.stategen.ModelInfo;
import natures.debris.data.models.stategen.VariantBlockStateGen;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static natures.debris.data.models.modelgen.InheritingModelGen.*;

public final class BlockStateTable {
    private static BiConsumer<Block, IBlockStateGen> consumer;

    public static void registerBlockStates(BiConsumer<Block, IBlockStateGen> c) {
        consumer = c;

        register(NdBlocks.ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.ROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_ROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CRACKED_ROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.ROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_ROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CRACKED_ROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.SMOOTH_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.POLISHED_ROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));

        register(NdBlocks.DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.DARKROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.DARKROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.MOSSY_DARKROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.CRACKED_DARKROCK_TILES, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.SMOOTH_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(NdBlocks.POLISHED_DARKROCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
    }

    private static IBlockStateGen simple(String name, IModelGen model) {
        return VariantBlockStateGen.create(ModelInfo.create(name, model));
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

    private static String name(Block block) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;
        return id.toString();
    }


    private BlockStateTable() {
    }
}
