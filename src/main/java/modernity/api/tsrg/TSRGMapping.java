package modernity.api.tsrg;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to load TSRG mapping files.
 *
 * @author RGSW
 */
public class TSRGMapping {
    private static final Pattern FIELD_PATTERN = Pattern.compile( "^\\t(.*?) (.*?)$" );
    private static final Pattern METHOD_PATTERN = Pattern.compile( "^\\t(.*?) (.*?) (.*?)$" );

    private final HashMap<String, ClassMapping> mappings = new HashMap<>();

    private TSRGMapping() {
    }

    public ClassMapping get( String name ) {
        return mappings.get( name );
    }

    public static TSRGMapping create( Scanner inputScanner ) {
        TSRGMapping mapping = new TSRGMapping();
        List<List<String>> rulesList = scanRulesList( inputScanner );
        for( List<String> rules : rulesList ) {
            ClassMapping cm = ClassMapping.from( rules );
            mapping.mappings.put( cm.name, cm );
        }
        return mapping;
    }

    private static List<List<String>> scanRulesList( Scanner scanner ) {
        List<List<String>> rulesList = new ArrayList<>();
        if( ! scanner.hasNextLine() ) return rulesList;
        String lastClassLine = scanner.nextLine();
        while( scanner.hasNextLine() ) {
            List<String> rules = new ArrayList<>();
            rules.add( lastClassLine );

            while( scanner.hasNextLine() ) {
                String line = scanner.nextLine();
                if( line.startsWith( "\t" ) ) rules.add( line );
                else {
                    lastClassLine = line;
                    break;
                }
            }

            rulesList.add( rules );
        }
        return rulesList;
    }

    public static class ClassMapping {
        private final String name;
        private final HashMap<String, Mapping> obfFields = new HashMap<>();
        private final HashMap<String, Mapping> deobfFields = new HashMap<>();
        private final HashMap<MethodKey, Mapping> obfMethods = new HashMap<>();
        private final HashMap<MethodKey, Mapping> deobfMethods = new HashMap<>();

        public ClassMapping( String name ) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Mapping fieldDeobf( String deobf ) {
            return deobfFields.get( deobf );
        }

        public Mapping fieldObf( String obf ) {
            return obfFields.get( obf );
        }

        public Mapping methodDeobf( MethodKey deobf ) {
            return deobfMethods.get( deobf );
        }

        public Mapping methodObf( MethodKey obf ) {
            return obfMethods.get( obf );
        }

        public static ClassMapping from( List<String> rules ) {
            String firstLine = rules.get( 0 );
            String[] sep = firstLine.split( " " );

            ClassMapping mapping = new ClassMapping( sep[ 0 ] );
            for( int i = 1; i < rules.size(); i++ ) {
                String line = rules.get( i );

                Matcher fieldMatcher = FIELD_PATTERN.matcher( line );
                if( fieldMatcher.matches() ) {
                    String obf = fieldMatcher.group( 1 );
                    String deobf = fieldMatcher.group( 2 );
                    Mapping fm = new Mapping( obf, deobf );
                    mapping.obfFields.put( obf, fm );
                    mapping.deobfFields.put( deobf, fm );
                }

                Matcher methodMatcher = METHOD_PATTERN.matcher( line );
                if( methodMatcher.matches() ) {
                    String obf = methodMatcher.group( 1 );
                    String desc = methodMatcher.group( 2 );
                    String deobf = methodMatcher.group( 3 );
                    Mapping mm = new Mapping( obf, deobf );
                    MethodKey obfKey = new MethodKey( obf, desc );
                    MethodKey deobfKey = new MethodKey( deobf, desc );
                    mapping.obfMethods.put( obfKey, mm );
                    mapping.deobfMethods.put( deobfKey, mm );
                }
            }
            return mapping;
        }
    }

    public static class Mapping {
        public final String obfName;
        public final String deobfName;

        public Mapping( String obfName, String deobfName ) {
            this.obfName = obfName;
            this.deobfName = deobfName;
        }
    }

    public static class MethodKey {
        public final String name;
        public final String desc;

        public MethodKey( String name, String desc ) {
            this.name = name;
            this.desc = desc;
        }

        @Override
        public boolean equals( Object obj ) {
            return obj instanceof MethodKey && ( (MethodKey) obj ).name.equals( name ) && ( (MethodKey) obj ).desc.equals( desc );
        }

        @Override
        public int hashCode() {
            return Objects.hash( name, desc );
        }
    }
}
