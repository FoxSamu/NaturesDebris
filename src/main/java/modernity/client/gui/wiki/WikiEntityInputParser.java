/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

public class WikiEntityInputParser implements BiFunction<String, Integer, String>, GuiWikiTextField.Format<WikiEntityType> {
    private static final List<String> namespaces = new ArrayList<>();

    @Override
    public WikiEntityType parse( String input ) {
        setString( input );
        return error ? null : result;
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

    private static final ParseType NAMESPACE = new ParseType( TextFormatting.GRAY );
    private static final ParseType ID = new ParseType( TextFormatting.AQUA );
    private static final ParseType ID_UNCHECKED = new ParseType( TextFormatting.GRAY );
    private static final ParseType NS_SEPARATOR = new ParseType( TextFormatting.GRAY );
    private static final ParseType WHITESPACE = new ParseType( TextFormatting.GRAY );
    private static final ParseType JSON = new ParseType( TextFormatting.GREEN );

    private final List<ParsedToken> tokens = new ArrayList<>();
    private String string = "";
    private StringReader reader = new StringReader( string );
    private EntityType entityType;
    private WikiEntityType result;
    private boolean error;

    public void setString( String input ) {
        if( input == null ) throw new NullPointerException();
        result = null;
        tokens.clear();
        string = input;
        reader = new StringReader( string );
        error = false;
        reparse();
    }

    private void reparse() {
        skipWhitespaces();
        parseEntityID();
        skipWhitespaces();
        parseEntityNBT();
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

    private void parseEntityID() {
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
                boolean entityValid = isValidEntity( ns, id );
                add(
                        new ParsedToken( nsMin, nsMax, ns, NAMESPACE, false ),
                        new ParsedToken( nsMax, nsMax + 1, ":", NS_SEPARATOR, false ),
                        new ParsedToken( nsMax + 1, idMax, id, ID, ! entityValid )
                );
            }
        } else {
            boolean valid = isValidEntity( "minecraft", ns );
            add(
                    new ParsedToken( nsMin, nsMax, ns, ID, ! valid )
            );
        }
    }

    private void parseEntityNBT() {
        if( ! reader.canRead() ) {
            result = new WikiEntityType( entityType, new NBTTagCompound() );
            return;
        }
        String str = string.substring( reader.getCursor() );
        int cur = reader.getCursor();
        try {
            JsonToNBT jtn = new JsonToNBT( reader );
            NBTTagCompound compound = jtn.readStruct();
            result = new WikiEntityType( entityType, compound );
            add( new ParsedToken( cur, reader.getTotalLength(), str, JSON, false ) );
        } catch( JsonSyntaxException | CommandSyntaxException exc ) {
            add( new ParsedToken( cur, reader.getTotalLength(), str, JSON, true ) );
        }
    }

    private static boolean isValidNamespace( String ns ) {
        for( String namespace : namespaces ) {
            if( ns.equals( namespace ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEntity( String ns, String id ) {
        entityType = null;
        ResourceLocation loc = ResourceLocation.tryCreate( ns + ":" + id );
        if( loc == null ) return false;
        if( ForgeRegistries.ENTITIES.containsKey( loc ) ) {
            entityType = ForgeRegistries.ENTITIES.getValue( loc );
            Validate.notNull( entityType );
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
