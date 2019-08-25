/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modernity.api.biome.BiomeColoringProfile;
import modernity.client.colormap.ColorMap;
import modernity.client.handler.OptionsButtonHandler;
import modernity.client.handler.TextureStitchHandler;
import modernity.client.handler.WorldListenerInjectionHandler;
import modernity.client.particle.DepthParticleList;
import modernity.client.particle.DripParticle;
import modernity.client.particle.NoDepthParticleList;
import modernity.client.particle.SaltParticle;
import modernity.client.render.block.CustomFluidRenderer;
import modernity.client.texture.ParticleSprite;
import modernity.common.block.MDBlocks;
import modernity.common.entity.MDEntityTypes;
import modernity.common.item.MDItems;
import modernity.common.particle.MDParticles;
import modernity.common.settings.*;
import modernity.common.util.ContainerManager;
import modernity.common.util.ProxyCommon;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;


@OnlyIn( Dist.CLIENT )
public class ProxyClient extends ProxyCommon implements ISelectiveResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation PARTICLE_MAP_LOCATION = new ResourceLocation( "modernity:particles" );

    public final Minecraft mc = Minecraft.getInstance();

    private TextureMap particleTextureMap;
    private CustomFluidRenderer fluidRenderer;

    private final ColorMap humusColorMap = new ColorMap( new ResourceLocation( "modernity:textures/block/humus_top.png" ) );

    private final DepthParticleList depthParticleList = new DepthParticleList( PARTICLE_MAP_LOCATION );
    private final NoDepthParticleList noDepthParticleList = new NoDepthParticleList( PARTICLE_MAP_LOCATION );

    private final ClientSettings clientSettings = new ClientSettings( ClientSettings.DEFAULT_LOCATION );
    private final DefaultServerSettings defaultServerSettings = new DefaultServerSettings( DefaultServerSettings.DEFAULT_LOCATION );
    private LocalServerSettings localServerSettings;
    private RemoteServerSettings remoteServerSettings;

    private BiomeColoringProfile grassColors;
    private BiomeColoringProfile blackwoodColors;
    private BiomeColoringProfile inverColors;
    private BiomeColoringProfile waterColors;

    @Override
    public void init() {
        super.init();
        addResourceManagerReloadListener( this );

        MDEntityTypes.registerClient();

        clientSettings.load( false );
        defaultServerSettings.load( false );
        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            clientSettings.save( false );
            defaultServerSettings.save( false );
        } ) );
    }

    @Override
    public void loadComplete() {
        super.loadComplete();

        fluidRenderer = new CustomFluidRenderer();

        addResourceManagerReloadListener( fluidRenderer );
        addResourceManagerReloadListener( humusColorMap );

        MDBlocks.registerClient( mc.getBlockColors(), mc.getItemColors() );
        MDItems.registerClient( mc.getItemColors() );

        mc.addScheduledTask( this::finishOnMainThread );

        mc.particles.registerFactory( MDParticles.SALT, new SaltParticle.Factory() );
        mc.particles.registerFactory( MDParticles.MODERNIZED_WATER_DRIP, new DripParticle.ModernizedWaterFactory() );
        mc.particles.registerFactory( MDParticles.PORTAL_DRIP, new DripParticle.PortalFactory() );
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
        if( resourcePredicate.test( VanillaResourceType.TEXTURES ) ) {
            if( particleTextureMap != null ) {
                reloadTextureMap();
            }
            grassColors = loadColorProfile( resourceManager, new ResourceLocation( "modernity:grass" ) );
            blackwoodColors = loadColorProfile( resourceManager, new ResourceLocation( "modernity:blackwood" ) );
            inverColors = loadColorProfile( resourceManager, new ResourceLocation( "modernity:inver" ) );
            waterColors = loadColorProfile( resourceManager, new ResourceLocation( "modernity:water" ) );
        }
    }

    private BiomeColoringProfile loadColorProfile( IResourceManager resManager, ResourceLocation type ) {
        ResourceLocation loc = new ResourceLocation( type.getNamespace(), "data/biomecolors/" + type.getPath() + ".json" );

        try {
            return BiomeColoringProfile.create( resManager, loc, type.toString() );
        } catch( IOException e ) {
            return new BiomeColoringProfile();
        }
    }

    public void finishOnMainThread() {
        // Texture map must be initialized on main thread (it uses OpenGL calls)...
        particleTextureMap = new TextureMap( "textures/particle" );
        reloadTextureMap();
    }

    private void reloadTextureMap() {
        particleTextureMap.clear();
        mc.textureManager.loadTickableTexture( PARTICLE_MAP_LOCATION, particleTextureMap );
        mc.textureManager.bindTexture( PARTICLE_MAP_LOCATION );
        particleTextureMap.setMipmapLevels( 0 );
        particleTextureMap.setBlurMipmapDirect( false, false );
        mc.textureManager.bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
        particleTextureMap.stitch( mc.getResourceManager(), collectParticleTextures() );

        for( ParticleSprite sprite : ParticleSprite.SPRITE_LIST ) {
            sprite.reloadSprite();
        }
    }

    private Set<ResourceLocation> collectParticleTextures() {
        HashSet<ResourceLocation> set = new HashSet<>();
        for( ParticleSprite sprite : ParticleSprite.SPRITE_LIST ) {
            LOGGER.info( "Stitching sprite {} on particle texture", sprite.getID() );
            set.add( sprite.getID() );
        }
        return set;
    }

    @Override
    public boolean fancyGraphics() {
        return mc.gameSettings.fancyGraphics;
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MinecraftForge.EVENT_BUS.register( new TextureStitchHandler() );
        MinecraftForge.EVENT_BUS.register( new WorldListenerInjectionHandler() );
        MinecraftForge.EVENT_BUS.register( new OptionsButtonHandler() );
    }

    @SuppressWarnings( "deprecation" )
    public void addResourceManagerReloadListener( IResourceManagerReloadListener listener ) {
        IResourceManager manager = mc.getResourceManager();
        if( manager instanceof IReloadableResourceManager ) {
            ( (IReloadableResourceManager) manager ).addReloadListener( listener );
        }
    }

    @Override
    public void openContainer( EntityPlayer player, IInventory inventory ) {
        if( player instanceof EntityPlayerSP ) {
            ContainerManager.openContainerSP( (EntityPlayerSP) player, inventory );
            return;
        }
        super.openContainer( player, inventory );
    }

    public Minecraft getMinecraft() {
        return mc;
    }

    public CustomFluidRenderer getFluidRenderer() {
        return fluidRenderer;
    }

    public TextureMap getParticleTextureMap() {
        return particleTextureMap;
    }

    public ColorMap getHumusColorMap() {
        return humusColorMap;
    }

    public DepthParticleList getDepthParticleList() {
        return depthParticleList;
    }

    public NoDepthParticleList getNoDepthParticleList() {
        return noDepthParticleList;
    }

    public DefaultServerSettings getDefaultServerSettings() {
        return defaultServerSettings;
    }

    public ClientSettings getClientSettings() {
        return clientSettings;
    }

    public BiomeColoringProfile getGrassColors() {
        return grassColors;
    }

    public BiomeColoringProfile getBlackwoodColors() {
        return blackwoodColors;
    }

    public BiomeColoringProfile getInverColors() {
        return inverColors;
    }

    public BiomeColoringProfile getWaterColors() {
        return waterColors;
    }

    @Override
    public ServerSettings getServerSettings() {
        return localServerSettings == null ? remoteServerSettings : localServerSettings;
    }

    @Override
    public void serverStart( FMLServerStartingEvent event ) {
        super.serverStart( event );
    }

    @Override
    public void serverAboutToStart( FMLServerAboutToStartEvent event ) {
        super.serverAboutToStart( event );
        MinecraftServer server = getServer();
        File settingsFile = server.getActiveAnvilConverter().getFile( server.getFolderName(), "modernity/server.properties" );
        localServerSettings = new LocalServerSettings( "Local Server Settings (" + server.getWorldName() + ")", settingsFile, getDefaultServerSettings() );
        localServerSettings.load( false );
    }

    @Override
    public void serverStop( FMLServerStoppedEvent event ) {
        super.serverStop( event );
        if( localServerSettings != null ) {
            localServerSettings.save( false );
        }
        localServerSettings = null;
    }

    public RemoteServerSettings getRemoteServerSettings() {
        return remoteServerSettings;
    }

    public RemoteServerSettings startRemoteServerSettings() {
        LOGGER.info( "Using remote server settings" );
        if( remoteServerSettings != null ) return remoteServerSettings;
        return remoteServerSettings = new RemoteServerSettings();
    }

    @SubscribeEvent
    public void clientTick( TickEvent.ClientTickEvent event ) {
        if( mc.world != null ) {
            depthParticleList.tick();
            noDepthParticleList.tick();
        }
    }

    public void renderParticles( Entity e, float partialTicks ) {
        if( mc.world != null ) {
            depthParticleList.render( e, partialTicks );
            noDepthParticleList.render( e, partialTicks );
        }
    }

    @SubscribeEvent
    public void onWorldLoad( WorldEvent.Load event ) {
        if( event.getWorld().isRemote() ) {
            depthParticleList.clear();
            noDepthParticleList.clear();
        }
    }

    public void onWorldUnload( WorldEvent.Unload event ) {
        if( event.getWorld().isRemote() ) {
            LOGGER.info( "Discarding remote server settings" );
            remoteServerSettings = null;
        }
    }

    @Override
    public IThreadListener getThreadListener() {
        return mc;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    public static ProxyClient get() {
        return (ProxyClient) ProxyCommon.get();
    }

    public static ClientSettings clientSettings() {
        return get().clientSettings;
    }
}
