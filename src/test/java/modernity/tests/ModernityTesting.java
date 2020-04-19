package modernity.tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import modernity.api.IModernity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.userdev.LaunchTesting;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModernityTesting implements IModernity {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Set<TestInstance> loadedTests = new HashSet<>();
    private static boolean available;

    public static void main( String[] args ) throws Exception {
        available = true;

        URL url = ModernityTesting.class.getClassLoader().getResource( "modernity_tests.json" );
        if( url != null ) {
            InputStream is = url.openStream();
            Reader reader = new InputStreamReader( is );
            JsonElement element = new JsonParser().parse( reader );
            reader.close();

            System.out.println( element );

            Stream<Pair<String, JsonElement>> testElements;

            if( element.isJsonObject() ) {
                testElements =
                    element.getAsJsonObject()
                           .entrySet()
                           .stream()
                           .flatMap( en -> en.getValue().isJsonArray()
                                           ? StreamSupport.stream( en.getValue().getAsJsonArray().spliterator(), false )
                                                          .map( el -> Pair.of( null, el ) )
                                           : Stream.of( Pair.of( en.getKey(), en.getValue() ) ) );
            } else if( element.isJsonArray() ) {
                testElements = StreamSupport.stream( element.getAsJsonArray().spliterator(), false )
                                            .map( el -> Pair.of( null, el ) );
            } else {
                throw new JsonSyntaxException( "Root element of tests.json must be object or array" );
            }

            Predicate<TestInstance> enabled = readEnabled();

            Stream<TestInstance> elements = testElements.map( pair -> TestInstance.deserialize( pair.getLeft(), pair.getRight() ) )
                                                        .filter( enabled )
                                                        .map( inst -> inst.load( ModernityTesting::createTestFromName ) )
                                                        .filter( Objects::nonNull );

            loadedTests.addAll( elements.collect( Collectors.toSet() ) );
        }

        if( loadedTests.size() == 0 ) {
            LOGGER.warn( "No test module found! Launching default..." );

            available = false;
        } else {
            LOGGER.info( "Launching modernity test suite with {} tests: {}", loadedTests.size(), loadedTests );
        }

        if( available ) {
            System.setProperty( "modernity.testing.available", "true" );
        }

        // Our configuration done above will be detected by ModernityBootstrap, and tests will load
        LaunchTesting.main( args );
    }

    private static Predicate<TestInstance> readEnabled() throws Exception {
        File tests = new File( "tests.json" );
        FileReader reader = new FileReader( tests );
        JsonElement element = new JsonParser().parse( reader );
        if( element.isJsonArray() ) {
            Set<String> enabled = StreamSupport.stream( element.getAsJsonArray().spliterator(), false )
                                               .map( JsonElement::getAsString )
                                               .collect( Collectors.toSet() );

            return in -> enabled.contains( in.getName() );
        } else if( element.isJsonObject() ) {
            Set<String> disabled = element.getAsJsonObject()
                                          .entrySet()
                                          .stream()
                                          .filter( en -> {
                                              if( en.getValue().isJsonPrimitive() ) {
                                                  return true;
                                              }
                                              LOGGER.error( "External 'tests.json' specifies incorrect enabled value - must be a primitive" );
                                              return false;
                                          } )
                                          .map( en -> Pair.of( en.getKey(), ! readEnabled( en.getValue().getAsJsonPrimitive() ) ) )
                                          .filter( Pair::getRight )
                                          .map( Pair::getLeft )
                                          .collect( Collectors.toSet() );

            return in -> ! disabled.contains( in.getName() );
        } else {
            return in -> true;
        }
    }

    private static boolean readEnabled( JsonPrimitive prim ) {
        if( prim.isBoolean() ) return prim.getAsBoolean();
        return prim.getAsString().equalsIgnoreCase( "enabled" ) || prim.getAsString().equalsIgnoreCase( "true" );
    }

    private static IModernityTest createTestFromName( String name ) {
        try {
            Class<?> cls = Class.forName( name );
            return (IModernityTest) cls.newInstance();
        } catch( Exception e ) {
            LOGGER.error( "Failed to load test class: " + name, e );
            return null;
        }
    }

    public static boolean isAvailable() {
        return available;
    }

    @Override
    public void registerListeners() {
        for( TestInstance testInstance : loadedTests ) {
            IModernityTest test = testInstance.getTestInstance();

            if( testInstance.needsForgeEventBus() ) MinecraftForge.EVENT_BUS.register( test );
            if( testInstance.needsFMLEventBus() ) FMLJavaModLoadingContext.get().getModEventBus().register( test );
            if( testInstance.needsModEventBus() ) IModernity.EVENT_BUS.register( test );
            test.registerListeners();
            System.out.println( "there" );
        }
    }

    @Override
    public void setupRegistryHandler() {
        for( TestInstance testInstance : loadedTests ) {
            IModernityTest test = testInstance.getTestInstance();
            test.setupRegistryHandler();
        }
    }

    @Override
    public void preInit() {
        for( TestInstance testInstance : loadedTests ) {
            IModernityTest test = testInstance.getTestInstance();
            test.preInit();
        }
    }

    @Override
    public void init() {
        for( TestInstance testInstance : loadedTests ) {
            IModernityTest test = testInstance.getTestInstance();
            test.init();
        }
    }

    @Override
    public void postInit() {
        for( TestInstance testInstance : loadedTests ) {
            IModernityTest test = testInstance.getTestInstance();
            test.postInit();
        }
    }
}
