/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.data;

import modernity.data.lang.TranslationKeyProvider;
import modernity.data.loot.MDLootTableProvider;
import modernity.data.recipes.MDRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public final class ModernityDataGenerator {
    public static final ModernityDataGenerator INSTANCE = new ModernityDataGenerator();

    private ModernityDataGenerator() {

    }

    public static void gather( GatherDataEvent event ) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider( new MDLootTableProvider( generator ) );
        generator.addProvider( new MDRecipeProvider( generator ) );
        generator.addProvider( new TranslationKeyProvider( generator ) );
    }
}
