package natures.debris.data.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;

import natures.debris.common.item.group.NdItemGroup;
import natures.debris.NdInfo;

public class NdLanguageProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder()
                                         .setPrettyPrinting()
                                         .disableHtmlEscaping()
                                         .create();

    private final DataGenerator generator;

    public NdLanguageProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        JsonObject lang = new JsonObject();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (Objects.requireNonNull(block.getRegistryName()).getNamespace().equals(NdInfo.ID)) {
                lang.addProperty(block.getTranslationKey(), "");
            }
        }
        for (Item item : ForgeRegistries.ITEMS) {
            if (Objects.requireNonNull(item.getRegistryName()).getNamespace().equals(NdInfo.ID)) {
                lang.addProperty(item.getTranslationKey(), "");
            }
        }
        for (NdItemGroup itemGroup : NdItemGroup.getGroups()) {
            lang.addProperty(itemGroup.getTranslationKey(), "");
        }
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (Objects.requireNonNull(biome.getRegistryName()).getNamespace().equals(NdInfo.ID)) {
                lang.addProperty(biome.getTranslationKey(), "");
            }
        }

        IDataProvider.save(GSON, cache, lang, generator.getOutputFolder().resolve("assets/lang/generated.json"));
    }

    @Override
    public String getName() {
        return "NaturesDebris/Language";
    }
}
