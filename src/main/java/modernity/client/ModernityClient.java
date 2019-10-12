package modernity.client;

import modernity.api.biome.BiomeColoringProfile;
import modernity.client.colormap.ColorMap;
import modernity.client.handler.TextureStitchHandler;
import modernity.client.reloader.BiomeColorProfileReloader;
import modernity.client.render.block.CustomFluidRenderer;
import modernity.common.Modernity;
import modernity.common.block.MDBlocks;
import modernity.common.container.MDContainerTypes;
import modernity.common.entity.MDEntityTypes;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraftforge.fml.LogicalSide;

public class ModernityClient extends Modernity {
    public final Minecraft mc = Minecraft.getInstance();

    private BiomeColoringProfile grassColors;
    private BiomeColoringProfile blackwoodColors;
    private BiomeColoringProfile inverColors;
    private BiomeColoringProfile waterColors;

    private final ColorMap humusColors = new ColorMap( new ResourceLocation( "modernity:textures/block/humus_top.png" ), 0xffffff );

    private final CustomFluidRenderer fluidRenderer = new CustomFluidRenderer();

    // TODO: When network is implemented, this seed must be set on world load
    private long lastWorldSeed;

    @Override
    public void preInit() {
        super.preInit();
        addFutureReloadListener( new BiomeColorProfileReloader( "modernity:grass", e -> grassColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "modernity:blackwood", e -> blackwoodColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "modernity:inver", e -> inverColors = e ) );
        addFutureReloadListener( new BiomeColorProfileReloader( "modernity:water", e -> waterColors = e ) );

        addFutureReloadListener( fluidRenderer );
        addFutureReloadListener( humusColors );
    }

    @Override
    public void init() {
        super.init();
        MDEntityTypes.initEntityRenderers();
        MDContainerTypes.registerScreens();
        MDParticleTypes.setupFactories( Minecraft.getInstance().particles );
    }

    @Override
    public void postInit() {
        super.postInit();
        MDBlocks.initBlockColors();
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MOD_EVENT_BUS.register( TextureStitchHandler.INSTANCE );
    }

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

    public CustomFluidRenderer getFluidRenderer() {
        return fluidRenderer;
    }

    public ColorMap getHumusColors() {
        return humusColors;
    }

    public long getLastWorldSeed() {
        return lastWorldSeed;
    }

    public void setLastWorldSeed( long lastWorldSeed ) {
        this.lastWorldSeed = lastWorldSeed;
    }

    public static ModernityClient get() {
        return (ModernityClient) Modernity.get();
    }
}
