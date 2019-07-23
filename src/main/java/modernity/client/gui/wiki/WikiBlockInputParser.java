/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import com.mojang.brigadier.StringReader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.function.BiFunction;

public class WikiBlockInputParser implements BiFunction<String, Integer, String>, GuiWikiTextField.Format<IBlockState> {
    private static final List<String> namespaces = new ArrayList<>();

    @Override
    public IBlockState parse( String input ) {
        setString( input );
        return error ? null : valid;
    }

    static {
        for( ResourceLocation loc : ForgeRegistries.BLOCKS.getKeys() ) {
            String ns = loc.getNamespace();
            if( ! namespaces.contains( ns ) ) {
                namespaces.add( ns );
            }
        }
        namespaces.sort( Comparator.naturalOrder() );
    }

    private static final char invalid = (char) 0;

    private static final ParseType NAMESPACE = new ParseType( TextFormatting.GRAY );
    private static final ParseType ID = new ParseType( TextFormatting.AQUA );
    private static final ParseType ID_UNCHECKED = new ParseType( TextFormatting.GRAY );
    private static final ParseType BRACKETS = new ParseType( TextFormatting.WHITE );
    private static final ParseType PROP_SEPARATOR = new ParseType( TextFormatting.GRAY );
    private static final ParseType KEY_SEPARATOR = new ParseType( TextFormatting.GRAY );
    private static final ParseType NS_SEPARATOR = new ParseType( TextFormatting.GRAY );
    private static final ParseType WHITESPACE = new ParseType( TextFormatting.GRAY );
    private static final ParseType PROPERTY_1 = new ParseType( TextFormatting.YELLOW );
    private static final ParseType PROPERTY_2 = new ParseType( TextFormatting.GREEN );
    private static final ParseType PROPERTY_3 = new ParseType( TextFormatting.LIGHT_PURPLE );
    private static final ParseType PROPERTY_4 = new ParseType( TextFormatting.BLUE );
    private static final ParseType PROPERTY_5 = new ParseType( TextFormatting.GOLD );
    private static final ParseType PROPERTY_6 = new ParseType( TextFormatting.AQUA );
    private static final ParseType VALUE = new ParseType( TextFormatting.WHITE );
    private static final ParseType UNKNOWN = new ParseType( TextFormatting.RED );

    private final List<ParsedToken> tokens = new ArrayList<>();
    private String string = "";
    private StringReader reader = new StringReader( string );
    private Block block;
    private final Set<String> usedProps = new HashSet<>();
    private IBlockState parsed;
    private IBlockState valid;
    private boolean error;

    private int prop;

    private IProperty<?> lastProperty;

    public void setString( String input ) {
        if( input == null ) throw new NullPointerException();
        prop = 0;
        tokens.clear();
        usedProps.clear();
        string = input;
        reader = new StringReader( string );
        error = false;
        parsed = null;
        reparse();
        if( ! error ) {
            valid = parsed;
        }
    }

    private void reparse() {
        skipWhitespaces();
        parseBlockID();
        skipWhitespaces();
        parseBlockProperties();
        skipWhitespaces();
    }

    public String apply( String input, Integer fromIndex ) {
        return highlight( input, fromIndex, fromIndex + input.length() );
    }

    public String highlight( String string, int fromIndex, int toIndex ) {
        StringBuilder builder = new StringBuilder();
        for( ParsedToken token : tokens ) {
            if( token.min >= toIndex ) {
                continue;
            }
            if( token.max <= fromIndex ) {
                continue;
            }

            String str = clip( string, fromIndex, toIndex, token.min, token.max );
            builder.append( token.error ? TextFormatting.RED : token.type.formats );
            builder.append( str );
            builder.append( TextFormatting.RESET );
        }
        return builder.toString();
    }

    private static String clip( String full, int from, int to, int min, int max ) {
        min -= from;
        max -= from;
        to -= from;
        return full.substring( Math.max( 0, min ), Math.min( to, max ) );
    }

    private void add( ParsedToken... tokens ) {
        this.tokens.addAll( Arrays.asList( tokens ) );
        for( ParsedToken t : tokens ) {
            if( t.error ) error = true;
        }
    }

    private void skipWhitespaces() {
        int wsMin = reader.getCursor();
        String ws = readWhitespaces( reader );
        int wsMax = reader.getCursor();
        add( new ParsedToken( wsMin, wsMax, ws, WHITESPACE, false ) );
    }

