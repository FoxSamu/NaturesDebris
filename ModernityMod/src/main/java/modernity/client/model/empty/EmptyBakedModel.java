/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.empty;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EmptyBakedModel implements IDynamicBakedModel {
    private final TextureAtlasSprite particleTexture;

    public EmptyBakedModel( TextureAtlasSprite particleTexture ) {
        this.particleTexture = particleTexture;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData ) {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particleTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
