package modernity.common.registry;

import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A handler for registry events of a certain type. Instances act like wrappers around the original registry, so that
 * missing mappings could easily be managed and redirected via aliases. This class also provides convenience utilities
 * for registring multiple objects with the same configuration.
 *
 * It is recommended to register entries through this class only when Forge lauches the specific register event for a
 * registry. It is however possible to register objects <em>before</em> the registry event: this happens with block
 * items during the block register event.
 */
public class RegistryHandler<E extends IForgeRegistryEntry<E>> {
    public static final RegistryHandler<Block> BLOCKS = new RegistryHandler<>("modernity", ForgeRegistries.BLOCKS);
    public static final RegistryHandler<Item> ITEMS = new RegistryHandler<>("modernity", ForgeRegistries.ITEMS);
    public static final RegistryHandler<Fluid> FLUIDS = new RegistryHandler<>("modernity", ForgeRegistries.FLUIDS);
    public static final BlockItemRegistryHandler BLOCKS_ITEMS = new BlockItemRegistryHandler();
    public static final RegistryHandler<SoundEvent> SOUND_EVENTS = new RegistryHandler<>("modernity", ForgeRegistries.SOUND_EVENTS);
    public static final RegistryHandler<ModDimension> DIMENSIONS = new RegistryHandler<>("modernity", ForgeRegistries.MOD_DIMENSIONS);
    public static final RegistryHandler<Biome> BIOMES = new RegistryHandler<>("modernity", ForgeRegistries.BIOMES);
    public static final RegistryHandler<ChunkGeneratorType<?, ?>> CHUNK_GENERATORS = new RegistryHandler<>("modernity", ForgeRegistries.CHUNK_GENERATOR_TYPES);
    public static final RegistryHandler<SurfaceBuilder<?>> SURFACE_BUILDERS = new RegistryHandler<>("modernity", ForgeRegistries.SURFACE_BUILDERS);
    private static final HashMap<Class<?>, RegistryHandler<?>> HANDLERS = new HashMap<>();
    private final HashMap<ResourceLocation, Entry> entries = new HashMap<>();
    private final String namespace;
    private final Set<Consumer<RegistryHandler<E>>> listeners = new HashSet<>();


    /**
     * Creates a registry handler.
     *
     * @param namespace The default namespace to use when generating object names for those with unspecified namespace.
     *                  This namespace can be overridden by registring objects under a specified namespace.
     * @param registry  The registry this handler should wrap. This is only used to derive the type class for the
     *                  generic type of this handler.
     */
    public RegistryHandler(String namespace, IForgeRegistry<E> registry) {
        this.namespace = namespace;

        HANDLERS.put(registry.getRegistrySuperType(), this);
    }

    public static void setup() {
        IEventBus fmlEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        fmlEventBus.addListener((Consumer<RegistryEvent.Register<?>>) RegistryHandler::allRegister);
        MinecraftForge.EVENT_BUS.addListener((Consumer<RegistryEvent.MissingMappings<?>>) RegistryHandler::allRemap);

        BLOCKS.addListener(reg -> MDBlocks.register(BLOCKS_ITEMS));
        ITEMS.addListener(MDItems::register);
        FLUIDS.addListener(MDFluids::register);
    }

    private static void allRegister(RegistryEvent.Register<?> event) {
        RegistryHandler<?> handler = HANDLERS.get(event.getRegistry().getRegistrySuperType());
        if(handler != null) {
            handler.register(event.getRegistry());
        }
    }

    private static void allRemap(RegistryEvent.MissingMappings<?> event) {
        RegistryHandler<?> handler = HANDLERS.get(event.getRegistry().getRegistrySuperType());
        handler.handleMissingMappings(event);
    }

    /**
     * Adds a registry event listener. This listener is triggered when Forge casts the specific registry event on this
     * object and must be used to register objects into this registry.
     *
     * @param listener The listener to add. Must not be null.
     */
    public void addListener(@Nonnull Consumer<RegistryHandler<E>> listener) {
        listeners.add(listener);
    }

    /**
     * Creates an ID from the specified name. When no namespace is specified, the default namespace is used.
     */
    ResourceLocation createID(String name) {
        if(name.indexOf(':') >= 0) {
            return new ResourceLocation(name);
        } else {
            return new ResourceLocation(namespace, name);
        }
    }

