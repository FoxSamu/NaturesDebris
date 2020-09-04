package natures.debris.client;

import net.minecraftforge.common.MinecraftForge;

import net.minecraft.client.Minecraft;

import natures.debris.api.INaturesDebris;
import natures.debris.api.INaturesDebrisClient;
import natures.debris.api.util.ISidedTickable;
import natures.debris.common.NaturesDebris;
import natures.debris.common.block.NdBlocks;
import natures.debris.client.handler.BiomeColorCacheHandler;
import natures.debris.client.util.BiomeColorCache;
import natures.debris.NdInfo;
import natures.debris.data.NaturesDebrisData;

public class NaturesDebrisClient extends NaturesDebris implements INaturesDebrisClient {
    protected final Minecraft minecraft = Minecraft.getInstance();

    private final BiomeColorCache grassColorCache = new BiomeColorCache();

    @Override
    public void setup() {
        NdBlocks.setupClient();
    }

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
