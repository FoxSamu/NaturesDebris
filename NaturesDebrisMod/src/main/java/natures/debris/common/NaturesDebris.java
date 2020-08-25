package natures.debris.common;

import natures.debris.NdInfo;
import natures.debris.api.INaturesDebris;
import natures.debris.api.INaturesDebrisInfo;
import natures.debris.api.util.ISidedTickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NaturesDebris implements INaturesDebris {
    protected static final IEventBus FML_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();


    public void construct() {
        registerEventListeners();
    }

    public void setup() {
    }

    public void complete() {

    }

    public void setLoaded() {
    }

    protected void registerEventListeners() {

    }

    @Override
    public INaturesDebrisInfo info() {
        return NdInfo.INSTANCE;
    }

    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        sidedTickable.serverTick();
    }

    public static NaturesDebris get() {
        return (NaturesDebris) INaturesDebris.get();
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
