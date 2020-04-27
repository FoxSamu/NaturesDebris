/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.farmland;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class FarmlandModelGeometry implements IModelGeometry<FarmlandModelGeometry> {
    private final float y;
    private final int tint;
    private final int uvrot;

    private Material particles;
    private Material center;
    private Material all;
    private Material cross;
    private Material horizontal;
    private Material vertical;

    public FarmlandModelGeometry( float y, int tint, int uvAngle ) {
        this.y = y;
        this.tint = tint;
        this.uvrot = computeUVRot( uvAngle );
    }

    private static int computeUVRot( int angle ) {
        if( angle == 0 ) return 0;
        if( angle == 90 ) return 1;
        if( angle == 180 ) return 2;
        if( angle == 270 ) return 3;
        throw new RuntimeException( "'uvrot' must be 0, 90, 180, or 270" );
    }

    @Override
    public IBakedModel bake( IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation ) {
        return new FarmlandBakedModel(
            owner, overrides, y, tint, uvrot,
            spriteGetter.apply( center ),
            spriteGetter.apply( all ),
            spriteGetter.apply( cross ),
            spriteGetter.apply( horizontal ),
            spriteGetter.apply( vertical ),
            spriteGetter.apply( particles )
        );
    }

    @Override
    public Collection<Material> getTextures( IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors ) {
        center = owner.resolveTexture( "center" );
        all = owner.resolveTexture( "all" );
        cross = owner.resolveTexture( "cross" );
        horizontal = owner.resolveTexture( "horizontal" );
        vertical = owner.resolveTexture( "vertical" );
        particles = owner.resolveTexture( "particles" );
        if( particles.getAtlasLocation().equals( MissingTextureSprite.getLocation() ) ) {
            particles = center;
        }
        return ImmutableSet.of( center, all, cross, horizontal, vertical, particles );
    }
}
