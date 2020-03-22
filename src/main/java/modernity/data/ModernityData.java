/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.data;

import modernity.api.IModernity;
import modernity.data.loot.MDLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class ModernityData implements IModernity {
    private final DataService dataService = new DataService();

    @SubscribeEvent
    public void gather( GatherDataEvent event ) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider( new MDLootTableProvider( generator ) );
    }

    @Override
    public DataService getDataService() {
        return dataService;
    }
}
