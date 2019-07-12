/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.texmap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;

@OnlyIn( Dist.CLIENT )
public class ParticleTextureMap {
    public static final TextureMap TEXTURE_MAP = new TextureMap( "textures/particle" );
    public static final ResourceLocation TEX_MAP_LOCATION = new ResourceLocation( "modernity:particles" );

    public static TextureAtlasSprite salt;

    public static void init() {
        TEXTURE_MAP.clear();
        TEXTURE_MAP.setMipmapLevels( 0 );
        Minecraft.getInstance().textureManager.loadTickableTexture( TEX_MAP_LOCATION, TEXTURE_MAP );
        Minecraft.getInstance().textureManager.bindTexture( TEX_MAP_LOCATION );
        TEXTURE_MAP.setBlurMipmapDirect( false, false );
        Minecraft.getInstance().textureManager.bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
        TEXTURE_MAP.stitch( Minecraft.getInstance().getResourceManager(), collectParticleTextures() );

        salt = TEXTURE_MAP.getSprite( new ResourceLocation( "modernity:salt" ) );
    }

    public static Set<ResourceLocation> collectParticleTextures() {
        HashSet<ResourceLocation> set = new HashSet<>();
        set.add( new ResourceLocation( "modernity:salt" ) );
        return set;
    }
}
