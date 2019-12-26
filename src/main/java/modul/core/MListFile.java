/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MListFile implements Iterable<String> {
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile( "^\\s*$" );
    private static final Pattern COMMENT_LINE_PATTERN = Pattern.compile( "^\\s*?#.*$" );
    private static final Pattern ROOT_LINE_PATTERN = Pattern.compile( "^\\s*(.*?)\\s*$" );
    private static final Pattern CONDITION_LINE_PATTERN = Pattern.compile( "^\\s*?-\\s*(.*?)\\s*$" );
    private static final Pattern NUMBER_PATTERN = Pattern.compile( "^[0-9]+$" );
    private static final Pattern CONDITIONAL_NAME = Pattern.compile( "^(.*?)\\s*\\[(.*?)]\\s*$" );

    private final ArrayList<String> entries = new ArrayList<>();

    private MListFile() {
    }

    @Override
    public Iterator<String> iterator() {
        return entries.iterator();
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public boolean contains( String str ) {
        return entries.contains( str );
    }

    public static MListFile load( Context ctx, File file ) throws Exception {
        return load( ctx, new Scanner( file ) );
    }

    public static MListFile load( Context ctx, InputStream file ) throws Exception {
        return load( ctx, new Scanner( file ) );
    }

    public static MListFile load( Context ctx, Reader file ) throws Exception {
        return load( ctx, new Scanner( file ) );
    }

    public static MListFile load( Context ctx, String file ) throws Exception {
        return load( ctx, new Scanner( file ) );
    }

    public static MListFile load( Context ctx, Scanner scanner ) throws Exception {
        ArrayList<String> lines = new ArrayList<>();

        while( scanner.hasNextLine() ) {
            lines.add( scanner.nextLine() );
        }

        IOException exc = scanner.ioException();
        if( exc != null ) {
            throw exc;
        }

        scanner.close();

        MListFile file = new MListFile();

        int index = 0;
        while( index < lines.size() ) {
            index = parseEntry( ctx, file, lines, index );
        }

        return file;
    }

    private static int parseEntry( Context context, MListFile file, List<String> lines, int index ) throws Exception {
        if( index >= lines.size() ) {
            return index;
        }
        String next = lines.get( index );
        Matcher emptyMatcher = EMPTY_LINE_PATTERN.matcher( next );
        Matcher commentMatcher = COMMENT_LINE_PATTERN.matcher( next );
        while( emptyMatcher.matches() || commentMatcher.matches() ) {
            index++;
            if( index >= lines.size() ) {
                return index;
            }
            next = lines.get( index );
            emptyMatcher.reset( next );
            commentMatcher.reset( next );
        }

        Matcher rootMatcher = ROOT_LINE_PATTERN.matcher( next );

        if( ! rootMatcher.matches() ) {
            throw new Exception( "Line " + ( index + 1 ) + ": Malformed line!" );
        }

        String name = rootMatcher.group( 1 );
        index++;

        ArrayList<Boolean> conditions = new ArrayList<>();

        if( index < lines.size() ) {
            next = lines.get( index );

            emptyMatcher = EMPTY_LINE_PATTERN.matcher( next );
            commentMatcher = COMMENT_LINE_PATTERN.matcher( next );
            Matcher conditionMatcher = CONDITION_LINE_PATTERN.matcher( next );
            boolean conditionMatched = false;
            while( emptyMatcher.matches() || commentMatcher.matches() || ( conditionMatched = conditionMatcher.matches() ) ) {
                if( conditionMatched ) {
                    String condition = conditionMatcher.group( 1 );
                    conditions.add( context.test( condition ) );
                }
                index++;
                if( index >= lines.size() ) {
                    break;
                }
                next = lines.get( index );
                emptyMatcher.reset( next );
                commentMatcher.reset( next );
                conditionMatcher.reset( next );
                conditionMatched = false;
            }
        }


        Matcher condNameMatcher = CONDITIONAL_NAME.matcher( name );
        boolean condition;
        if( condNameMatcher.matches() ) {
            name = condNameMatcher.group( 1 );
            String expr = condNameMatcher.group( 2 );
            try {
                condition = parseConditionExpression( conditions, expr );
            } catch( Exception exc ) {
                throw new Exception( "Line " + ( index + 1 ) + ": Invalid Condition: " + exc.getMessage() );
            }
        } else {
            condition = true;
            for( boolean b : conditions ) {
                condition = b;
                if( ! condition ) break;
            }
        }

        if( condition ) {
            if( ! file.entries.contains( name ) ) file.entries.add( name );
        }
        return index;
    }

    private static boolean parseConditionExpression( ArrayList<Boolean> conditions, String expr ) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();

        for( int i = 0; i < expr.length(); ) {
            char c = expr.charAt( i );
            if( isDigit( c ) ) {
                StringBuilder numBuilder = new StringBuilder( c + "" );
                i++;
                while( i < expr.length() && isDigit( expr.charAt( i ) ) ) {
                    numBuilder.append( expr.charAt( i ) );
                    i++;
                }

                tokens.add( numBuilder.toString() );
            } else if( c == '(' ) {
                int parenIndex = i;
                i++;
                int nesting = 1;
                while( i < expr.length() && nesting > 0 ) {
                    if( expr.charAt( i ) == '(' ) {
                        nesting++;
                    }
                    if( expr.charAt( i ) == ')' ) {
                        nesting--;
                    }
                    i++;
                }
                if( nesting != 0 ) {
                    throw new Exception( "Unmatched parentheses" );
                }
                tokens.add( expr.substring( parenIndex, i ) );
            } else if( c == ')' ) {
                throw new Exception( "Unmatched parentheses" );
            } else if( c == '&' || c == '|' || c == '!' ) {
                tokens.add( c + "" );
                i++;
            } else if( c == ' ' ) {
                i++;
            } else {
                throw new Exception( "Unexpected token '" + c + "'" );
            }
        }

        return parseCondition( conditions, tokens, "Expected a condition" );
    }

    private static boolean parseCondition( ArrayList<Boolean> conditions, List<String> tokens, String noCondition ) throws Exception {
        if( tokens.isEmpty() ) {
            throw new Exception( noCondition );
        }

        Boolean c = parseAndCondition( conditions, tokens );
        if( c == null ) c = parseOrCondition( conditions, tokens );
        if( c == null ) c = parseNotCondition( conditions, tokens );
        if( c == null ) c = parseParenCondition( conditions, tokens );
        if( c == null ) c = parseIndexedCondition( conditions, tokens );

        if( c == null ) {
            throw new Exception( "Malformed condition expression!" );
        }
        return c;
    }

    private static Boolean parseAndCondition( ArrayList<Boolean> conditions, List<String> tokens ) throws Exception {
        int andIndex = tokens.indexOf( "&" );
        if( andIndex < 0 ) return null;

        boolean left = parseCondition( conditions, tokens.subList( 0, andIndex ), "Expected condition at left hand side of AND operator" );
        boolean right = parseCondition( conditions, tokens.subList( andIndex + 1, tokens.size() ), "Expected condition at right hand side of AND operator" );

        return left && right;
    }

    private static Boolean parseOrCondition( ArrayList<Boolean> conditions, List<String> tokens ) throws Exception {
        int orIndex = tokens.indexOf( "|" );
        if( orIndex < 0 ) return null;

        boolean left = parseCondition( conditions, tokens.subList( 0, orIndex ), "Expected condition at left hand side of OR operator" );
        boolean right = parseCondition( conditions, tokens.subList( orIndex + 1, tokens.size() ), "Expected condition at right hand side of OR operator" );

        return left || right;
    }

    private static Boolean parseNotCondition( ArrayList<Boolean> conditions, List<String> tokens ) throws Exception {
        if( tokens.isEmpty() ) {
            throw new Exception( "Expected condition at right hand side of NOT operator" );
        }
        String firstToken = tokens.get( 0 );
        if( ! firstToken.equals( "!" ) ) return null;

        boolean right = parseCondition( conditions, tokens.subList( 1, tokens.size() ), "Expected condition at right hand side of NOT operator" );

        return ! right;
    }

    private static Boolean parseParenCondition( ArrayList<Boolean> conditions, List<String> tokens ) throws Exception {
        String firstToken = tokens.get( 0 );
        if( ! firstToken.startsWith( "(" ) ) return null;

        return parseConditionExpression( conditions, firstToken.substring( 1, firstToken.length() - 1 ) );
    }

    private static Boolean parseIndexedCondition( ArrayList<Boolean> conditions, List<String> tokens ) throws Exception {
        String firstToken = tokens.get( 0 );
        if( ! NUMBER_PATTERN.asPredicate().test( firstToken ) ) return null;

        int index;
        try {
            index = Integer.parseInt( firstToken );
        } catch( NumberFormatException exc ) {
            throw new Exception( "Malformed index: " + firstToken );
        }

        if( index >= 0 && index < conditions.size() ) {
            return conditions.get( index );
        } else {
            throw new Exception( "Invalid index: " + firstToken );
        }
    }

    private static boolean isDigit( char c ) {
        return c >= '0' && c <= '9';
    }

    public static Context context() {
        return new Context();
    }

    public static Condition not( String prefix ) {
        return ( ctx, str ) -> {
            if( str.startsWith( prefix + " " ) ) {
                return ! ctx.test( str.substring( prefix.length() + 1 ) );
            }
            return false;
        };
    }

    public static Condition regex( String prefix, String regex ) {
        return ( ctx, str ) -> {
            if( str.startsWith( prefix + " " ) ) {
                return str.substring( prefix.length() + 1 ).matches( regex );
            }
            return false;
        };
    }

    public static Condition regex( String prefix, Pattern regex ) {
        return ( ctx, str ) -> {
            if( str.startsWith( prefix + " " ) ) {
                return regex.asPredicate().test( str.substring( prefix.length() + 1 ) );
            }
            return false;
        };
    }

    public static Condition prefix( String prefix, Condition condition ) {
        return ( ctx, str ) -> {
            if( str.startsWith( prefix + " " ) ) {
                return condition.test( ctx, str.substring( prefix.length() + 1 ) );
            }
            return false;
        };
    }

    public static Condition prefix( String prefix, Predicate<String> condition ) {
        return ( ctx, str ) -> {
            if( str.startsWith( prefix + " " ) ) {
                return condition.test( str.substring( prefix.length() + 1 ) );
            }
            return false;
        };
    }

    public static class Context {
        private final ArrayList<Condition> conditions = new ArrayList<>();

        private Context() {
        }

        public Context withCondition( Condition condition ) {
            conditions.add( condition );
            return this;
        }

        public Context withCondition( Predicate<String> condition ) {
            conditions.add( ( ctx, string ) -> condition.test( string ) );
            return this;
        }

        public boolean test( String str ) {
            for( Condition pr : conditions ) {
                if( pr.test( this, str ) ) {
                    return true;
                }
            }
            return false;
        }
    }

    @FunctionalInterface
    public interface Condition {
        boolean test( Context ctx, String string );
    }
}
