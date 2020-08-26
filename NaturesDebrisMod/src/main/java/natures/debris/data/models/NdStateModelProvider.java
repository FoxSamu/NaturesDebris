package natures.debris.data.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import natures.debris.data.models.modelgen.IModelGen;
import natures.debris.data.models.stategen.IBlockStateGen;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NdStateModelProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder()
                                         .setPrettyPrinting()
                                         .disableHtmlEscaping()
                                         .create();

    private final DataGenerator datagen;

    private final Map<Block, IBlockStateGen> blockStateData = new HashMap<>();
    private final Map<Item, IModelGen> itemModelData = new HashMap<>();
    private final Map<String, IModelGen> blockModelData = new HashMap<>();

    public NdStateModelProvider(DataGenerator datagen) {
        this.datagen = datagen;
    }

    @Override
    public void act(DirectoryCache cache) {
        blockStateData.clear();
        blockModelData.clear();
        itemModelData.clear();

        BlockStateTable.registerBlockStates((block, stategen) -> {
            blockStateData.put(block, stategen);
            stategen.getModels(blockModelData::put);
        });
        ItemModelTable.registerItemModels(itemModelData::put);

        Path path = datagen.getOutputFolder();
        blockStateData.forEach((block, state) -> {
            ResourceLocation id = block.getRegistryName();
            assert id != null;

            Path out = getPath(path, id, "blockstates");

            try {
                IDataProvider.save(GSON, cache, state.makeJson(id, block), out);
            } catch (IOException exc) {
                LOGGER.error("Couldn't save blockstate {}", out, exc);
            }
        });

        itemModelData.forEach((item, model) -> {
            ResourceLocation id = item.getRegistryName();
            assert id != null;

            Path out = getPath(path, id, "models/item");

            try {
                IDataProvider.save(GSON, cache, model.makeJson(id), out);
            } catch (IOException exc) {
                LOGGER.error("Couldn't save item model {}", out);
            }
        });

        blockModelData.forEach((name, model) -> {
            ResourceLocation id = new ResourceLocation(name);

            Path out = getPath(path, id, "models");

            try {
                IDataProvider.save(GSON, cache, model.makeJson(id), out);
            } catch (IOException exc) {
                LOGGER.error("Couldn't save block model {}", out);
            }
        });
    }

    @Override
    public String getName() {
        return "NaturesDebris/StatesModels";
    }

    private static Path getPath(Path path, ResourceLocation id, String folder) {
        return path.resolve(String.format("assets/%s/%s/%s.json", id.getNamespace(), folder, id.getPath()));
    }
}
