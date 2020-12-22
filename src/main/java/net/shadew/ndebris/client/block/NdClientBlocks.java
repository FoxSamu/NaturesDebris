package net.shadew.ndebris.client.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

import net.shadew.ndebris.common.block.NdBlocks;

public class NdClientBlocks {
    public static void setupBlocksClient() {
        setupRenderLayers();
        setupBlockColors();
    }

    private static void setupRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            NdBlocks.MURKY_GRASS_BLOCK
        );
    }

    private static void setupBlockColors() {
        ColorProviderRegistry.BLOCK.register(
            // TODO Biome colors coming later
            (state, world, pos, index) -> 0x11783F,
            NdBlocks.MURKY_GRASS_BLOCK
        );

        ColorProviderRegistry.ITEM.register(
            (item, index) -> 0x11783F,
            NdBlocks.MURKY_GRASS_BLOCK
        );
    }
}
