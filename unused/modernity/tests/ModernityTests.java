package modernity.tests;

import modernity.generic.IRunMode;

import java.util.HashMap;
import java.util.Properties;

public final class ModernityTests {
    private static final HashMap<String, IRunMode> TEST_MODES = new HashMap<>();

    public static final IRunMode MODELS = mode( "models", "modernity.tests.models.ModelsTest" );

    private ModernityTests() {
    }

    private static IRunMode mode( String testName, String className ) {
        IRunMode mode = () -> className;
        TEST_MODES.put( testName, mode );
        return mode;
    }

    public static IRunMode getTest() {
        Properties props = System.getProperties();
        if( props.containsKey( "modernity.test" ) ) {
            return TEST_MODES.get( props.getProperty( "modernity.test" ) );
        }
        return null;
    }
}
