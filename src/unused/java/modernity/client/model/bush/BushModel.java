/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 30 - 2020
 * Author: rgsw
 */

package modernity.client.model.bushold;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.PerspectiveMapWrapper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class BushModel implements IUnbakedModel {
    private final ResourceLocation texture;

    public BushModel( ResourceLocation texture ) {
        this.texture = texture == null ? MissingTextureSprite.getLocation() : texture;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
        return Collections.singleton( texture );
    }

    @Nullable
    @Override
    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
        return new BakedBush(
            sprite.getState().apply( Optional.empty() ),
            PerspectiveMapWrapper.getTransforms( sprite.getState() ),
            format,
            spriteGetter.apply( texture )
        );
    }

    @Override
    public IUnbakedModel retexture( ImmutableMap<String, String> textures ) {
        return new BushModel(
            textures.containsKey( "texture" )
            ? ResourceLocation.tryCreate( textures.get( "texture" ) )
            : null
        );
    }
}
