package natures.debris.data;

import natures.debris.client.NaturesDebrisClient;
import natures.debris.common.handler.RegistryHandler;
import natures.debris.data.loottables.NdLootTableProvider;
import natures.debris.data.models.NdStateModelProvider;
import natures.debris.data.recipes.NdRecipeProvider;
import natures.debris.data.recipes.NdStonecuttingRecipeProvider;
import natures.debris.data.tags.NdBlockTagsProvider;
import natures.debris.data.tags.NdFluidTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NaturesDebrisData extends NaturesDebrisClient {
    @Override
    protected void registerEventListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new RegistryHandler());
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void onGatherData(GatherDataEvent evt) {
        DataGenerator gen = evt.getGenerator();
        if (evt.includeClient()) {
            gen.addProvider(new NdStateModelProvider(gen));
        }
        if (evt.includeServer()) {
            gen.addProvider(new NdLootTableProvider(gen));
            gen.addProvider(new NdBlockTagsProvider(gen));
            gen.addProvider(new NdFluidTagsProvider(gen));
            gen.addProvider(new NdRecipeProvider(gen));
            gen.addProvider(new NdStonecuttingRecipeProvider(gen));
        }
    }
}