    private void parseBlockID() {
        int nsMin = reader.getCursor();
        String ns = readIDString( reader );
        int nsMax = reader.getCursor();
        if( reader.canRead() && reader.peek() == ':' ) {
            reader.skip();
            boolean nsValid = isValidNamespace( ns );

            String id = readIDString( reader );
            int idMax = reader.getCursor();

            if( ! nsValid ) {
                add(
                        new ParsedToken( nsMin, nsMax, ns, NAMESPACE, true ),
                        new ParsedToken( nsMax, nsMax + 1, ":", NS_SEPARATOR, false ),
                        new ParsedToken( nsMax + 1, idMax, id, ID_UNCHECKED, false )
                );
            } else {
                boolean blockValid = isValidBlock( ns, id );
                add(
                        new ParsedToken( nsMin, nsMax, ns, NAMESPACE, false ),
                        new ParsedToken( nsMax, nsMax + 1, ":", NS_SEPARATOR, false ),
                        new ParsedToken( nsMax + 1, idMax, id, ID, ! blockValid )
                );
            }
        } else {
            boolean valid = isValidBlock( "minecraft", ns );
            add(
                    new ParsedToken( nsMin, nsMax, ns, ID, ! valid )
            );
        }
    }

    private void parseBlockProperties() {
        // No properties: it's okay...
        if( ! reader.canRead() ) return;
        char open = addBracketOpen();
        if( open == invalid ) {
            return;
        }
        skipWhitespaces();
        if( parseClose( open, true ) ) {
            return;
        }

        while( reader.canRead() ) {
            if( ! reader.canRead() ) {
                break;
            }
            boolean valid = parseProperty();
            if( ! reader.canRead() ) {
                break;
            }
            skipWhitespaces();
            if( parseSeparatorOrClose( open, valid ) == 2 ) {
                break;
            }
            skipWhitespaces();
        }
        skipWhitespaces();
        if( reader.canRead() ) {
            add( new ParsedToken( reader.getCursor(), reader.getTotalLength(), reader.getString().substring( reader.getCursor() ), UNKNOWN, true ) );
        }
    }

    private boolean matchBracket( char open ) {
        boolean b = false;

        int cur = reader.getCursor();
        while( reader.canRead() && ! b ) {
            b = isBracketClose( reader.read(), open );
        }
        reader.setCursor( cur );
        return b;
    }

    private boolean parseClose( char open, boolean valid ) {
        if( ! reader.canRead() ) {
            return false;
        }
        int cursor = reader.getCursor();
        char c = reader.peek();
        if( isBracketClose( c, open ) ) {
            reader.skip();
            add( new ParsedToken( cursor, cursor + 1, c + "", BRACKETS, ! valid ) );
            return true;
        }
        return false;
    }

    private int parseSeparatorOrClose( char open, boolean valid ) {
        if( ! reader.canRead() ) {
            return 2;
        }
        int cursor = reader.getCursor();
        char c = reader.peek();
        if( c == ',' || c == ';' ) {
            reader.skip();
            add( new ParsedToken( cursor, cursor + 1, c + "", PROP_SEPARATOR, ! valid ) );
            return 1;
        }
        if( parseClose( open, valid ) ) {
            return 2;
        }
        reader.skip();
        add( new ParsedToken( cursor, cursor + 1, c + "", UNKNOWN, true ) );
        return 0;
    }

    private boolean parseProperty() {
        lastProperty = null;
        if( ! parsePropertyKey() ) return false;
        skipWhitespaces();
        if( ! parsePropertyEquals() ) return false;
        skipWhitespaces();
        return parsePropertyValue();
    }

    private boolean parsePropertyValue() {
        int valMin = reader.getCursor();
        String val = readIDString( reader );
        if( val.isEmpty() ) return false;
        int valMax = reader.getCursor();
        boolean valid = isValidValue( val );
        add( new ParsedToken( valMin, valMax, val, VALUE, ! valid ) );
        return true;
    }

    private boolean parsePropertyKey() {
        int idMin = reader.getCursor();
        String id = readIDString( reader );
        if( id.isEmpty() ) return false;
        int idMax = reader.getCursor();
        boolean valid = isValidProperty( id );
        add( new ParsedToken( idMin, idMax, id, getNextProperty(), ! valid ) );
        return true;
    }

