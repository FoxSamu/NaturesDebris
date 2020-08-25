/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.registryold;

import com.google.common.reflect.TypeToken;
import natures.debris.common.ModernityOld;
import natures.debris.common.area.MDAreas;
import natures.debris.common.area.core.AreaType;
import natures.debris.common.biome.MDBiomes;
import natures.debris.common.blockold.MDBlocks;
import natures.debris.common.container.MDContainerTypes;
import natures.debris.common.entity.MDEntityTypes;
import natures.debris.common.environment.event.EnvironmentEventType;
import natures.debris.common.environment.event.MDEnvEvents;
import natures.debris.common.event.BlockEvent;
import natures.debris.common.event.MDBlockEvents;
import natures.debris.common.fluidold.MDFluids;
import natures.debris.common.generator.MDChunkGeneratorTypes;
import natures.debris.common.generator.structure.MDStructures;
import natures.debris.common.itemold.MDItems;
import natures.debris.common.particle.MDParticleTypes;
import natures.debris.common.recipes.MDRecipeSerializers;
import natures.debris.common.sound.MDSoundEvents;
import natures.debris.common.tileentity.MDTileEntitiyTypes;
import natures.debris.common.world.dimen.MDDimensions;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Handles the actual registry events.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public enum RegistryEventHandler {
    INSTANCE;

    private final HashMap<Class<? extends IForgeRegistryEntry<?>>, HashSet<RegistryHandler<?>>> registries = new HashMap<>();

    RegistryEventHandler() {
        MDItems.setup(this);
        MDBlocks.setup(this);
        MDFluids.setup(this);
        MDEntityTypes.setup(this);
        MDContainerTypes.setup(this);
        MDTileEntitiyTypes.setup(this);
        MDDimensions.setup(this);
        MDBiomes.setup(this);
        MDChunkGeneratorTypes.setup(this);
        MDStructures.setup(this);
        MDBlockEvents.setup(this);
        MDEnvEvents.setup(this);
        MDAreas.setup(this);
        MDParticleTypes.setup(this);
        MDRecipeSerializers.setup(this);
        MDSoundEvents.setup(this);
    }

    private <T extends IForgeRegistryEntry<T>> void register(Class<T> entryClass, IForgeRegistry<T> registry) {
        HashSet<RegistryHandler<?>> list = registries.get(entryClass);
        if (list == null)
            return;
        for (RegistryHandler<?> handler : list) {
            handler.fillRegistry(registry);
        }
    }

    private <T extends IForgeRegistryEntry<T>> void remap(Class<T> entryClass, List<RegistryEvent.MissingMappings.Mapping<T>> registry) {
        HashSet<RegistryHandler<?>> list = registries.get(entryClass);
        if (list == null)
            return;
        for (RegistryHandler<?> handler : list) {
            handler.remap(registry);
        }
    }

    /**
     * Adds a {@link RegistryHandler} for a specific registry type.
     */
    public <T extends IForgeRegistryEntry<T>> void addHandler(Class<T> entryClass, RegistryHandler<? extends T> handler) {
        registries.computeIfAbsent(entryClass, ec -> new HashSet<>()).add(handler);
    }

    // No subscription to generic event types: we want to listen to all registries and automatically detect a
    // registered RegistryHandler for that class... It saves us a lot of code here...
    @SubscribeEvent
    public void registerAll(RegistryEvent.Register event) {
        register(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerRegistries(RegistryEvent.NewRegistry event) {
        TypeToken<BlockEvent<?>> blockEventToken = new TypeToken<BlockEvent<?>>() {
        };
        TypeToken<AreaType<?>> areaTypeToken = new TypeToken<AreaType<?>>() {
        };
        new RegistryBuilder<BlockEvent<?>>()
            .setType((Class<BlockEvent<?>>) blockEventToken.getRawType())
            .setMaxID(Integer.MAX_VALUE - 1)
            .disableSaving()
            .setName(ModernityOld.res("block_events"))
            .create();

        new RegistryBuilder<EnvironmentEventType>()
            .setType(EnvironmentEventType.class)
            .setMaxID(Integer.MAX_VALUE - 1)
            .setName(ModernityOld.res("environment_events"))
            .create();

        new RegistryBuilder<AreaType<?>>()
            .setType((Class<AreaType<?>>) areaTypeToken.getRawType())
            .setMaxID(Integer.MAX_VALUE - 1)
            .setName(ModernityOld.res("area_types"))
            .create();
    }

    @SubscribeEvent
    public void remapMissing(RegistryEvent.MissingMappings event) {
        remap(event.getRegistry().getRegistrySuperType(), event.getAllMappings());
    }
}
