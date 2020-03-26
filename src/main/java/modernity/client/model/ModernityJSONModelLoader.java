/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.client.model;


//public abstract class ModernityJSONModelLoader implements ICustomModelLoader {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private final Object2ObjectLinkedOpenHashMap<ResourceLocation, Either<JsonObject, Exception>> cache = new Object2ObjectLinkedOpenHashMap<>();
//
//    private final String type;
//
//    protected ModernityJSONModelLoader( String type ) {
//        this.type = type;
//    }
//
//    @Override
//    public void onResourceManagerReload( IResourceManager resourceManager ) {
//        cache.clear();
//    }
//
//    @Override
//    public boolean accepts( ResourceLocation rl ) {
//        Either<JsonObject, Exception> either = getJSON( rl );
//        boolean[] out = { false };
//        either.ifLeft( obj -> {
//            if( obj.has( "modernity" ) && obj.get( "modernity" ).isJsonPrimitive() && obj.getAsJsonPrimitive( "modernity" ).isString() ) {
//                if( obj.getAsJsonPrimitive( "modernity" ).getAsString().equalsIgnoreCase( type ) ) {
//                    LOGGER.info( "Loading model '{}' as '{}' modernity model", rl, type );
//                    out[ 0 ] = true;
//                }
//            }
//        } );
//        either.ifRight( exc -> out[ 0 ] = false );
//        return out[ 0 ];
//    }
//
//    private Either<JsonObject, Exception> getJSON( ResourceLocation loc ) {
//        if( ! loc.getPath().endsWith( ".json" ) )
//            loc = new ResourceLocation( loc.getNamespace(), loc.getPath() + ".json" );
//
//        if( cache.containsKey( loc ) ) {
//            return cache.getAndMoveToFirst( loc );
//        }
//
//        Either<JsonObject, Exception> either;
//        try {
//            IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
//
//            IResource resource = resourceManager.getResource( loc );
//
//            InputStream stream = resource.getInputStream();
//
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse( new InputStreamReader( stream ) );
//
//            if( ! element.isJsonObject() ) throw new Exception( "Object expected" );
//
//            either = Either.left( element.getAsJsonObject() );
//
//            stream.close();
//        } catch( Exception exc ) {
//            either = Either.right( exc );
//        }
//
//        cache.putAndMoveToFirst( loc, either );
//        if( cache.size() > 60 ) {
//            cache.removeLast();
//        }
//
//        return either;
//    }
//
//    @Override
//    public IUnbakedModel loadModel( ResourceLocation rl ) throws Exception {
//        Either<JsonObject, Exception> either = getJSON( rl );
//        Optional<Exception> exc = either.right();
//        if( exc.isPresent() ) throw exc.get();
//        JsonObject obj = either.orThrow();
//        return loadModel( obj );
//    }
//
//    public abstract IUnbakedModel loadModel( JsonObject object ) throws Exception;
//}
