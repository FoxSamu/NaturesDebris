/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.client.colors;

// TODO Re-evaluate
//public class ColorProfile {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private static final HashMap<ResourceLocation, ColorMap> COLOR_MAPS = new HashMap<>();
//
//    private final IColorProvider provider;
//
//    private AtomicLong seed = new AtomicLong();
//    private final ThreadLocal<Cache> cache = new ThreadLocal<>();
//
//    private final ThreadLocal<BlockPos> cachedPos = new ThreadLocal<>();
//    private final ThreadLocal<Long> cachedSeed = new ThreadLocal<>();
//    private final ThreadLocal<Integer> cachedColor = new ThreadLocal<>();
//    private final ThreadLocal<Boolean> cachedWorldNull = new ThreadLocal<>();
//
//    public ColorProfile( IColorProvider provider ) {
//        this.provider = provider;
//        provider.initForSeed( 0 );
//    }
//
//    public int getColor( @Nullable IEnviromentBlockReader world, @Nullable BlockPos pos ) {
//        updateSeed();
//        long seed = ModernityClient.get().getLastWorldSeed();
//
//        if( pos == null ) pos = BlockPos.ZERO;
//
//        int color;
//        if( cachedSeed.get() != null && seed == cachedSeed.get() && pos.equals( cachedPos.get() ) && cachedWorldNull.get() == ( world == null ) ) {
//            return cachedColor.get();
//        } else {
//            color = provider.getColor( world, pos );
//        }
//
//        cachedColor.set( color );
//        cachedPos.set( pos.toImmutable() );
//        cachedSeed.set( seed );
//        cachedWorldNull.set( world == null );
//
//        return color;
//    }
//
//    public int getItemColor() {
//        return getColor( null, null );
//    }
//
//    private void updateSeed() {
//        long newSeed = ModernityClient.get().getLastWorldSeed();
//        if( newSeed != seed.get() ) {
//            seed.set( newSeed );
//            provider.initForSeed( newSeed );
//        }
//    }
//
//    public IColorProvider getProvider() {
//        return provider;
//    }
//
//    public static ColorMap getColorMap( ResourceLocation id ) {
//        return COLOR_MAPS.computeIfAbsent( id, key -> {
//            ColorMap map = new ColorMap( new ResourceLocation(
//                key.getNamespace(),
//                "textures/" + key.getPath() + ".png"
//            ) );
//            map.reload( Minecraft.getInstance().getResourceManager() );
//            return map;
//        } );
//    }
//
//    public static ColorProfile parse( JsonObject object, ResourceLocation id ) {
//        ColorDeserializeContext ctx = new ColorDeserializeContext();
//        IColorProvider color = ctx.silentDeserialize( object, "root" );
//
//        List<ColorDeserializeException> excs = ctx.getExcs();
//
//        if( ! excs.isEmpty() ) {
//            LOGGER.warn( "Color profile '{}' has {} problems:", id, excs.size() );
//            for( ColorDeserializeException exc : ctx.getExcs() ) {
//                LOGGER.warn( " - " + exc.getMessage() );
//            }
//        } else {
//            LOGGER.info( "Color profile '{}' loaded successfully", id );
//        }
//
//        return new ColorProfile( color );
//    }
//
//    public static ColorProfile load( InputStream stream, ResourceLocation id ) {
//        try {
//            JsonParser parser = new JsonParser();
//            JsonElement el = parser.parse( new InputStreamReader( stream ) );
//            return parse( el.getAsJsonObject(), id );
//        } catch( Throwable exc ) {
//            LOGGER.warn( "Color profile '{}' failed loading: {}", id, exc.getMessage() );
//            return new ColorProfile( ErrorColorProvider.INSTANCE );
//        }
//    }
//
//    public static ColorProfile load( IResourceManager manager, ResourceLocation id ) {
//        try {
//            IResource resource = manager.getResource( new ResourceLocation(
//                id.getNamespace(),
//                "colors/" + id.getPath() + ".json"
//            ) );
//            return load( resource.getInputStream(), id );
//        } catch( Throwable exc ) {
//            LOGGER.warn( "Color profile '{}' failed loading: {}", id, exc.getMessage() );
//            return new ColorProfile( ErrorColorProvider.INSTANCE );
//        }
//    }
//
//    private static class Cache {
//        IEnviromentBlockReader world;
//        BlockPos pos;
//        int color;
//
//        boolean canGet( IEnviromentBlockReader world, BlockPos pos ) {
//            return Objects.equals( world, this.world ) && Objects.equals( pos, this.pos );
//        }
//    }
//}
