/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.client.model.empty;


// TODO Re-evaluate
//public class EmptyModel implements IUnbakedModel {
//
//    @Override
//    public Collection<ResourceLocation> getDependencies() {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
//        return Collections.emptyList();
//    }
//
//    @Nullable
//    @Override
//    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
//        return new Baked( spriteGetter.apply( MissingTextureSprite.getLocation() ) );
//    }
//
//    public static class Baked implements IBakedModel {
//        private final TextureAtlasSprite particleTex;
//
//        public Baked( TextureAtlasSprite particleTex ) {
//            this.particleTex = particleTex;
//        }
//
//        @Override
//        public List<BakedQuad> getQuads( @Nullable BlockState state, @Nullable Direction side, Random rand ) {
//            return Collections.emptyList();
//        }
//
//        @Override
//        public boolean isAmbientOcclusion() {
//            return false;
//        }
//
//        @Override
//        public boolean isGui3d() {
//            return false;
//        }
//
//        @Override
//        public boolean isBuiltInRenderer() {
//            return true;
//        }
//
//        @Override
//        public TextureAtlasSprite getParticleTexture() {
//            return particleTex;
//        }
//
//        @Override
//        public ItemOverrideList getOverrides() {
//            return ItemOverrideList.EMPTY;
//        }
//    }
//}
