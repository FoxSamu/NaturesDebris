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

}
