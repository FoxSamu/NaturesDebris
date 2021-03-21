package net.shadew.ndebris.server;

import net.fabricmc.api.DedicatedServerModInitializer;

import net.shadew.ndebris.common.NaturesDebris;

public class NaturesDebrisServer extends NaturesDebris implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        onInitialize();
    }
}
