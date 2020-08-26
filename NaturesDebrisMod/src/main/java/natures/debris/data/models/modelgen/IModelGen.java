package natures.debris.data.models.modelgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface IModelGen {
    IModelGen EMPTY = name -> new JsonObject();

    JsonElement makeJson(ResourceLocation name);
}
