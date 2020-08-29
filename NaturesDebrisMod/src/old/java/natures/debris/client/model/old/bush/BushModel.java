/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.model.old.bush;


// TODO Re-evaluate
//public class BushModel implements IUnbakedModel {
//    private final ResourceLocation texture;
//
//    public BushModel( ResourceLocation texture ) {
//        this.texture = texture == null ? MissingTextureSprite.getLocation() : texture;
//    }
//
//    @Override
//    public Collection<ResourceLocation> getDependencies() {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
//        return Collections.singleton( texture );
//    }
//
//    @Nullable
//    @Override
//    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
//        return new BakedBush(
//            sprite.getState().apply( Optional.empty() ),
//            PerspectiveMapWrapper.getTransforms( sprite.getState() ),
//            format,
//            spriteGetter.apply( texture )
//        );
//    }
//
//    @Override
//    public IUnbakedModel retexture( ImmutableMap<String, String> textures ) {
//        return new BushModel(
//            textures.containsKey( "texture" )
//            ? ResourceLocation.tryCreate( textures.get( "texture" ) )
//            : null
//        );
//    }
//}