    private boolean parsePropertyEquals() {
        if( ! reader.canRead() ) {
            return false;
        }
        int cursor = reader.getCursor();
        char c = reader.read();
        if( c == '=' ) {
            add( new ParsedToken( cursor, cursor + 1, "=", KEY_SEPARATOR, false ) );
            return true;
        }
        add( new ParsedToken( cursor, cursor + 1, c + "", UNKNOWN, true ) );
        return false;
    }

    private ParseType getNextProperty() {
        int p = prop;
        prop = ( prop + 1 ) % 6;
        switch( p ) {
            default:
            case 0:
                return PROPERTY_1;
            case 1:
                return PROPERTY_2;
            case 2:
                return PROPERTY_3;
            case 3:
                return PROPERTY_4;
            case 4:
                return PROPERTY_5;
            case 5:
                return PROPERTY_6;
        }
    }

    private char addBracketOpen() {
        if( ! reader.canRead() ) {
            return invalid;
        }
        int cursor = reader.getCursor();
        char c = reader.read();
        if( isBracketOpen( c ) ) {
            boolean valid = matchBracket( c );
            add( new ParsedToken( cursor, cursor + 1, c + "", BRACKETS, ! valid ) );
            return c;
        } else {
            add( new ParsedToken( cursor, reader.getTotalLength(), c + "", UNKNOWN, true ) );
            return invalid;
        }
    }

    private boolean isValidValue( String name ) {
        if( lastProperty == null ) {
            return false;
        }
        Optional opt = lastProperty.parseValue( name );
        if( opt.isPresent() ) {
            IProperty prop = lastProperty;
            parsed = parsed.with( prop, (Comparable) opt.get() );
            return true;
        }
        return false;
    }

    private boolean isValidProperty( String name ) {
        if( block == null ) return false;
        if( usedProps.contains( name ) ) return false;

        StateContainer<Block, IBlockState> container = block.getStateContainer();
        Collection<IProperty<?>> props = container.getProperties();
        for( IProperty<?> prop : props ) {
            if( prop.getName().equals( name ) ) {
                usedProps.add( name );
                lastProperty = prop;
                return true;
            }
        }

        return false;
    }

    private static boolean isBracketOpen( char c ) {
        return c == '[' || c == '{' || c == '(';
    }

    private static boolean isBracketClose( char c, char open ) {
        if( open == '[' ) return c == ']';
        if( open == '{' ) return c == '}';
        if( open == '(' ) return c == ')';
        return false;
    }

    private static boolean isValidNamespace( String ns ) {
        for( String namespace : namespaces ) {
            if( ns.equals( namespace ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidBlock( String ns, String id ) {
        block = null;
        ResourceLocation loc = ResourceLocation.tryCreate( ns + ":" + id );
        if( loc == null ) return false;
        if( ForgeRegistries.BLOCKS.containsKey( loc ) ) {
            block = ForgeRegistries.BLOCKS.getValue( loc );
            Validate.notNull( block );
            parsed = block.getDefaultState();
            return true;
        }
        return false;
    }

    private static boolean isAllowedInID( char c ) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '.'
                || c == '/';
    }

    private static String readIDString( StringReader reader ) {
        final int start = reader.getCursor();
        while( reader.canRead() && isAllowedInID( reader.peek() ) ) {
            reader.skip();
        }
        return reader.getString().substring( start, reader.getCursor() );
    }

    private static String readWhitespaces( StringReader reader ) {
        final int start = reader.getCursor();
        while( reader.canRead() && Character.isWhitespace( reader.peek() ) ) {
            reader.skip();
        }
        return reader.getString().substring( start, reader.getCursor() );
    }

    private static class ParsedToken {
        private final int min;
        private final int max;
        private final String string;
        private final ParseType type;
        private final boolean error;

        private ParsedToken( int min, int max, String string, ParseType type, boolean error ) {
            this.min = min;
            this.max = max;
            this.string = string;
            this.type = type;
            this.error = error;
        }
    }

    private static class ParseType {

        public final String formats;

        public ParseType( TextFormatting... formats ) {
            StringBuilder builder = new StringBuilder();
            for( TextFormatting formatting : formats ) {
                builder.append( formatting );
            }
            this.formats = builder.toString();
        }

        public String color( String token ) {
            return formats + token + TextFormatting.RESET;
        }

        @Override
        public String toString() {
            return formats;
        }
    }
}
