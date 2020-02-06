/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 06 - 2020
 * Author: rgsw
 */

package modernity.common;

import modernity.MDInfo;
import modernity.MDModules;
import modernity.ModernityBootstrap;
import modernity.api.dimension.IInitializeDimension;
import modernity.common.area.core.IWorldAreaManager;
import modernity.common.area.core.ServerWorldAreaManager;
import modernity.common.capability.MDCapabilities;
import modernity.common.command.MDCommands;
import modernity.common.generator.structure.MDStructurePieceTypes;
import modernity.common.generator.structure.MDStructures;
import modernity.common.handler.*;
import modernity.common.loot.MDLootTables;
import modernity.common.net.MDPackets;
import modernity.common.util.ISidedTickable;
import modernity.common.world.dimen.MDDimensions;
import modernity.network.PacketChannel;
import modul.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * <p>
 * Base proxy of the Modernity, that handles all common (both client and server) loading, listens to the main events,
 * and holds all objects neede during the game.
 * </p>
 * <p>
 * <ul>
 * <li>Mod ID: {@code modernity}</li>
 * </ul>
 * </p>
 *
 * @author RGSW
 * @see ModernityBootstrap
 */
public abstract class Modernity {
    public static final Logger LOGGER = LogManager.getLogger( "Modernity" );

    private static Modernity instance;

    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    public static final IEventBus FORGE_EVENT_BUS = MinecraftForge.EVENT_BUS;

    private MinecraftServer server;

    private final HashMap<DimensionType, ServerWorldAreaManager> areaManagers = new HashMap<>();

    private final PacketChannel networkChannel = new PacketChannel( new ResourceLocation( "modernity:connection" ), 0 );

    private final ModuleManager<Modernity> modules = new ModuleManager<>( this, MDModules.MODERNITY );

    public Modernity() {
        if( instance != null ) {
            throw new IllegalStateException( "Modernity is already initialized" );
        }
        instance = this;
    }

    /**
     * Called after creating the {@link Modernity} instance and after {@link #registerListeners()} to initalize things
     * that need to be initialized as early as possible.
     */
    public void preInit() {
        modules.init();
    }

