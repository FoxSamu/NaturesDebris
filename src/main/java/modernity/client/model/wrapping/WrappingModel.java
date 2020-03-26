/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model.wrapping;


//public class WrappingModel implements IUnbakedModel {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private final ResourceLocation parent;
//    private final ImmutableMap<String, String> custom;
//    private final ImmutableMap<String, String> textures;
//    private final Boolean gui3d;
//    private final Boolean smooth;
//    private final Optional<IModelState> state;
//
//    private IUnbakedModel parentModel;
//    private boolean loadingTextures;
//
//    public WrappingModel( ResourceLocation parent, Map<String, String> custom, Map<String, String> textures, Boolean gui3d, Boolean smooth, Optional<IModelState> state ) {
//        this.parent = parent;
//        this.custom = ImmutableMap.copyOf( custom );
//        this.textures = ImmutableMap.copyOf( textures );
//        this.gui3d = gui3d;
//        this.smooth = smooth;
//        this.state = state;
//    }
//
//    @Override
//    public Collection<ResourceLocation> getDependencies() {
//        return Collections.singleton( parent );
//    }
//
//    @Override
//    public Collection<ResourceLocation> getTextures( Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors ) {
//        try {
//            parentModel = ModelLoaderRegistry.getModel( parent );
//        } catch( Exception e ) {
//            parentModel = ModelLoaderRegistry.getMissingModel();
//        }
//
//        parentModel = parentModel.retexture( textures );
//        parentModel = parentModel.process( custom );
//        if( smooth != null ) parentModel = parentModel.smoothLighting( smooth );
//        if( gui3d != null ) parentModel = parentModel.gui3d( gui3d );
//
//        if( loadingTextures ) {
//            LOGGER.warn( "Can't load textures: circular parent reference!" );
//            return Collections.emptyList();
//        } else {
//            loadingTextures = true;
//            Collection<ResourceLocation> textures = parentModel.getTextures( modelGetter, missingTextureErrors );
//            loadingTextures = false;
//            return textures;
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBakedModel bake( ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format ) {
//        return parentModel != null
//               ? parentModel.bake( bakery, spriteGetter, new Sprite( sprite, state.orElse( ModelRotation.X0_Y0 ) ), format )
//               : null;
//    }
//
//    private Map<String, String> merge( Map<String, String> a, Map<String, String> b ) {
//        HashMap<String, String> map = new HashMap<>( a );
//        map.putAll( b );
//        return map;
//    }
//
//    @Override
//    public IUnbakedModel process( ImmutableMap<String, String> customData ) {
//        return new WrappingModel(
//            parent,
//            merge( custom, customData ),
//            textures,
//            gui3d,
//            smooth,
//            state
//        );
//    }
//
//    @Override
//    public IUnbakedModel retexture( ImmutableMap<String, String> tex ) {
//        return new WrappingModel(
//            parent,
//            custom,
//            merge( textures, tex ),
//            gui3d,
//            smooth,
//            state
//        );
//    }
//
//    @Override
//    public IUnbakedModel gui3d( boolean value ) {
//        return new WrappingModel( parent, custom, textures, value, smooth, state );
//    }
//
//    @Override
//    public IUnbakedModel smoothLighting( boolean value ) {
//        return new WrappingModel( parent, custom, textures, gui3d, value, state );
//    }
//
//    private static class Sprite implements ISprite {
//
//        private final ISprite parent;
//        private final IModelState state;
//
//        private Sprite( ISprite parent, IModelState state ) {
//            this.parent = parent;
//            this.state = state;
//        }
//
//        @Override
//        @SuppressWarnings( "deprecation" )
//        public ModelRotation getRotation() {
//            return parent.getRotation();
//        }
//
//        @Override
//        public boolean isUvLock() {
//            return parent.isUvLock();
//        }
//
//        @Override
//        public IModelState getState() {
//            return state;
//        }
//    }
//}
