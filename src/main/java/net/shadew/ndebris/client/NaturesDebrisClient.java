package net.shadew.ndebris.client;

import net.fabricmc.api.ClientModInitializer;

import net.shadew.ndebris.client.block.NdClientBlocks;
import net.shadew.ndebris.common.NaturesDebris;

public class NaturesDebrisClient extends NaturesDebris implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        onInitialize();
        NdClientBlocks.setupBlocksClient();
    }
}
