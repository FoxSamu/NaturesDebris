/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.deserializer;

// TODO Re-evaluate
//public class BiomeColorProviderDeserializer implements IColorProviderDeserializer {
//    @Override
//    public IColorProvider deserialize( JsonElement element, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( element.isJsonObject() ) {
//            JsonObject object = element.getAsJsonObject();
//
//            HashMap<Predicate<String>, IColorProvider> providers = new HashMap<>();
//            for( Map.Entry<String, JsonElement> entry : object.entrySet() ) {
//                if( entry.getKey().equals( "radius" ) ) continue;
//                providers.put( biomeSelector( entry.getKey(), ctx ), ctx.silentDeserialize( entry.getValue(), entry.getKey() ) );
//            }
//
//            IColorProvider fallback = ErrorColorProvider.INSTANCE;
//            IColorProvider item = ErrorColorProvider.INSTANCE;
//
//            HashMap<Biome, IColorProvider> flattened = new HashMap<>();
//            for( Biome biome : ForgeRegistries.BIOMES ) {
//                for( Map.Entry<Predicate<String>, IColorProvider> entry : providers.entrySet() ) {
//                    String id = biome.getRegistryName() + "";
//                    if( entry.getKey().test( id ) ) {
//                        flattened.put( biome, entry.getValue() );
//                        break;
//                    }
//                }
//            }
//
//            for( Map.Entry<Predicate<String>, IColorProvider> entry : providers.entrySet() ) {
//                if( entry.getKey().test( "item" ) ) {
//                    item = entry.getValue();
//                    break;
//                }
//            }
//
//            for( Map.Entry<Predicate<String>, IColorProvider> entry : providers.entrySet() ) {
//                if( entry.getKey().test( "default" ) ) {
//                    fallback = entry.getValue();
//                    break;
//                }
//            }
//
//            int radius = - 1;
//            if( object.has( "radius" ) && object.get( "radius" ).isJsonPrimitive() ) {
//                radius = object.getAsJsonPrimitive( "radius" ).getAsInt();
//            }
//
//            BiomeSpecificColorProvider provider = new BiomeSpecificColorProvider( fallback, item, radius );
//
//            for( Map.Entry<Biome, IColorProvider> e : flattened.entrySet() ) {
//                provider.addColor( e.getKey(), e.getValue() );
//            }
//
//            return provider;
//        } else {
//            throw ctx.exception( "Expected an object" );
//        }
//    }
//
//    private static Predicate<String> biomeSelector( String sel, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        String selector = sel.trim().toLowerCase();
//        if( selector.isEmpty() ) throw ctx.exception( "Empty selector" );
//
//        int or = selector.indexOf( '|' );
//        if( or >= 0 ) {
//            Predicate<String> a = biomeSelector( selector.substring( 0, or ), ctx );
//            Predicate<String> b = biomeSelector( selector.substring( or + 1 ), ctx );
//            return str -> a.test( str ) || b.test( str );
//        }
//
//        int and = selector.indexOf( '&' );
//        if( and >= 0 ) {
//            Predicate<String> a = biomeSelector( selector.substring( 0, and ), ctx );
//            Predicate<String> b = biomeSelector( selector.substring( and + 1 ), ctx );
//            return str -> a.test( str ) && b.test( str );
//        }
//
//        if( selector.startsWith( "!" ) ) {
//            Predicate<String> a = biomeSelector( selector.substring( 1 ), ctx );
//            return str -> ! a.test( str );
//        }
//
//        if( selector.equals( "*" ) ) {
//            return str -> true;
//        }
//
//        if( selector.equals( "~" ) ) {
//            return str -> false;
//        }
//
//        if( selector.startsWith( "#" ) ) {
//            Set<String> group = BiomeGroups.getBiomeGroup( sel.substring( 1 ) );
//            return group::contains;
//        }
//
//        return str -> str.equals( selector );
//    }
//
//    private static IColorProvider[] parseArray( JsonArray array, ColorDeserializeContext ctx ) throws ColorDeserializeException {
//        if( array.size() == 0 ) {
//            throw ctx.exception( "Required at least one entry to pick from" );
//        }
//
//        IColorProvider[] providers = new IColorProvider[ array.size() ];
//        int i = 0;
//        for( JsonElement element : array ) {
//            providers[ i ] = ctx.silentDeserialize( element, "" + i );
//        }
//        return providers;
//    }
//}
