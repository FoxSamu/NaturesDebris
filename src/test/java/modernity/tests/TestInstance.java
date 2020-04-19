package modernity.tests;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TestInstance {
    private final String className;
    private final String name;
    private IModernityTest instance;
    private boolean modEventBus;
    private boolean forgeEventBus;
    private boolean fmlEventBus;

    public TestInstance( String className, String name ) {
        this.className = className;
        this.name = name;
    }

    public boolean needsModEventBus() {
        return modEventBus;
    }

    public boolean needsForgeEventBus() {
        return forgeEventBus;
    }

    public boolean needsFMLEventBus() {
        return fmlEventBus;
    }

    public IModernityTest getTestInstance() {
        return instance;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public TestInstance load( Function<String, IModernityTest> testFactory ) {
        if( instance == null ) {
            instance = testFactory.apply( className );
            if( instance == null ) return null;
        }
        return this;
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        TestInstance that = (TestInstance) o;

        return className.equals( that.className );
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public static TestInstance deserialize( String key, JsonElement element ) {
        if( element.isJsonPrimitive() ) {
            return new TestInstance( element.getAsString(), key == null ? element.getAsString() : key );
        } else if( element.isJsonObject() ) {
            JsonObject obj = element.getAsJsonObject();
            if( ! obj.has( "class" ) ) throw new JsonSyntaxException( "Field 'class' must be defined in test entry" );
            if( ! obj.has( "name" ) && key == null )
                throw new JsonSyntaxException( "Field 'name' must be defined in test entry as no key is specified" );

            TestInstance instance = new TestInstance(
                obj.get( "class" ).getAsString(),
                key == null
                ? obj.get( "name" ).getAsString()
                : obj.has( "name" )
                  ? obj.get( "name" ).getAsString()
                  : key
            );
            if( obj.has( "eventBuses" ) ) {
                instance.readEventBuses( obj.get( "eventBuses" ) );
            }
            return instance;
        } else {
            throw new JsonSyntaxException( "Test entry must be an array or string" );
        }
    }

    private void readEventBuses( JsonElement element ) {
        if( element.isJsonObject() ) {
            JsonObject object = element.getAsJsonObject();
            modEventBus = JSONUtils.getBoolean( object, "modernity", false ) || JSONUtils.getBoolean( object, "all", false );
            fmlEventBus = JSONUtils.getBoolean( object, "fml", false ) || JSONUtils.getBoolean( object, "main", false ) || JSONUtils.getBoolean( object, "all", false );
            forgeEventBus = JSONUtils.getBoolean( object, "forge", false ) || JSONUtils.getBoolean( object, "main", false ) || JSONUtils.getBoolean( object, "all", false );
        } else if( element.isJsonArray() ) {
            JsonArray array = element.getAsJsonArray();
            Set<String> entries = StreamSupport.stream( array.spliterator(), false )
                                               .map( JsonElement::getAsString )
                                               .map( String::toLowerCase )
                                               .collect( Collectors.toSet() );

            modEventBus = entries.contains( "all" ) || entries.contains( "modernity" );
            fmlEventBus = entries.contains( "all" ) || entries.contains( "fml" ) || entries.contains( "main" );
            forgeEventBus = entries.contains( "all" ) || entries.contains( "forge" ) || entries.contains( "main" );
        } else if( element.isJsonPrimitive() ) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if( primitive.isString() ) {
                String str = element.getAsString().toLowerCase();
                modEventBus = str.equals( "all" ) || str.equals( "modernity" );
                fmlEventBus = str.equals( "all" ) || str.equals( "fml" ) || str.equals( "main" );
                forgeEventBus = str.equals( "all" ) || str.equals( "forge" ) || str.equals( "main" );
            } else if( primitive.isBoolean() ) {
                modEventBus = primitive.getAsBoolean();
                fmlEventBus = primitive.getAsBoolean();
                forgeEventBus = primitive.getAsBoolean();
            }
        }
    }
}
