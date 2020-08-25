/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.data;

import natures.debris.common.loot.MDLootTables;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.data.lang.TranslationKeyProvider;
import natures.debris.data.loot.MDLootTableProvider;
import natures.debris.data.recipes.MDRecipeProvider;
import natures.debris.generic.IModernityOld;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModernityData implements IModernityOld {
    private final DataService dataService = new DataService();

    @Override
    public void setupRegistryHandler() {
        FMLJavaModLoadingContext.get().getModEventBus().register(RegistryEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RegistryEventHandler.INSTANCE);
    }

    @Override
    public void preInit() {
        MDLootTables.register();
    }

    @SubscribeEvent
    public void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(new MDLootTableProvider(generator));
        generator.addProvider(new MDRecipeProvider(generator));
        generator.addProvider(new TranslationKeyProvider(generator));
    }

    @Override
    public DataService getDataService() {
        return dataService;
    }
}
