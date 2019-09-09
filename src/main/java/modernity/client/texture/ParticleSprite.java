/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.texture;

import modernity.api.util.NoEditList;
import modernity.client.util.ProxyClient;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public final class ParticleSprite {
    private static final ArrayList<ParticleSprite> SPRITES = new ArrayList<>();
    public static final NoEditList<ParticleSprite> SPRITE_LIST = new NoEditList<>( SPRITES );

    public static final ParticleSprite SALT = new ParticleSprite( "salt" );
    public static final ParticleSprite LEAF_1 = new ParticleSprite( "leaf1" );
    public static final ParticleSprite LEAF_2 = new ParticleSprite( "leaf2" );
    public static final ParticleSprite LEAF_3 = new ParticleSprite( "leaf3" );
    public static final ParticleSprite LEAF_4 = new ParticleSprite( "leaf4" );
    public static final ParticleSprite LEAF_5 = new ParticleSprite( "leaf5" );
    public static final ParticleSprite LEAF_6 = new ParticleSprite( "leaf6" );
    public static final ParticleSprite LEAF_7 = new ParticleSprite( "leaf7" );
    public static final ParticleSprite LEAF_8 = new ParticleSprite( "leaf8" );
    public static final ParticleSprite LEAF_9 = new ParticleSprite( "leaf9" );
    public static final ParticleSprite LEAF_10 = new ParticleSprite( "leaf10" );
    public static final ParticleSprite LEAF_11 = new ParticleSprite( "leaf11" );
    public static final ParticleSprite LEAF_12 = new ParticleSprite( "leaf12" );
    public static final ParticleSprite LEAF_13 = new ParticleSprite( "leaf13" );
    public static final ParticleSprite LEAF_14 = new ParticleSprite( "leaf14" );
    public static final ParticleSprite LEAF_15 = new ParticleSprite( "leaf15" );
    public static final ParticleSprite LEAF_16 = new ParticleSprite( "leaf16" );

    private final ResourceLocation textureID;
    private TextureAtlasSprite sprite;

    public ParticleSprite( ResourceLocation textureID ) {
        this.textureID = textureID;
        SPRITES.add( this );
    }

    private ParticleSprite( String id ) {
        this( new ResourceLocation( "modernity:" + id ) );
    }

    public ResourceLocation getID() {
        return textureID;
    }

    public TextureAtlasSprite getSprite() {
        if( sprite == null ) reloadSprite();
        return sprite;
    }

    public void reloadSprite() {
        sprite = ProxyClient.get().getParticleTextureMap().getSprite( getID() );
    }
}
