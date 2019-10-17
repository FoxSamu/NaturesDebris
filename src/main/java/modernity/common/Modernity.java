package modernity.common;

import modernity.MDInfo;
import modernity.ModernityBootstrap;
import modernity.api.dimension.IInitializeDimension;
import modernity.common.command.MDCommands;
import modernity.common.handler.CaveHandler;
import modernity.common.handler.EntitySwimHandler;
import modernity.common.loot.MDLootTables;
import modernity.common.net.MDPackets;
import modernity.common.world.dimen.MDDimensions;
import modernity.common.world.gen.structure.MDStructurePieceTypes;
import modernity.network.PacketChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.dimension.Dimension;
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

    private final PacketChannel networkChannel = new PacketChannel( new ResourceLocation( "modernity:connection" ), 0 );

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
    }

    /**
     * Called when the Modernity recives {@link FMLLoadCompleteEvent}, to do some post-initialization.
     */
    public void postInit() {

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
        FORGE_EVENT_BUS.register( new CaveHandler() );
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
        server = null;
    }

    @SubscribeEvent
    public void worldLoad( WorldEvent .Load event ) {
        Dimension dimen = event.getWorld().getDimension();
        if( dimen instanceof IInitializeDimension ) {
            ( (IInitializeDimension) dimen ).init();
        }
    }

    /**
     * Shortcut for <code>{@linkplain #get() get}().{@linkplain #getNetworkChannel() getNetworkChannel}()</code>.
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
        return new ResourceLocation( MDInfo.MODID + ":" + id );
    }
}