    /**
     * Adds a registry object. Objects must not have their registry name set, otherwise they must be registered via
     * {@link #add(IForgeRegistryEntry, String...)}.
     *
     * @param id      The registry ID to assign to the object.
     * @param entry   The registry object.
     * @param aliases Alias names to remap when missing mappings are detected.
     * @return This instance for chaining convenience.
     */
    public RegistryHandler<E> add(String id, E entry, String... aliases) {
        ResourceLocation resID = createID(id);
        Entry e = new Entry(resID, entry, aliases);
        entry.setRegistryName(resID);

        entries.put(resID, e);
        for(String alias : aliases) {
            entries.put(createID(alias), e);
        }

        return this;
    }

    /**
     * Adds a registry object with a predefined registry name. Objects must have their registry name set, otherwise they
     * must be registered via {@link #add(String, IForgeRegistryEntry, String...)}.
     *
     * @param entry   The registry object.
     * @param aliases Alias names to remap when missing mappings are detected.
     * @return This instance for chaining convenience.
     */
    public RegistryHandler<E> add(E entry, String... aliases) {
        ResourceLocation resID = entry.getRegistryName();
        Entry e = new Entry(resID, entry, aliases);

        entries.put(resID, e);
        for(String alias : aliases) {
            entries.put(createID(alias), e);
        }

        return this;
    }

    /**
     * Creates a configured registry handler for adding multiple entries with the same config.
     *
     * @param config The base configuration supplier. This supplier is called for each registry object registered
     *               through this instance.
     * @see #configured(Object)
     */
    public <C> Configured<C, E> configured(Supplier<C> config) {
        return new Configured<>(this, config);
    }

    /**
     * Creates a configured registry handler for adding multiple entries with the same config.
     *
     * @param config The base configuration. This config is used for every registry object registered through this
     *               instance.
     * @see #configured(Supplier)
     */
    public <C> Configured<C, E> configured(C config) {
        return configured(() -> config);
    }

    @SuppressWarnings("unchecked")
    public void register(IForgeRegistry<?> genericRegistry) {
        for(Consumer<RegistryHandler<E>> listener : listeners) {
            listener.accept(this);
        }

        IForgeRegistry<E> registry = (IForgeRegistry<E>) genericRegistry;
        for(Entry entry : entries.values()) {
            registry.register(entry.entry);
        }
    }

    @SuppressWarnings("unchecked")
    public void handleMissingMappings(RegistryEvent.MissingMappings<?> genericEvent) {
        RegistryEvent.MissingMappings<E> event = (RegistryEvent.MissingMappings<E>) genericEvent;

        for(RegistryEvent.MissingMappings.Mapping<E> mapping : event.getAllMappings()) {
            if(entries.containsKey(mapping.key)) {
                mapping.remap(entries.get(mapping.key).entry);
            }
        }
    }

    public static class Configured<C, E extends IForgeRegistryEntry<E>> {
        private final RegistryHandler<E> handler;
        private final Supplier<C> config;

        private Configured(RegistryHandler<E> handler, Supplier<C> config) {
            this.handler = handler;
            this.config = config;
        }

        /**
         * Adds a registry object with config.
         *
         * @param id      The ID to register the object under.
         * @param factory The object factory. Called with the created config to build the object.
         * @param aliases Alias names to remap when missing mappings are detected.
         * @return This instance for chaining convenience.
         */
        public Configured<C, E> add(String id, Function<C, E> factory, String... aliases) {
            handler.add(id, factory.apply(config.get()), aliases);
            return this;
        }

        /**
         * Adds multiple registry objects with config and the same factory.
         *
         * @param factory The object factory. Called with a created config to build each object.
         * @param names   The IDs to register the objects under.
         * @return This instance for chaining convenience.
         */
        public Configured<C, E> addAll(BiFunction<ResourceLocation, C, E> factory, String... names) {
            for(String name : names) {
                handler.add(name, factory.apply(handler.createID(name), config.get()));
            }
            return this;
        }

        /**
         * Adds multiple registry objects with config and the same factory.
         *
         * @param factory The object factory. Called with a created config to build each object.
         * @param names   The IDs to register the objects under.
         * @return This instance for chaining convenience.
         */
        public Configured<C, E> addAll(Function<C, E> factory, String... names) {
            for(String name : names) {
                handler.add(name, factory.apply(config.get()));
            }
            return this;
        }
    }

    private class Entry {
        private final ResourceLocation id;
        private final E entry;
        private final String[] aliases;

        private Entry(ResourceLocation id, E entry, String[] aliases) {
            this.id = id;
            this.entry = entry;
            this.aliases = aliases;
        }
    }


}
