package natures.debris.client;

import natures.debris.NdInfo;
import natures.debris.api.INaturesDebris;
import natures.debris.api.INaturesDebrisClient;
import natures.debris.api.util.ISidedTickable;
import natures.debris.client.handler.BiomeColorCacheHandler;
import natures.debris.common.NaturesDebris;
import natures.debris.data.NaturesDebrisData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class NaturesDebrisClient extends NaturesDebris implements INaturesDebrisClient {
    protected final Minecraft minecraft = Minecraft.getInstance();

    @Override
    protected void registerEventListeners() {
        super.registerEventListeners();
        MinecraftForge.EVENT_BUS.register(new BiomeColorCacheHandler());
    }

    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        sidedTickable.serverTick();
        sidedTickable.clientTick();
    }

    public static NaturesDebrisClient get() {
        return (NaturesDebrisClient) INaturesDebris.get();
    }

    public static NaturesDebrisClient make() {
        if (NdInfo.DATAGEN) {
            return new NaturesDebrisData();
        } else {
            return new NaturesDebrisClient();
        }
    }
}
