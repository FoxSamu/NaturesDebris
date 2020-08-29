package natures.debris.data.models.stategen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import natures.debris.data.models.modelgen.IModelGen;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class VariantBlockStateGen implements IBlockStateGen {
    private final Map<String, ModelInfo[]> variants = new HashMap<>();

    @Override
    public JsonElement makeJson(ResourceLocation id, Block block) {
        JsonObject root = new JsonObject();

        JsonObject variants = new JsonObject();
        for (Map.Entry<String, ModelInfo[]> variant : this.variants.entrySet()) {
            JsonElement variantJson = ModelInfo.makeJson(variant.getValue());
            variants.add(variant.getKey(), variantJson);
        }
        root.add("variants", variants);
        return root;
    }

    @Override
    public void getModels(BiConsumer<String, IModelGen> consumer) {
        for (ModelInfo[] infos : variants.values()) {
            for (ModelInfo info : infos) {
                info.getModels(consumer);
            }
        }
    }

    public VariantBlockStateGen variant(String variant, ModelInfo... models) {
        variants.put(variant, models.clone());
        return this;
    }

    public static VariantBlockStateGen create(String variant, ModelInfo... models) {
        return new VariantBlockStateGen().variant(variant, models);
    }

    public static VariantBlockStateGen create(ModelInfo... models) {
        return new VariantBlockStateGen().variant("", models);
    }

    public static VariantBlockStateGen create() {
        return new VariantBlockStateGen();
    }
}
