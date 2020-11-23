package natures.debris.client;

import net.minecraft.client.Minecraft;

import natures.debris.api.INaturesDebrisClient;
import natures.debris.api.util.ISidedTickable;
import natures.debris.common.NaturesDebris;
import natures.debris.common.block.NdBlocks;
import natures.debris.client.handler.BiomeColorCacheHandler;
import natures.debris.client.util.BiomeColorCache;
import natures.debris.Bootstrap;
import natures.debris.NdInfo;
import natures.debris.data.NaturesDebrisData;
import natures.debris.dev.NaturesDebrisDev;

public class NaturesDebrisClient extends NaturesDebris implements INaturesDebrisClient {
    protected final Minecraft minecraft = Minecraft.getInstance();

    private final BiomeColorCache grassColorCache = new BiomeColorCache();

    @Override
    public void setup() {
        super.setup();
        NdBlocks.setupClient();
    }

    public BiomeColorCache getGrassColorCache() {
        return grassColorCache;
    }

    @Override
    protected void registerEventListeners() {
        super.registerEventListeners();
        FORGE_EVENT_BUS.register(new BiomeColorCacheHandler());
    }

    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        sidedTickable.serverTick();
        sidedTickable.clientTick();
    }

    public static NaturesDebrisClient make() {
        if (NdInfo.DATAGEN) {
            try {
                return (NaturesDebrisData) Class.forName("natures.debris.data.NaturesDebrisData").newInstance();
            } catch (Exception ignored) {
                Bootstrap.LOGGER.warn("Was not able to instantiate Data instance while requested");
            }
        }
        if (NdInfo.IDE) {
            try {
                return (NaturesDebrisDev) Class.forName("natures.debris.dev.NaturesDebrisDev").newInstance();
            } catch (Exception ignored) {
                Bootstrap.LOGGER.warn("Was not able to instantiate Dev instance while requested");
            }
        }
        return new NaturesDebrisClient();
    }

    public static NaturesDebrisClient get() {
        return (NaturesDebrisClient) NaturesDebris.get();
    }

}
