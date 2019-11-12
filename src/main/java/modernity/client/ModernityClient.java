package modernity.client;

import modernity.api.biome.BiomeColoringProfile;
import modernity.api.dimension.IClientTickingDimension;
import modernity.client.colormap.ColorMap;
import modernity.client.handler.FogHandler;
import modernity.client.handler.ParticleRegistryHandler;
import modernity.client.handler.WorldRenderHandler;
import modernity.client.handler.TextureStitchHandler;
import modernity.client.reloader.BiomeColorProfileReloader;
import modernity.client.render.area.AreaRenderManager;
import modernity.client.render.block.CustomFluidRenderer;
import modernity.client.render.environment.SurfaceCloudRenderer;
import modernity.client.render.environment.SurfaceSkyRenderer;
import modernity.common.Modernity;
import modernity.common.area.MDAreas;
import modernity.common.area.core.ClientWorldAreaManager;
import modernity.common.block.MDBlocks;
import modernity.common.container.MDContainerTypes;
import modernity.common.entity.MDEntityTypes;
import modernity.common.item.MDItems;
import modernity.common.net.SSeedPacket;
import modernity.common.world.dimen.MDSurfaceDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

/**
 * The modernity's client proxy. This class is intantiated during bootstrap when we're running on Minecraft Client (the
 * most usual case). This class inherits modernity's default proxy {@link Modernity} and adds extra behaviour and
 * loading for the client side only.
 */
public class ModernityClient extends Modernity {
    /** The {@link Minecraft} instance. */
    public final Minecraft mc = Minecraft.getInstance();

    private BiomeColoringProfile grassColors;
    private BiomeColoringProfile blackwoodColors;
    private BiomeColoringProfile inverColors;
    private BiomeColoringProfile waterColors;

    private ClientWorldAreaManager worldAreaManager;
    private AreaRenderManager areaRenderManager;

    // Used to give color to humus particles
    private final ColorMap humusColors = new ColorMap( new ResourceLocation( "modernity:textures/block/humus_top.png" ), 0xffffff );

    private final CustomFluidRenderer fluidRenderer = new CustomFluidRenderer();

    // Last world seed, which is sent by server worlds when the player joins.
    private long lastWorldSeed;

    @Override
    public void preInit() {
        super.preInit();
        addFutureReloadListener( new BiomeColorProfileReloader( "grass", e -> grassColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "blackwood", e -> blackwoodColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "inver", e -> inverColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "water", e -> waterColors = e ) );

        addFutureReloadListener( fluidRenderer );
        addFutureReloadListener( humusColors );

        areaRenderManager = new AreaRenderManager();
        MDAreas.setupClient( areaRenderManager );
    }

    @Override
    public void init() {
        super.init();
        MDEntityTypes.initEntityRenderers();
        MDContainerTypes.registerScreens();
    }

    @Override
    public void postInit() {
        super.postInit();
        MDBlocks.initBlockColors();
        MDItems.initItemColors();
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MOD_EVENT_BUS.register( TextureStitchHandler.INSTANCE );
        MOD_EVENT_BUS.register( ParticleRegistryHandler.INSTANCE );
        FORGE_EVENT_BUS.register( FogHandler.INSTANCE );
        FORGE_EVENT_BUS.register( WorldRenderHandler.INSTANCE );
    }

    /**
     * Adds a reload listener to Minecraft's resource manager when it's reloadable
     */
    public void addFutureReloadListener( IFutureReloadListener listener ) {
        IResourceManager manager = mc.getResourceManager();
        if( manager instanceof IReloadableResourceManager ) {
            ( (IReloadableResourceManager) manager ).addReloadListener( listener );
        }
    }

    @Override
    protected ThreadTaskExecutor<Runnable> getClientThreadExecutor() {
        return mc;
    }

    @Override
    public LogicalSide side() {
        return LogicalSide.CLIENT;
    }

    @SubscribeEvent
    public void onWorldLoad( WorldEvent.Load event ) {
        if( event.getWorld().isRemote() ) {
            Dimension dimen = event.getWorld().getDimension();
            if( dimen instanceof MDSurfaceDimension ) {
                dimen.setSkyRenderer( new SurfaceSkyRenderer( lastWorldSeed ) );
                dimen.setCloudRenderer( new SurfaceCloudRenderer() );
            }
        }
    }

    @SubscribeEvent
    public void onTick( TickEvent.ClientTickEvent event ) {
        if( event.phase == TickEvent.Phase.END ) {
            if( mc.world != null && ! mc.isGamePaused() ) {
                Dimension dimen = mc.world.dimension;
                if( dimen instanceof IClientTickingDimension ) {
                    ( (IClientTickingDimension) dimen ).tickClient();
                }

                getWorldAreaManager().tick();
            }
        }
    }

    /**
     * Gets the biome color profile for grass colors
     */
    public BiomeColoringProfile getGrassColors() {
        return grassColors;
    }

    /**
     * Gets the biome color profile for blackwood colors
     */
    public BiomeColoringProfile getBlackwoodColors() {
        return blackwoodColors;
    }

    /**
     * Gets the biome color profile for inver colors
     */
    public BiomeColoringProfile getInverColors() {
        return inverColors;
    }

    /**
     * Gets the biome color profile for water colors
     */
    public BiomeColoringProfile getWaterColors() {
        return waterColors;
    }

    /**
     * Gets the Modernity fluid render
     */
    public CustomFluidRenderer getFluidRenderer() {
        return fluidRenderer;
    }

    /**
     * Returns the Humus color map, used to give color to humus particles
     */
    public ColorMap getHumusColors() {
        return humusColors;
    }

    public ClientWorldAreaManager getWorldAreaManager() {
        if( worldAreaManager == null ) {
            if( mc.world != null ) {
                return worldAreaManager = new ClientWorldAreaManager( mc.world );
            }
        }
        if( worldAreaManager != null ) {
            if( mc.world == null ) {
                return worldAreaManager = null;
            } else {
                if( mc.world != worldAreaManager.getWorld() ) {
                    worldAreaManager = new ClientWorldAreaManager( mc.world );
                }
            }
        }
        return worldAreaManager;
    }

    public AreaRenderManager getAreaRenderManager() {
        return areaRenderManager;
    }

    /**
     * Returns the seed of the last joined world, or 0 if no world has sent a seed yet.
     */
    public long getLastWorldSeed() {
        return lastWorldSeed;
    }

    /**
     * Sets the seed of the last joined world. Used in {@link SSeedPacket} to set the received seed.
     */
    public void setLastWorldSeed( long lastWorldSeed ) {
        this.lastWorldSeed = lastWorldSeed;
    }

    /**
     * Gets the {@link ModernityClient} instance we're using now, or null if not yet initialized.
     */
    public static ModernityClient get() {
        return (ModernityClient) Modernity.get();
    }
}
