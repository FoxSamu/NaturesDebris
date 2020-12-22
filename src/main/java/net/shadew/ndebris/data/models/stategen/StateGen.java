package net.shadew.ndebris.data.models.stategen;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

import net.shadew.ndebris.data.models.modelgen.ModelGen;

public interface StateGen {
    JsonElement makeJson(Identifier id, Block block);
    void getModels(BiConsumer<String, ModelGen> consumer);
}
