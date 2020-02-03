/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client;

import modernity.api.dimension.IClientTickingDimension;
import modernity.client.colormap.ColorMap;
import modernity.client.colors.ColorProfile;
import modernity.client.colors.ColorProfileManager;
import modernity.client.colors.ColorProviderRegistry;
import modernity.client.handler.FogHandler;
import modernity.client.handler.ParticleRegistryHandler;
import modernity.client.handler.TextureStitchHandler;
import modernity.client.handler.WorldRenderHandler;
import modernity.client.model.MDModelLoaders;
import modernity.client.render.area.AreaRenderManager;
import modernity.client.render.block.CustomFluidRenderer;
import modernity.client.render.environment.SurfaceCloudRenderer;
import modernity.client.render.environment.SurfaceSkyRenderer;
import modernity.client.render.environment.SurfaceWeatherRenderer;
import modernity.client.shaders.ShaderManager;
import modernity.client.util.MDRecipeBookCategories;
import modernity.common.Modernity;
import modernity.common.area.MDAreas;
import modernity.common.area.core.ClientWorldAreaManager;
import modernity.common.area.core.IWorldAreaManager;
import modernity.common.block.MDBlocks;
import modernity.common.container.MDContainerTypes;
import modernity.common.entity.MDEntityTypes;
import modernity.common.item.MDItems;
import modernity.common.net.SSeedPacket;
import modernity.common.tileentity.MDTileEntitiyTypes;
import modernity.common.util.ISidedTickable;
import modernity.common.world.dimen.MDSurfaceDimension;
import modul.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.world.World;
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
    public static final ModuleType<Modernity, ShaderManager> SHADER_MANAGER = new ModuleType<>( ShaderManager::new, "Modernity:ShaderManager" );

    /** The {@link Minecraft} instance. */
    public final Minecraft mc = Minecraft.getInstance();

    private ColorProfileManager colorProfileManager;

    private ColorProfile grassColors;
    private ColorProfile blackwoodColors;
    private ColorProfile inverColors;
    private ColorProfile waterColors;
    private ColorProfile mossColors;
    private ColorProfile fernColors;
    private ColorProfile watergrassColors;
    private ColorProfile algaeColors;

    private ClientWorldAreaManager worldAreaManager;
    private AreaRenderManager areaRenderManager;

    // Used to give color to fallen leaf particles
    private final ColorMap fallenLeafColors = new ColorMap( new ResourceLocation( "modernity:textures/block/murky_podzol_top.png" ), 0xffffff );

    private final CustomFluidRenderer fluidRenderer = new CustomFluidRenderer();

    // Last world seed, which is sent by server worlds when the player joins.
    private long lastWorldSeed;

    @Override
    public void preInit() {
        super.preInit();

        colorProfileManager = new ColorProfileManager( Minecraft.getInstance().getResourceManager(), manager -> {
            grassColors = manager.load( new ResourceLocation( "modernity:grass" ) );
            blackwoodColors = manager.load( new ResourceLocation( "modernity:blackwood" ) );
            inverColors = manager.load( new ResourceLocation( "modernity:inver" ) );
            waterColors = manager.load( new ResourceLocation( "modernity:water" ) );
            mossColors = manager.load( new ResourceLocation( "modernity:moss" ) );
            fernColors = manager.load( new ResourceLocation( "modernity:ferns" ) );
            watergrassColors = manager.load( new ResourceLocation( "modernity:watergrass" ) );
            algaeColors = manager.load( new ResourceLocation( "modernity:algae" ) );
        } );

        addFutureReloadListener( fluidRenderer );
        addFutureReloadListener( fallenLeafColors );
        addFutureReloadListener( colorProfileManager );

        areaRenderManager = new AreaRenderManager();
        MDAreas.setupClient( areaRenderManager );
        ColorProviderRegistry.setup();
    }

    @Override
    public void init() {
        super.init();
        MDEntityTypes.initEntityRenderers();
        MDContainerTypes.registerScreens();

        MDRecipeBookCategories.init();
        MDModelLoaders.register();
    }

    @Override
    public void postInit() {
        super.postInit();
        MDBlocks.initBlockColors();
        MDItems.initItemColors();
        MDTileEntitiyTypes.setupClient();
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MOD_EVENT_BUS.register( TextureStitchHandler.INSTANCE );
        MOD_EVENT_BUS.register( ParticleRegistryHandler.INSTANCE );
        FORGE_EVENT_BUS.register( FogHandler.INSTANCE );
        FORGE_EVENT_BUS.register( WorldRenderHandler.INSTANCE );
    }

    @Override
    public void callSidedTick( ISidedTickable tickable ) {
        tickable.serverTick();
        tickable.clientTick();
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
                dimen.setWeatherRenderer( new SurfaceWeatherRenderer() );
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
    public ColorProfile getGrassColors() {
        return grassColors;
    }

    /**
     * Gets the biome color profile for blackwood colors
     */
    public ColorProfile getBlackwoodColors() {
        return blackwoodColors;
    }

    /**
     * Gets the biome color profile for inver colors
     */
    public ColorProfile getInverColors() {
        return inverColors;
    }

    /**
     * Gets the biome color profile for water colors
     */
    public ColorProfile getWaterColors() {
        return waterColors;
    }

    /**
     * Gets the biome color profile for moss colors
     */
    public ColorProfile getMossColors() {
        return mossColors;
    }

    /**
     * Gets the biome color profile for algae colors
     */
    public ColorProfile getAlgaeColors() {
        return algaeColors;
    }

    /**
     * Gets the biome color profile for watergrass colors
     */
    public ColorProfile getWatergrassColors() {
        return watergrassColors;
    }

    /**
     * Gets the biome color profile for fern colors
     */
    public ColorProfile getFernColors() {
        return fernColors;
    }

    public ColorProfileManager getColorProfileManager() {
        return colorProfileManager;
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
    public ColorMap getFallenLeafColors() {
        return fallenLeafColors;
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

    @Override
    public IWorldAreaManager getWorldAreaManager( World world ) {
        if( world == null ) {
            return getWorldAreaManager();
        }
        if( world instanceof ClientWorld ) {
            if( world == mc.world ) {
                return getWorldAreaManager();
            }
            return null;
        }
        return super.getWorldAreaManager( world );
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
