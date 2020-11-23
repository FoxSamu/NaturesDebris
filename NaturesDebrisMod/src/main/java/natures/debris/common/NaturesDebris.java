package natures.debris.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.util.ResourceLocation;

import natures.debris.api.util.ISidedTickable;
import natures.debris.core.NaturesDebrisCore;
import natures.debris.common.block.NdBlocks;
import natures.debris.common.dev.IDevManager;
import natures.debris.common.dev.SilentDevManager;
import natures.debris.common.handler.CommandRegistryHandler;
import natures.debris.common.handler.PlantHandler;
import natures.debris.common.handler.RegistryHandler;
import natures.debris.Bootstrap;
import natures.debris.NdInfo;

public class NaturesDebris extends NaturesDebrisCore {
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    public static final IEventBus FORGE_EVENT_BUS = MinecraftForge.EVENT_BUS;

    public void construct() {
        registerEventListeners();
    }

    public void setup() {
        NdBlocks.setup();
    }

    public void complete() {

    }

    protected void registerEventListeners() {
        MOD_EVENT_BUS.register(new RegistryHandler());
        FORGE_EVENT_BUS.register(new CommandRegistryHandler());
        CORE_EVENT_BUS.register(new PlantHandler());
    }

    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        sidedTickable.serverTick();
    }

    public IDevManager getDevManager() {
        return SilentDevManager.INSTANCE;
    }

    public static NaturesDebris get() {
        return Bootstrap.INSTANCE;
    }

    /**
     * Create a {@link ResourceLocation} with {@link NdInfo#ID ndebris} as default namespace.
     * <ul>
     * <li>{@code "minecraft:path"} will yield {@code minecraft:path}</li>
     * <li>{@code "ndebris:path"} will yield {@code ndebris:path}</li>
     * <li>{@code "path"} will yield {@code ndebris:path} (unlike the {@link ResourceLocation#ResourceLocation(String) ResourceLocation} constructor will yield {@code minecraft:path})</li>
     * </ul>
     *
     * @param path The resource path.
     * @return The created {@link ResourceLocation} instance.
     */
    public static ResourceLocation resLoc(String path) {
        int colon = path.indexOf(':');
        if (colon >= 0) {
            return new ResourceLocation(path.substring(0, colon), path.substring(colon + 1));
        }
        return new ResourceLocation(NdInfo.ID, path);
    }

    /**
     * Create a stringified {@link ResourceLocation} with {@link NdInfo#ID ndebris} as default namespace. See {@link
     * #resLoc}.
     *
     * @param path The resource path.
     * @return The created resource id.
     */
    public static String resStr(String path) {
        if (path.indexOf(':') >= 0) {
            return path;
        }
        return NdInfo.ID + ":" + path;
    }
}
