/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.client.model.wrapping;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class WrappingModel implements IUnbakedModel {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ResourceLocation parent;
    private final ImmutableMap<String, String> custom;
    private final ImmutableMap<String, String> textures;
    private final Boolean gui3d;
    private final Boolean smooth;

    private IUnbakedModel parentModel;
    private boolean loadingTextures;

    public WrappingModel( ResourceLocation parent, Map<String, String> custom, Map<String, String> textures, Boolean gui3d, Boolean smooth ) {
        this.parent = parent;
        this.custom = ImmutableMap.copyOf( custom );
        this.textures = ImmutableMap.copyOf( textures );
        this.gui3d = gui3d;
        this.smooth = smooth;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.singleton( parent );
    }

    @Override
    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
        parentModel = modelGetter.apply( parent );

        parentModel = parentModel.retexture( textures );
        parentModel = parentModel.process( custom );
        if( smooth != null ) parentModel = parentModel.smoothLighting( smooth );
        if( gui3d != null ) parentModel = parentModel.gui3d( gui3d );

        if( loadingTextures ) {
            LOGGER.warn( "Can't load textures: circular parent reference!" );
            return Collections.emptyList();
        } else {
            loadingTextures = true;
            Collection<ResourceLocation> textures = parentModel.getTextures( modelGetter, missingTextureErrors );
            loadingTextures = false;
            return textures;
        }
    }

    @Nullable
    @Override
    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
        return parentModel != null
               ? parentModel.bake( bakery, spriteGetter, sprite, format )
               : null;
    }

    private Map<String, String> merge( Map<String, String> a, Map<String, String> b ) {
        HashMap<String, String> map = new HashMap<>( a );
        map.putAll( b );
        return map;
    }

    @Override
    public IUnbakedModel process( ImmutableMap<String, String> customData ) {
        return new WrappingModel(
            parent,
            merge( custom, customData ),
            textures,
            gui3d,
            smooth
        );
    }

    @Override
    public IUnbakedModel retexture( ImmutableMap<String, String> tex ) {
        return new WrappingModel(
            parent,
            custom,
            merge( textures, tex ),
            gui3d,
            smooth
        );
    }

    @Override
    public IUnbakedModel gui3d( boolean value ) {
        return new WrappingModel( parent, custom, textures, value, smooth );
    }

    @Override
    public IUnbakedModel smoothLighting( boolean value ) {
        return new WrappingModel( parent, custom, textures, gui3d, value );
    }
}