    /**
     * Called when the Modernity receives {@link FMLCommonSetupEvent}, to register things to vanilla instances (such as
     * loot tables to the loot table manager).
     */
    public void init() {
        MDPackets.register( networkChannel );
        networkChannel.lock();
        MDStructurePieceTypes.registerPieces();
        MDLootTables.register();
        MDCapabilities.register();

        Biomes.DARK_FOREST.addStructure( MDStructures.FOREST_RUNES, IFeatureConfig.NO_FEATURE_CONFIG );
        Biomes.DARK_FOREST_HILLS.addStructure( MDStructures.FOREST_RUNES, IFeatureConfig.NO_FEATURE_CONFIG );

        Biome[] biomes = {
            Biomes.OCEAN,
            Biomes.PLAINS,
            Biomes.DESERT,
            Biomes.MOUNTAINS,
            Biomes.FOREST,
            Biomes.TAIGA,
            Biomes.SWAMP,
            Biomes.RIVER,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.SNOWY_TUNDRA,
            Biomes.SNOWY_MOUNTAINS,
            Biomes.MUSHROOM_FIELDS,
            Biomes.MUSHROOM_FIELD_SHORE,
            Biomes.BEACH,
            Biomes.DESERT_HILLS,
            Biomes.WOODED_HILLS,
            Biomes.TAIGA_HILLS,
            Biomes.MOUNTAIN_EDGE,
            Biomes.JUNGLE,
            Biomes.JUNGLE_HILLS,
            Biomes.JUNGLE_EDGE,
            Biomes.DEEP_OCEAN,
            Biomes.STONE_SHORE,
            Biomes.SNOWY_BEACH,
            Biomes.BIRCH_FOREST,
            Biomes.BIRCH_FOREST_HILLS,
            Biomes.DARK_FOREST,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TAIGA_HILLS,
            Biomes.GIANT_TREE_TAIGA,
            Biomes.GIANT_TREE_TAIGA_HILLS,
            Biomes.WOODED_MOUNTAINS,
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU,
            Biomes.BADLANDS,
            Biomes.WOODED_BADLANDS_PLATEAU,
            Biomes.BADLANDS_PLATEAU,
            Biomes.WARM_OCEAN,
            Biomes.LUKEWARM_OCEAN,
            Biomes.COLD_OCEAN,
            Biomes.DEEP_WARM_OCEAN,
            Biomes.DEEP_LUKEWARM_OCEAN,
            Biomes.DEEP_COLD_OCEAN,
            Biomes.DEEP_FROZEN_OCEAN,
            Biomes.SUNFLOWER_PLAINS,
            Biomes.DESERT_LAKES,
            Biomes.GRAVELLY_MOUNTAINS,
            Biomes.FLOWER_FOREST,
            Biomes.TAIGA_MOUNTAINS,
            Biomes.SWAMP_HILLS,
            Biomes.ICE_SPIKES,
            Biomes.MODIFIED_JUNGLE,
            Biomes.MODIFIED_JUNGLE_EDGE,
            Biomes.TALL_BIRCH_FOREST,
            Biomes.TALL_BIRCH_HILLS,
            Biomes.DARK_FOREST_HILLS,
            Biomes.SNOWY_TAIGA_MOUNTAINS,
            Biomes.GIANT_SPRUCE_TAIGA,
            Biomes.GIANT_SPRUCE_TAIGA_HILLS,
            Biomes.MODIFIED_GRAVELLY_MOUNTAINS,
            Biomes.SHATTERED_SAVANNA,
            Biomes.SHATTERED_SAVANNA_PLATEAU,
            Biomes.ERODED_BADLANDS,
            Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,
            Biomes.MODIFIED_BADLANDS_PLATEAU
        };
        for( Biome biome : biomes ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature( MDStructures.FOREST_RUNES, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
    }

    /**
     * Called when the Modernity recives {@link FMLLoadCompleteEvent}, to do some post-initialization.
     */
    public void postInit() {

    }

    public void callSidedTick( ISidedTickable tickable ) {
        tickable.serverTick();
    }

    /**
     * Handler for registering dimensions (handles {@link RegisterDimensionsEvent}).
     */
    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.restore( event.getMissingNames() );
        MDDimensions.init();
    }

    /**
     * Called directly after instantiation to register any necessary event listeners.
     */
    public void registerListeners() {
        FORGE_EVENT_BUS.register( EntitySwimHandler.INSTANCE );
        FORGE_EVENT_BUS.register( CaveHandler.INSTANCE );
        FORGE_EVENT_BUS.register( CapabilityHandler.INSTANCE );
        FORGE_EVENT_BUS.register( WorldAreaHandler.INSTANCE );
        FORGE_EVENT_BUS.register( FuelHandler.INSTANCE );
        FORGE_EVENT_BUS.register( ContainerHandler.INSTANCE );
        FORGE_EVENT_BUS.register( LootTableHandler.INSTANCE );
    }

    /**
     * Returns the thread executor for the specified side. This calls {@link #getClientThreadExecutor()} when the
     * specified side is {@link LogicalSide#CLIENT}, {@link #getServerThreadExecutor()} otherwise.
     */
    public ThreadTaskExecutor<? extends Runnable> getThreadExecutor( LogicalSide side ) {
        if( side == LogicalSide.CLIENT ) return getClientThreadExecutor();
        else return getServerThreadExecutor();
    }

    /**
     * Returns the client thread executor, which is {@link Minecraft}, or null when {@link Minecraft} is not available
     * (server case).
     */
    protected ThreadTaskExecutor<Runnable> getClientThreadExecutor() {
        return null;
    }

    /**
     * Returnst the server thread executor, which is a {@link MinecraftServer} instance, or null when no {@link
     * MinecraftServer} is running (client case when no world is loaded).
     */
    protected ThreadTaskExecutor<TickDelayedTask> getServerThreadExecutor() {
        if( server == null ) return null;
        return server;
    }

    /**
     * Returns the networking channel for Modernity packets.
     */
    public PacketChannel getNetworkChannel() {
        return networkChannel;
    }

    public ServerWorldAreaManager getWorldAreaManager( ServerWorld world ) {
        if( server == null ) {
            if( ! areaManagers.isEmpty() ) {
                areaManagers.clear();
            }
            return null;
        } else {
            DimensionType type = world.getDimension().getType();
            ServerWorldAreaManager manager = areaManagers.get( type );
            if( manager == null || manager.getWorld() != world ) {
                areaManagers.put( type, manager = new ServerWorldAreaManager( world ) );
            }
            return manager;
        }
    }

    public IWorldAreaManager getWorldAreaManager( World world ) {
        if( ! ( world instanceof ServerWorld ) ) return null;
        return getWorldAreaManager( (ServerWorld) world );
    }

    public ModuleManager<Modernity> getModules() {
        return modules;
    }

    /**
     * Returns the {@link LogicalSide} of this proxy.
     */
    public abstract LogicalSide side();

    /**
     * Handler for {@link FMLServerAboutToStartEvent} to obtain the {@link MinecraftServer} instance.
     */
    @SubscribeEvent
    public void preServerStart( FMLServerAboutToStartEvent event ) {
        server = event.getServer();
    }

    /**
     * Handler for {@link FMLServerStartingEvent} to register {@linkplain MDCommands commands}.
     */
    @SubscribeEvent
    public void serverStart( FMLServerStartingEvent event ) {
        MDCommands.register( event.getCommandDispatcher() );
    }

    /**
     * Handler for {@link FMLServerStoppedEvent} event to remove any reference to a {@link MinecraftServer} instance.
     */
    @SubscribeEvent
    public void serverStop( FMLServerStoppedEvent event ) {
        areaManagers.clear();
        server = null;
    }

    @SubscribeEvent
    public void worldLoad( WorldEvent.Load event ) {
        Dimension dimen = event.getWorld().getDimension();
        if( dimen instanceof IInitializeDimension ) {
            ( (IInitializeDimension) dimen ).init();
        }
    }

    public static ModuleManager<Modernity> modules() {
        return get().getModules();
    }

    /**
     * Shortcut for <code>{@linkplain #get() get()}.{@linkplain #getNetworkChannel() getNetworkChannel()}</code>.
     *
     * @see #get()
     * @see #getNetworkChannel()
     */
    public static PacketChannel network() {
        return get().getNetworkChannel();
    }

    /**
     * Returns the only instance of the {@link Modernity} class.
     */
    public static Modernity get() {
        return instance;
    }

    /**
     * Creates a resource location with {@code modernity} as namespace
     *
     * @param id The ID for the location.
     * @return <code>modernity:...</code>
     */
    public static ResourceLocation res( String id ) {
        if( id != null && id.indexOf( ':' ) >= 0 ) return new ResourceLocation( id );
        return new ResourceLocation( MDInfo.MODID + ":" + id );
    }
}
