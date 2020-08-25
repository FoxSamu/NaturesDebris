/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.processor;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Consumer;

public enum BlockLayer implements Consumer<Block> {
    SOLID {
        @Override
        public void accept(Block block) {
            DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> {
                RenderTypeLookup.setRenderLayer(block, RenderType.getSolid());
                return block;
            });
        }
    },
    CUTOUT {
        @Override
        public void accept(Block block) {
            DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
                return block;
            });
        }
    },
    CUTOUT_MIPPED {
        @Override
        public void accept(Block block) {
            DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped());
                return block;
            });
        }
    },
    TRANSLUCENT {
        @Override
        public void accept(Block block) {
            DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> {
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent());
                return block;
            });
        }
    }
}
