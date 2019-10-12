package modernity.common;

import modernity.MDInfo;
import modernity.common.command.MDCommands;
import modernity.common.handler.CaveHandler;
import modernity.common.handler.EntitySwimHandler;
import modernity.common.loot.MDLootTables;
import modernity.common.net.MDPackets;
import modernity.common.world.dimen.MDDimensions;
import modernity.common.world.gen.structure.MDStructurePieceTypes;
import modernity.network.PacketChannel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public void preInit() {

    }

    public void init() {
        MDPackets.register( networkChannel );
        networkChannel.lock();
        MDStructurePieceTypes.registerPieces();
        MDLootTables.register();
    }

    public void postInit() {

    }

    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.restore( event.getMissingNames() );
        MDDimensions.init();
    }

    public void registerListeners() {
        FORGE_EVENT_BUS.register( EntitySwimHandler.INSTANCE );
        FORGE_EVENT_BUS.register( new CaveHandler() );
    }

    public ThreadTaskExecutor<? extends Runnable> getThreadExecutor( LogicalSide side ) {
        if( side == LogicalSide.CLIENT ) return getClientThreadExecutor();
        else return getServerThreadExecutor();
    }

    protected ThreadTaskExecutor<Runnable> getClientThreadExecutor() {
        return null;
    }

    protected ThreadTaskExecutor<TickDelayedTask> getServerThreadExecutor() {
        if( server == null ) return null;
        return server;
    }

    public PacketChannel getNetworkChannel() {
        return networkChannel;
    }

    public abstract LogicalSide side();

    @SubscribeEvent
    public void preServerStart( FMLServerAboutToStartEvent event ) {
        server = event.getServer();
    }

    @SubscribeEvent
    public void serverStart( FMLServerStartingEvent event ) {
        MDCommands.register( event.getCommandDispatcher() );
    }

    @SubscribeEvent
    public void serverStop( FMLServerStoppedEvent event ) {
        server = null;
    }

    public static PacketChannel network() {
        return get().getNetworkChannel();
    }

    public static Modernity get() {
        return instance;
    }

    public static ResourceLocation res( String id ) {
        return new ResourceLocation( MDInfo.MODID + ":" + id );
    }
}
